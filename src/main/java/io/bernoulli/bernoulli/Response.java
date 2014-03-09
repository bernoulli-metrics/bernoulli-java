package io.bernoulli.bernoulli;

import com.google.api.client.util.Key;

import java.util.List;

public class Response {

    @Key("status")
    public String Status;

    @Key("value")
    private List<Experiment> value;

    public List<Experiment> getExperiments() {
        return value;
    }

    @Key("message")
    public String Message;
}
