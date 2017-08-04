package nl.esciencecenter.xenon.cli.exec;

import net.sourceforge.argparse4j.inf.Namespace;
import nl.esciencecenter.xenon.XenonException;
import nl.esciencecenter.xenon.cli.XenonCommand;
import nl.esciencecenter.xenon.schedulers.JobDescription;
import nl.esciencecenter.xenon.schedulers.Scheduler;
import nl.esciencecenter.xenon.schedulers.Streams;
import nl.esciencecenter.xenon.utils.StreamForwarder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static nl.esciencecenter.xenon.cli.Utils.*;

/**
 * Command to execute job in the foreground
 */
public class ExecCommand extends XenonCommand {
    private final Logger logger = LoggerFactory.getLogger(ExecCommand.class);

    @Override
    public Object run(Namespace res) throws XenonException {
        Scheduler scheduler = createScheduler(res);
        JobDescription description = getJobDescription(res);
        long waitTimeout = res.getLong("wait_timeout");

        Streams streams = scheduler.submitInteractiveJob(description);
        StreamForwarder stdinForwarder = new StreamForwarder(System.in, streams.getStdin());
        StreamForwarder stderrForwarder = new StreamForwarder(streams.getStderr(), System.err);
        try {
            // Using copy instead of StreamForwarder to pipe stdout in main thread,
            // so close is called after all stdout has been produced
            pipe(streams.getStdout(), System.out);
            streams.getStdout().close();
        } catch (IOException e) {
            logger.warn("Copy stdout failed", e);
        }
        scheduler.waitUntilDone(streams.getJobIdentifier(), waitTimeout);
        stdinForwarder.terminate(1000);
        stderrForwarder.terminate(1000);
        scheduler.close();
        // run has no output, because all output has already been sent to stdout and stderr.
        return null;
    }


}
