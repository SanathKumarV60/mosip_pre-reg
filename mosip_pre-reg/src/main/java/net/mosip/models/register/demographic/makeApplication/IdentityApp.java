package net.mosip.models.register.demographic.makeApplication;

public class IdentityApp {
    public double IDSchemaVersion;
    public FullNameApp[] fullName;
    public String dateOfBirth;
    public GenderApp[] gender;
    public ResidenceApp[] residenceStatus;
    public AddressVal[] addressLine1;
    public RegionVal[] region;
    public ProvinceVal[] province;
    public CityVal[] city;
    public ZoneVal[] zone;
    public String postalCode;
    public String phone;
    public String email;
}
