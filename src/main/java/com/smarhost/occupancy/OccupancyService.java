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
        List<Double> guests
    ) {
        Map<String, List<Double>> guestsByCategory = groupByCategory(guests);

        List<Double> premiumGuests = guestsByCategory.get("premium");
        Integer premiumUsage = getUsage(premiumRoomCount, premiumGuests);
        Double premiumEarnings = getSum(premiumGuests, premiumUsage);

        List<Double> economyGuests = guestsByCategory.get("economy");
        List<Double> upgradedGuests = Collections.emptyList();

        if (economyGuests.size() > economyRoomCount) {
            if (premiumUsage < premiumRoomCount) {
                int emptyPremiumRoomCount = premiumRoomCount - premiumUsage;
                upgradedGuests = economyGuests.stream()
                    .limit(emptyPremiumRoomCount)
                    .collect(toList());
            }
            premiumUsage += upgradedGuests.size();
            premiumEarnings += getSum(upgradedGuests);
        }

        Integer economyUsage = getUsage(economyRoomCount, economyGuests);
        Double economyEarnings =
            getSum(economyGuests, upgradedGuests.size(), economyUsage);

        return new ImmutableMap.Builder<String, ImmutableMap<String, Object>>()
            .put("premium", makeSummary(premiumUsage, premiumEarnings))
            .put("economy", makeSummary(economyUsage, economyEarnings))
            .build();
    }

    private double getSum(List<Double> aList) {
        return getSum(aList, 0, aList.size());
    }

    private double getSum(List<Double> aList, Integer limit) {
        return getSum(aList, 0, limit);
    }

    private double getSum(List<Double> aList, int skip, Integer limit) {
        return aList.stream()
            .skip(skip)
            .limit(limit)
            .mapToDouble(Double::doubleValue)
            .sum();
    }

    private int getUsage(int roomCount, List<Double> guests) {
        boolean emptyRooms = guests.size() < roomCount;
        return emptyRooms ? guests.size() : roomCount;
    }

    private Map<String, List<Double>> groupByCategory(List<Double> guestBudgets) {
        return guestBudgets
            .stream()
            .collect(groupingBy(
                (Double budget) -> Guest.getCategory(budget),
                collectingAndThen(toList(), budgets -> sortByPriority(budgets))
            ));
    }

    private ImmutableMap<String, Object> makeSummary(
        Integer usage, Double earnings
    ) {
        return ImmutableMap.of(
            "usage", usage,
            "earnings", earnings
        );
    }

    private List<Double> sortByPriority(List<Double> l) {
        l.sort(Comparator.reverseOrder());
        return l;
    }
}
