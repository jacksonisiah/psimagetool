package me.jackson.kissa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class PlayStation {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String createMedia(String device, String console) {
        Config c;
        try {
            c = new Config();
        } catch (IOException e) {
            // probably a case of a weird windows filesystem configuration.
            // todo: implement loading from defaults as a fallback.
            return "An error occurred while loading configuration: \n" + e.getMessage();
        }

        var conData = getConsole(c, console);

        // format drive to fat32
        try {
            USBDevice.format(device);
        } catch (Exception e) {
            return "An error occurred while formatting device: \n" + e.getMessage();
        }

        // create cache for later use, so we are not waiting for a download always
        var cacheDir = new File(System.getProperty("user.dir") + "/cache");
        cacheDir.mkdir(); // creates directory if directory does not exist.

        var media = new File(cacheDir + "/" + conData.get("updateFile"));

        if (!media.exists()) {
            // run on separate thread
            var dt = new Download(cacheDir, conData);
            dt.run();

            if (!dt.getMessage().isEmpty())
                return dt.getMessage();
        }

        // create installation media.
        try {
            var f = new File(device + "/" + conData.get("shortname") + "/UPDATE");
            f.mkdirs();
            Files.copy(Paths.get(media.getAbsolutePath()), Paths.get(f.getAbsolutePath() + "/" + conData.get("updateFile")), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return "An error occurred while creating media: \n" + e.getMessage();
        }

        return "Media created successfully.";
    }

    public static Map<String, String> getConsole(Config c, String console) {
        HashMap<String, String> conData = new HashMap<>();
        switch (console) {
            case "PlayStation 5" -> {
                conData.put("downloadUrl", c.getPS5Download());
                conData.put("updateFile", "PS5UPDATE.PUP");
                conData.put("shortname", "PS5");
            }
            default -> {
                conData.put("downloadUrl", c.getPS4Download());
                conData.put("updateFile", "PS4UPDATE.PUP");
                conData.put("shortname", "PS4");
            }
        }
        return conData;
    }
}
