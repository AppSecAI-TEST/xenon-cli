package nl.esciencecenter.xenon.cli.copy;

/**
 * Results of a copy command
 */
public class CopyOutput {
    private final CopyInput target;
    private final CopyInput source;
    private final long bytesCopied;

    public CopyOutput(CopyInput source, CopyInput target, long bytesCopied) {
        this.source = source;
        this.target = target;
        this.bytesCopied = bytesCopied;
    }

    @Override
    public String toString() {
        String sourceLocation = source.getLocation();
        if (source.isLocal() && sourceLocation == null) {
            sourceLocation = "local";
        }
        String targetLocation = target.getLocation();
        if (target.isLocal() && targetLocation == null) {
            targetLocation = "local";
        }
        return String.format("Copied %i bytes %s' from location '%s' to '%s' to location '%s'", bytesCopied, source.getPath(), sourceLocation, target.getPath(), targetLocation);
    }
}
