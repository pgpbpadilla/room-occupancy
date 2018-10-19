package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OccupancyController {

    @Autowired
    OccupancyService occupancyService;

    public OccupancyController(OccupancyService occupancyService) {
        this.occupancyService = occupancyService;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ImmutableMap<String, ImmutableMap<String, Object>> getSummary(
        @RequestBody() Map<String, Object> payload
    ) {
        int premiumCount = (int)payload.get("premiumCount");
        int economyCount = (int)payload.get("economyCount");
        List<Double> guests = (List<Double>) payload.get("guests");
        return occupancyService.calculateSummary(
            premiumCount,
            economyCount,
            guests
        );
    }
}
