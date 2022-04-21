package me.jackson.kissa;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.Map;

public class Download implements Runnable {
    private String message = "";
    private Map<String, String> conData;
    private final File cacheDir;

    public Download(File dir, Map<String, String> console) {
        conData = console;
        cacheDir = dir;
    }

    @Override
    public void run() {
        try {
            // this method avoids loading 1+gb files into memory
            var url = new URL(conData.get("downloadUrl"));
            var rbc = Channels.newChannel(url.openStream());
            var fos = new FileOutputStream(cacheDir + "/" + conData.get("updateFile"));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception e) {
            message = "An error occurred while downloading file: \n" + e.getMessage();
        }
    }

    public String getMessage() {
        return message;
    }
}
