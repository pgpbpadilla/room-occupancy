package com.smarhost.occupancy;

import com.google.common.collect.ImmutableMap;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@WebAppConfiguration
public class OccupancyControllerTest {
    private OccupancyController controller;

    @BeforeMethod
    public void setUp() {
        OccupancyService os = new OccupancyService();
        controller = new OccupancyController(os);
    }

    @Test
    public void testGetSummary() {
        ImmutableMap<String, ImmutableMap<String, Object>> response =
            controller.getSummary(3, 3);

        Assert.assertNotNull(response);
        ImmutableMap<String, Object> premium = response.get("premium");
        Assert.assertEquals(premium.get("usage"), 3);
        Assert.assertEquals(premium.get("earnings"), new Double(738));
        ImmutableMap<String, Object> economy = response.get("economy");
        Assert.assertEquals(economy.get("usage"), 3);
        Assert.assertEquals(economy.get("earnings"), new Double(167));
    }
}