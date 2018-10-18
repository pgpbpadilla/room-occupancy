package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class OccupancyController {
    @RequestMapping("/summary")
    public ImmutableMap<String, ImmutableMap<String, Object>> getSummary(
        @RequestParam("premium") int premiumCount,
        @RequestParam("economy") int economyCount
    ) {
        ClassLoader classLoader = getClass().getClassLoader();
        File guestsFile =
            new File(classLoader.getResource("json/guests.json").getFile());

        List guestBudgets = null;

        try {
            JsonParser parser = new BasicJsonParser();
            String content =
                new String(Files.readAllBytes(Paths.get(guestsFile.toURI())));
            guestBudgets = parser.parseList(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImmutableMap<String, Object> premiumData = ImmutableMap.of(
            "usage", new Integer(3),
            "earnings", new Double(738)
        );
        ImmutableMap<String, Object> economyData = ImmutableMap.of(
            "usage", new Integer(3),
            "earnings", new Double(167)
        );
        return new ImmutableMap.Builder<String, ImmutableMap<String, Object>>()
            .put("premium", premiumData)
            .put("economy", economyData)
            .build();
    }
}
