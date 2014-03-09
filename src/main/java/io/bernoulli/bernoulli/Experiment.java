package io.bernoulli.bernoulli;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Experiment extends GenericJson {

    @Key("id")
    public String ID;

    @Key("name")
    public String Name;

    @Key("user_id")
    public String UserID;

    @Key("variant")
    public String Variant;

    @Key("status")
    public int Status; // FIXME

    @Key("segmentName")
    public String SegmentName;

    @Key("segment")
    public String Segment;

}
