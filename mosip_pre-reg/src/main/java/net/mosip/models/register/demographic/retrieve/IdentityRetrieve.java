package net.mosip.models.register.demographic.retrieve;

public class IdentityRetrieve {
    public GenderRetrieve[] gender;
    public CityRetrieve[] city;
    public String postalCode;
    public NameRetrieve[] fullName;
    public String dateOfBirth;
    public double IDSchemaVersion;
    public ProvinceRetrieve[] province;
    public ZoneRetrieve[] zone;
    public String phone;
    public AddressRetrieve[] addressLine1;
    public ResidenceRetrieve[] residenceStatus;
    public RegionRetrieve[] region;
    public String email;
}
