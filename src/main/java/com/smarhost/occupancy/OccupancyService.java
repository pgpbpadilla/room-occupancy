package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class OccupancyService {

    public ImmutableMap<String, ImmutableMap<String, Object>> calculateSummary(
        int premiumRoomCount,
        int economyRoomCount,
        List<Double> customerBudgets
    ) {
        Map<String, List<Double>> customerByCategory = customerBudgets
            .stream()
            .collect(groupingBy(
                (Double o) -> Customer.getCategory(o),
                collectingAndThen(toList(), budgets -> sortByPriority(budgets))
            ));

        List<Double> premiumCustomers = customerByCategory.get("premium");
        boolean emptyPremiumRooms = premiumCustomers.size() < premiumRoomCount;
        Integer premiumUsage =
            emptyPremiumRooms ? premiumCustomers.size() : premiumRoomCount;
        Double premiumEarnings = premiumCustomers.stream()
            .limit(premiumUsage)
            .mapToDouble(Double::doubleValue)
            .sum();

        List<Double> economyCustomers = customerByCategory.get("economy");
        List<Double> upgradedCustomers = Collections.emptyList();
        if (economyCustomers.size() > economyRoomCount) {
            if (emptyPremiumRooms) {
                upgradedCustomers = economyCustomers.stream()
                    .limit(premiumRoomCount - premiumUsage)
                    .collect(toList());
                premiumUsage += upgradedCustomers.size();
                premiumEarnings += upgradedCustomers.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
            }
        }
        boolean emptyEconomyRooms = economyCustomers.size() < economyRoomCount;
        Integer economyUsage =
            emptyEconomyRooms ? economyCustomers.size(): economyRoomCount;
        Double economyEarnings = economyCustomers.stream()
            .skip(upgradedCustomers.size())
            .limit(economyUsage)
            .mapToDouble(Double::doubleValue)
            .sum();

        ImmutableMap<String, Object> premiumSummary = ImmutableMap.of(
            "usage", premiumUsage,
            "earnings", premiumEarnings
        );
        ImmutableMap<String, Object> economySummary = ImmutableMap.of(
            "usage", economyUsage,
            "earnings", economyEarnings
        );
        return new ImmutableMap.Builder<String, ImmutableMap<String, Object>>()
            .put("premium", premiumSummary)
            .put("economy", economySummary)
            .build();
    }

    private List<Double> sortByPriority(List<Double> l) {
        l.sort(Comparator.reverseOrder());
        return l;
    }
}
