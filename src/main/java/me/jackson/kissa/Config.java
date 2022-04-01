package me.jackson.kissa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final Properties settings;

    @SuppressWarnings("ResultOfMethodCallIgnored") // java doesn't have discards? why?
    public Config() throws IOException {
        var defaults = new Properties();
        defaults.setProperty("ps5", "https://pc.ps5.update.playstation.net/update/ps5/official/tJMRE80IbXnE9YuG0jzTXgKEjIMoabr6/image/2022_0316/rec_028896220519726f78007ef3b9c7cd2e4df67f87babe533f61e908569220084f/PS5UPDATE.PUP");
        defaults.setProperty("ps4", "https://pc.ps4.update.playstation.net/update/ps4/image/2022_0307/rec_e3b319239ae0cd4e585db81e4e35dabc/PS4UPDATE.PUP");

        File f = new File("urls.properties");
        f.createNewFile();

        // load config
        settings = new Properties(defaults);
        var in = new FileInputStream(f);
        settings.load(in);
        in.close();

        //save config
        FileOutputStream out = new FileOutputStream("urls.properties");
        settings.store(out, null);
        out.close();
    }

    public String getPS5Download() {
        return settings.getProperty("ps5");
    }

    public String getPS4Download() {
        return settings.getProperty("ps4");
    }
}
