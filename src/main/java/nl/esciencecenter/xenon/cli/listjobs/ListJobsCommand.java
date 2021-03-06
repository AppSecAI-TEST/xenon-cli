package nl.esciencecenter.xenon.cli.listjobs;

import static nl.esciencecenter.xenon.cli.Utils.createScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.esciencecenter.xenon.XenonException;
import nl.esciencecenter.xenon.cli.XenonCommand;
import nl.esciencecenter.xenon.schedulers.JobStatus;
import nl.esciencecenter.xenon.schedulers.Scheduler;

import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Command to list jobs of scheduler
 */
public class ListJobsCommand extends XenonCommand {
    @Override
    public ListJobsOutput run(Namespace res) throws XenonException {
        Scheduler scheduler = createScheduler(res);
        List<String> queues = res.getList("queue");
        List<String> identifiers = res.getList("identifier");

        if (identifiers == null) {
            if (queues == null) {
                queues = new ArrayList<>();
            }
            identifiers = Arrays.asList(scheduler.getJobs(queues.toArray(new String[0])));
        }

        List<JobStatus> statuses = Arrays.asList(scheduler.getJobStatuses(identifiers.toArray(new String[0])));

        scheduler.close();

        return new ListJobsOutput(statuses);
    }
}
