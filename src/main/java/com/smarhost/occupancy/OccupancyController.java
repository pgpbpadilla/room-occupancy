package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class OccupancyController {

    @Autowired
    OccupancyService os;

    public OccupancyController(OccupancyService os) {
        this.os = os;
    }

    @RequestMapping("/summary")
    public ImmutableMap<String, ImmutableMap<String, Object>> getSummary(
        @RequestParam("premium") int premiumCount,
        @RequestParam("economy") int economyCount
    ) {
        return os.calculateSummary(premiumCount, economyCount, new ArrayList<>());
    }
}
