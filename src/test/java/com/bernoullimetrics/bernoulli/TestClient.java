package com.bernoullimetrics.bernoulli;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class TestClient {
    @Test
    public void testClientThrowsWithoutClientID() {
        List<String> experimentIds = new ArrayList<String>();
        experimentIds.add("1");
        try {
            Client.GetExperiments(null, experimentIds, "user59", null);
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals(ex.getMessage(), "clientId");
            return;
        } catch (BernoulliException ex) {
            Assert.assertTrue(false); // should not get here
        } catch (IOException ex) {
            Assert.assertTrue(false);
        }

        // Should not get here
        Assert.assertTrue(false);
    }

    @Test
    public void testClientReturnsExperiments() {
        List<String> experimentIds = new ArrayList<String>();
        experimentIds.add("first");
        List<Experiment> experiments = null;
        try {
            experiments = Client.GetExperiments("1", experimentIds, "user59", null);
        } catch (BernoulliException ex) {
            Assert.assertTrue(false); // should not get here
        } catch (IOException ex) {
            Assert.assertTrue(false);
        }

        Assert.assertNotNull(experiments);
        Assert.assertEquals(1, experiments.size());
        Experiment experiment = (Experiment)experiments.get(0);
        Assert.assertEquals("First Experiment", experiment.Name);
        Assert.assertEquals("first", experiment.ID);
        Assert.assertEquals("", experiment.Segment);
        Assert.assertEquals("blue", experiment.Variant);
        Assert.assertEquals("", experiment.SegmentName);
        Assert.assertEquals("user59", experiment.UserID);
        Assert.assertEquals(1, experiment.Status);
    }

    @Test
    public void testClientGoalAttained() {
        boolean result = false;

        try {
            result = Client.GoalAttained("1", "first", "user59");
        } catch (BernoulliException ex) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(result);
    }
}
