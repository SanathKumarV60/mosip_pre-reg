package net.mosip.models.register.confirmation.onScreenAck;

public class ResponseDataAck {
    public String id;
    public String version;
    public String responsetime;
    public String[] metadata;
    public ResponseDetailsAck response;
    public ErrorsAck[] errors;
}
