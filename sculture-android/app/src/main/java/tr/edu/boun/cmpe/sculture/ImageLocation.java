package tr.edu.boun.cmpe.sculture;

import android.net.Uri;

/**
 * A class which represents local or remote image paths
 */
public class ImageLocation {
    private boolean isLocal;
    private String id;
    private Uri uri;

    /**
     * Creates a local image path
     *
     * @param uri Uri of image
     */
    public ImageLocation(Uri uri) {
        this.uri = uri;
        isLocal = true;
    }

    /**
     * Creates a remote image path
     *
     * @param ID ID of the image on the server
     */
    public ImageLocation(String ID) {
        this.id = ID;
        isLocal = false;
    }

    /**
     * Gets uri of a local file
     *
     * @return Uri
     */
    public Uri getUri() {
        return uri;
    }

    /**
     * Gets id of a remote file
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Whether stored path is local or remote
     *
     * @return True:local, False:remote
     */
    public boolean isLocal() {
        return isLocal;
    }
}