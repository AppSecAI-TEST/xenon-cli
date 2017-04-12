package nl.esciencecenter.xenon.cli.listfiles;

import static nl.esciencecenter.xenon.util.Utils.walkFileTree;

import nl.esciencecenter.xenon.Xenon;
import nl.esciencecenter.xenon.XenonException;
import nl.esciencecenter.xenon.cli.XenonCommand;
import nl.esciencecenter.xenon.credentials.Credential;
import nl.esciencecenter.xenon.files.FileAttributes;
import nl.esciencecenter.xenon.files.FileSystem;
import nl.esciencecenter.xenon.files.Files;
import nl.esciencecenter.xenon.files.Path;
import nl.esciencecenter.xenon.files.RelativePath;

import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Command to list objects at path of location
 */
public class ListFilesCommand extends XenonCommand {

    private ListFilesOutput listObjects(Files files, String scheme, String location, String pathIn, Credential credential, Boolean recursive, Boolean hidden) throws XenonException {
        FileSystem fs = files.newFileSystem(scheme, location, credential, null);

        RelativePath relPath = new RelativePath(pathIn);
        Path path = files.newPath(fs, relPath);
        FileAttributes att = files.getAttributes(path);
        int depth = 1;
        if (recursive) {
            depth = Integer.MAX_VALUE;
        }
        ListFilesOutput listing;
        if (att.isDirectory()) {
            ListFilesVisitor visitor = new ListFilesVisitor(path, hidden);
            walkFileTree(files, path, true, depth, visitor);
            listing = visitor.getListing();
        } else {
            listing = new ListFilesOutput();
            listing.addFile(relPath.getFileNameAsString());
        }
        files.close(fs);
        return listing;
    }

    public ListFilesOutput run(Namespace res, Xenon xenon) throws XenonException {
        String scheme = res.getString("scheme");
        String location = res.getString("location");
        String path = res.getString("path");
        Boolean recursive = res.getBoolean("recursive");
        Boolean hidden = res.getBoolean("hidden");
        Files files = xenon.files();
        Credential credential = buildCredential(res, xenon);
        return listObjects(files, scheme, location, path, credential, recursive, hidden);
    }
}