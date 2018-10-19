package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.*;

public class OccupancyServiceTest {

    private OccupancyService os;

    @BeforeMethod
    public void setUp(){
        os = new OccupancyService();
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
        List guestBudgets = loadGuestBudgets();

        ImmutableMap<String, ImmutableMap<String, Object>> summary =
            os.calculateSummary(premiumCount, economyCount, guestBudgets);

        ImmutableMap<String, Object> premium = summary.get("premium");
        Assert.assertEquals(premium.get("usage"), expectedPremiumUsage);
        Assert.assertEquals(premium.get("earnings"), expectedEarningsPremium);

        ImmutableMap<String, Object> economy = summary.get("economy");
        Assert.assertEquals(economy.get("usage"), expectedEconomyUsage);
        Assert.assertEquals(economy.get("earnings"), expectedEarningsEconomy);
    }

    private List loadGuestBudgets() {
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