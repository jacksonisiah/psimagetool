package me.jackson.kissa;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class USBDevice {
    public static ArrayList<String> getUSBDevices() {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        ArrayList<String> devices = new ArrayList<>();
        File[] d = File.listRoots();

        if (d != null && d.length > 0) {
            for (File drive : d) {
                // do not allow users to format their hard drives
                if (!fsv.getSystemTypeDescription(drive).equals("Local Disk"))
                    devices.add(drive.toString());
            }
        }

        return devices;
    }

    public static void format(String device) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(String.format("CMD /C format %s /FS:FAT32 /Q /X /Y", device.substring(0, device.length() - 1)));
        p.waitFor(1200000, TimeUnit.MILLISECONDS);
    }
}
