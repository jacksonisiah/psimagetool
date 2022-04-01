package me.jackson.kissa;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Download implements Runnable {
    private String message = "";
    private final String downloadUrl;
    private final File cacheDir;
    private final String updateFile;

    public Download(String url, File dir, String file) {
        downloadUrl = url;
        cacheDir = dir;
        updateFile = file;
    }

    @Override
    public void run() {
        try {
            // avoid buffering update files into memory if possible
            var url = new URL(downloadUrl);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(cacheDir + "/" + updateFile);
            FileChannel fc = fos.getChannel();
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            message = "An error occurred while downloading file: \n" + e.getMessage();
        }
    }

    public String getMessage() {
        return message;
    }
}
