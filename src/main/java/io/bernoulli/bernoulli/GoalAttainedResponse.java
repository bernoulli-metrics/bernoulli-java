package io.bernoulli.bernoulli;
import java.util.Map;
import com.google.api.client.util.Key;

public class GoalAttainedResponse {
    @Key("status")
    public String Status;

    @Key("value")
    public Map<String, Boolean> Value;

    @Key("message")
    public String Message;
}
