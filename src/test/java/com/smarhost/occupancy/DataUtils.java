package com.smarhost.occupancy;

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataUtils {
    public List<Integer> loadGuestBudgets() {
        ClassLoader classLoader = getClass().getClassLoader();
        File guestsFile =
            new File(classLoader.getResource("json/guests.json").getFile());

        List budgets = null;

        try {
            JsonParser parser = new BasicJsonParser();
            String content =
                new String(Files.readAllBytes(Paths.get(guestsFile.toURI())));
            budgets = parser.parseList(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return budgets;
    }
}
