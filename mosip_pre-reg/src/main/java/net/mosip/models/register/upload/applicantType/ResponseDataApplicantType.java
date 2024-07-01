package net.mosip.models.register.upload.applicantType;

public class ResponseDataApplicantType {
    public String id;
    public String version;
    public String responsetime;
    public MetadataApplicantType metadata;
    public ResponseDetailsApplicantType response;
    public ErrorsApplicantType[] errors; 
}
