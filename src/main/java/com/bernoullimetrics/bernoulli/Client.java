package com.bernoullimetrics.bernoulli;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


public class Client {
    private static final String URL = "https://bernoulli.herokuapp.com/client/api/experiments/";
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static final String SCHEME = "http";
    private static final String PATH = "/client/api/experiments/";

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static List<Experiment> GetExperiments(String clientId, List<String> experimentIds, String userId, Map<String, String> userData) throws BernoulliException, IllegalArgumentException, IOException {
    {
        return GetExperiments(clientId, experimentIds, userId, userData, null);
    }
    }

    public static List<Experiment> GetExperiments(String clientId, List<String> experimentIds, String userId, Map<String, String> userData, HttpTransport transport) throws BernoulliException, IllegalArgumentException, IOException {
        if (clientId == null || clientId.equals("")) {
            throw new IllegalArgumentException("clientId");
        }

        Map<String, String> query = new HashMap<String, String>();
        query.put("clientId", clientId);
        query.put("userId", userId);
        query.put("experimentIds", Join(experimentIds, ","));

        if (userData != null && userData.size() > 0) {
            for (Map.Entry<String, String> entry : userData.entrySet()) {
                query.put(entry.getKey(), entry.getValue());
            }
        }

        GenericUrl url = new GenericUrl();
        url.setHost(HOST);
        url.setPort(PORT);
        url.setScheme(SCHEME);
        url.setRawPath(PATH);

        for (Map.Entry<String, String> entry : query.entrySet()) {
            url.set(entry.getKey(), entry.getValue());
        }

        if (transport == null) {
            transport = HTTP_TRANSPORT;
        }

        Response response = new Response();
        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });
        try {
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            response = httpResponse.parseAs(Response.class);
        } catch (IOException ex) {
            throw new BernoulliException("Unable to get experiments");
        }

        if (response.Status.equals("ok")) {
            return response.getExperiments();
        }

        throw new BernoulliException(response.Message);
    }

    public static boolean GoalAttained(String clientId, String experimentId, String userId) throws BernoulliException
    {
        return GoalAttained(clientId, experimentId, userId, null);
    }

    public static boolean GoalAttained(String clientId, String experimentId, String userId, HttpTransport transport) throws BernoulliException
    {
        if (clientId == null || clientId.equals("")) {
            throw new IllegalArgumentException("clientId");
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("clientId", clientId);
        data.put("userId", userId);
        data.put("experimentId", experimentId);

        GenericUrl url = new GenericUrl();
        url.setHost(HOST);
        url.setPort(PORT);
        url.setScheme(SCHEME);
        url.setRawPath(PATH);

        if (transport == null) {
            transport = HTTP_TRANSPORT;
        }

        GoalAttainedResponse response = new GoalAttainedResponse();
        HttpRequestFactory requestFactory =
                transport.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        try {
            HttpContent content = new UrlEncodedContent(data);
            HttpRequest request = requestFactory.buildPostRequest(url, content);
            HttpResponse httpResponse = request.execute();
            response = httpResponse.parseAs(GoalAttainedResponse.class);
        } catch (IOException ex) {
            throw new BernoulliException("Unable to contact bernoulli");
        }

        return response.Value.get("success");
    }

    private static String Join(List<String> strings, String sep) {
        String ret = "";
        for (int i = 0; i < strings.size(); i++) {
            ret += strings.get(i);
            if (i != strings.size()-1) {
                ret += sep;
            }
        }

        return ret;
    }
}
