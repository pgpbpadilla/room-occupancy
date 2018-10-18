package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@WebAppConfiguration
public class OccupancyControllerTest {

    private OccupancyController controller;

    @BeforeMethod
    public void setUp() {
        controller = new OccupancyController();
    }

    @DataProvider(name = "summaryData")
    public Object[][] summaryData() {
        return new Object[][] {
            {
                new Integer(3),
                new Integer(3),
                new Integer(3),
                new Integer(3),
                new Double(738.00),
                new Double(167.00)
            },
//            { new Integer(7), new Integer(5) },
//            { new Integer(2), new Integer(7) },
//            { new Integer(7), new Integer(1) }
        };
    }

    @Test(dataProvider = "summaryData")
    public void testGetSummary(
        int premiumCount,
        int economyCount,
        int expectedPremiumUsage,
        int expectedEconomyUsage,
        double expectedEarningsPremium,
        double expectedEarningsEconomy
    ) {
        ImmutableMap<String, ImmutableMap<String, Object>> response =
            controller.getSummary(premiumCount, economyCount);

        ImmutableMap<String, Object> premium = response.get("premium");
        Assert.assertEquals(premium.get("usage"), expectedPremiumUsage);
        Assert.assertEquals(premium.get("earnings"), expectedEarningsPremium);
        ImmutableMap<String, Object> economy = response.get("economy");
        Assert.assertEquals(economy.get("usage"), expectedEconomyUsage);
        Assert.assertEquals(economy.get("earnings"), expectedEarningsEconomy);
    }
}