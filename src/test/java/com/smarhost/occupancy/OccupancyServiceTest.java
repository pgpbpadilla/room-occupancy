package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class OccupancyServiceTest {

    private OccupancyService occupancyService;

    @BeforeMethod
    public void setUp(){
        occupancyService = new OccupancyService();
    }

    @DataProvider(name = "summaryData")
    public Object[][] summaryData() {
        return new Object[][]{
            {
                new Integer(3), new Integer(3),
                new Integer(3), new Double(738),
                new Integer(3), new Double(167)
            },
            {
                new Integer(7), new Integer(5),
                new Integer(6), new Double(1054),
                new Integer(4), new Double(189)
            },
            {
                new Integer(2), new Integer(7),
                new Integer(2), new Double(583),
                new Integer(4), new Double(189)
            },
            {
                new Integer(7), new Integer(1),
                new Integer(7), new Double(1153),
                new Integer(1), new Double(45)
            }
        };
    }

    @Test(dataProvider = "summaryData")
    public void testCalculateSummary(
        int premiumCount, int economyCount,
        int expectedPremiumUsage, double expectedEarningsPremium,
        int expectedEconomyUsage, double expectedEarningsEconomy
    ) {
        DataUtils dataUtils = new DataUtils();
        List guestBudgets = dataUtils.loadGuestBudgets();

        ImmutableMap<String, ImmutableMap<String, Object>> summary =
            occupancyService.calculateSummary(
                premiumCount,
                economyCount,
                guestBudgets
            );

        ImmutableMap<String, Object> premium = summary.get("premium");
        Assert.assertEquals(premium.get("usage"), expectedPremiumUsage);
        Assert.assertEquals(premium.get("earnings"), expectedEarningsPremium);

        ImmutableMap<String, Object> economy = summary.get("economy");
        Assert.assertEquals(economy.get("usage"), expectedEconomyUsage);
        Assert.assertEquals(economy.get("earnings"), expectedEarningsEconomy);
    }
}