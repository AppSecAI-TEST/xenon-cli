package nl.esciencecenter.xenon.cli.removefile;

import nl.esciencecenter.xenon.cli.IParser;

import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class RemoveFileParser implements IParser {
    @Override
    public Subparser buildArgumentParser(Subparsers subparsers) {
        Subparser subparser = subparsers.addParser("remove")
            .setDefault("command", new RemoveFileCommand())
            .help("Remove path at location")
            .description("Remove path at location");
        subparser.addArgument("path").help("Path").required(true);
        return subparser;
    }

}
