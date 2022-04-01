package me.jackson.kissa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PlayStation {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String createMedia(String device, String console) {
        // create config object and retrieve download url.
        Config c;
        try {
            c = new Config();
        } catch (IOException e) {
            // probably a case of a weird windows filesystem configuration.
            // todo: implement loading from defaults as a fallback.
            return "An error occurred while loading configuration: \n" + e.getMessage();
        }

        String downloadUrl;
        String updateFile;
        String shortname;
        if (console.equals("PlayStation 5")) { // when would this ever be null?
            downloadUrl = c.getPS5Download();
            updateFile = "PS5UPDATE.PUP";
            shortname = "PS5";
        }
        else {
            downloadUrl = c.getPS4Download();
            updateFile = "PS4UPDATE.PUP";
            shortname = "PS4";
        }

        // format drive to fat32
        try {
            USBDevice.format(device);
        } catch (Exception e) {
            return "An error occurred while formatting device: \n" + e.getMessage();
        }

        // create cache for later use so we are not waiting for a download always
        var cacheDir = new File(System.getProperty("user.dir") + "/cache");
        cacheDir.mkdir(); // creates directory if directory does not exist.

        var media = new File(cacheDir + "/" + updateFile);

        if (!media.exists()) {
            // run on separate thread
            var dt = new Download(downloadUrl, cacheDir, updateFile);
            dt.run();

            if (!dt.getMessage().isEmpty())
                return dt.getMessage();
        }

        // create installation media.
        try {
            var f = new File(device + "/" + shortname + "/UPDATE");
            f.mkdirs();
            Files.copy(Paths.get(media.getAbsolutePath()), Paths.get(f.getAbsolutePath() + "/" + updateFile), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return "An error occurred while creating media: \n" + e.getMessage();
        }

        return "Media created successfully.";
    }
}
