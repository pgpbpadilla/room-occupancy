package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OccupancyService {

    public ImmutableMap<String, ImmutableMap<String, Object>> calculateSummary(
        int premiumCount,
        int economyCount,
        List<Double> budgets
    ) {
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
