package io.bernoulli.bernoulli;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import io.bernoulli.bernoulli.Client;

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
        HttpTransport mockTransport = getMockTransport("{\"status\": \"ok\", \"value\": [{\"status\": 1, \"user_id\": \"user59\", \"segmentName\": null, \"variant\": \"blue\", \"segment\": null, \"id\": \"first\", \"name\": \"First Experiment\"}]}");
        List<String> experimentIds = new ArrayList<String>();
        experimentIds.add("first");
        List<Experiment> experiments = null;
        try {
            experiments = Client.GetExperiments("1", experimentIds, "user59", null, mockTransport);
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
        HttpTransport mockTransport = getMockTransport("{\"status\": \"ok\", \"value\": {\"success\": true}}");

        boolean result = false;

        try {
            result = Client.GoalAttained("1", "first", "user59", mockTransport);
        } catch (BernoulliException ex) {
            Assert.assertTrue(false);
        }

        Assert.assertTrue(result);
    }

    private HttpTransport getMockTransport(final String responseString) {
        return new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.addHeader("custom_header", "value");
                        response.setStatusCode(200);
                        response.setContentType(Json.MEDIA_TYPE);
                        response.setContent(responseString);
                        return response;
                    }
                };
            }
        };
    }
}
