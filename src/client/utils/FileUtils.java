package client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * THIS IS FROM THE SWAGGER PDF!
 *
 * Created by Sierra on 11/10/16.
 */
public class FileUtils {

    private static final Map<String, String> MIMETYPES = makeMimeTypes();

    private static Map<String, String> makeMimeTypes() {
        Map<String, String> mimeTypes = new HashMap<String, String>();
        mimeTypes.put(".js", "application/javascript");
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".html", "text/html");
        mimeTypes.put(".json", "application/json");
        return mimeTypes;
    }

    public static byte[] readFile(String path) throws IOException {
        // Make a file object from the path name
        File file = new File(path);
        // Find the size
        int size = (int) file.length();
        // Create a buffer big enough to hold the file
        byte[] contents = new byte[size];
        // Create an input stream from the file object
        FileInputStream in = new FileInputStream(file);
        // Read it all
        in.read(contents);
        // Close the file
        in.close();

        return contents;
    }

    public static String getMimeType(String path){
        String ending = path.substring(path.lastIndexOf('.'),path.length());

        if (MIMETYPES.get(ending) != null) {
            return MIMETYPES.get(ending);
        }
        else {
            return "";
        }
    }
}
