# mosip_pre-reg
 The command line version of [MOSIP's pre-registration website](https://uat2.mosip.net/pre-registration-ui/#/eng).

# How to run
## METHOD 1:
 Clone the repository to any location on your PC an double-click the `mosip_pre-reg-run.bat` file.

## METHOD 2:
1. Open the location of the cloned repository on PC in terminal.
2. On the terminal type `cd "mosip_pre-reg/target"`.
3. On the terminal type `java -jar mosip_pre-reg-1.0.jar`.

# APIs used
## General
1. [**config.json**](https://uat2.mosip.net/pre-registration-ui/assets/config.json) - Get website url.
2. [**config**](https://uat2.mosip.net//preregistration/v1/login/config) - Get website configuration.
3. [**eng.json**](https://uat2.mosip.net/pre-registration-ui/assets/i18n/eng.json) - Get english text.

## Login
1. [**sendOtpWithCaptcha**](https://uat2.mosip.net//preregistration/v1/login/sendOtpWithCaptcha) - Post login request using captcha.
2. [**validateOtp**](https://uat2.mosip.net//preregistration/v1/login/validateOtp) - Post OTP from captcha and validate to authenticate user login.

## Open Applications
1. [**applications**](https://uat2.mosip.net//preregistration/v1/applications) - Get a list of all open applications for the given user.

## New Application
### Demographic Details
1. [**consent**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/templates/eng/consent) - Get consent text.
2. [**formPage0**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/dynamicfields?pageNumber=0&pageSize=10) - Get details of first page of form.
3. [**formPage1**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/dynamicfields?pageNumber=1&pageSize=10) - Get details of second page of form.
4. [**eng (Regions)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/MOR/eng) - Get a list of all available regions.
5. [**eng (Provinces)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/{region}/eng) - Get a list of all available provinces within a given region.
6. [**eng (Cities)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/{province}/eng) - Get a list of all available cities within a given province.
7. [**eng (Zones)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/{city}/eng) - Get a list of all available zones within a given city.
8. [**eng (Postal Codes)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/immediatechildren/{zone}/eng) - Get a list of all available postal codes within a given zone.

### Upload Document
1. [**{applicationId} (Details)**](https://uat2.mosip.net//preregistration/v1/applications/{applicationId}) - Get, Post, Push, Delete details of the given application ID.
2. [**{applicationId} (Documents)**](https://uat2.mosip.net//preregistration/v1/documents/preregistration/{{applicationId}}) - Get, Post, Push, Delete documents uploaded to given application ID.
3. [**getApplicantType**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/getApplicantType) - Post using application details to determine applicant type.
4. [**languages**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/applicanttype/{applicantType}/languages?languages=eng) - Get the different forms of identity proof applicable to the applicant type.

### Book Appointment
1. [**eng (Region Info)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/{region}/eng) - Get info on given region.
2. [**eng (Province Info)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/{province}/eng) - Get info on given province.
3. [**eng (City Info)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/{city}/eng) - Get info on given city.
4. [**eng (Zone Info)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/{zone}/eng) - Get info on given zone.
5. [**eng (Postal Code Info)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/info/{pincode}/eng) - Get info on given postal code.
6. [**eng (Location Info)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/locations/eng) - Get all location masterdata.
7. [**names**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/eng/5/names?name={pincode}) - Gets a list of all available centers in a given postal code.
8. [**eng (Working Days)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/workingdays/{regCenterId}/eng) - Gets all the working days for a given registration center ID.
9. [**eng (Registration Center)**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/registrationcenters/{regCenterId}/eng) - Get all details of a given registration center ID.
10. [**{regCenterId}**](https://uat2.mosip.net//preregistration/v1/appointment/availability/{regCenterId}) - Get all available dates and time slots for a given registration center ID.
11. [**appointment**](https://uat2.mosip.net//preregistration/v1/appointment) - Post and finalise the appointment.

### Confirmation
1. [**{applicationId} (Appointment Details)**](https://uat2.mosip.net//preregistration/v1/appointment/{applicationId}) - Get appointment details of a given application ID.
2. [**generate**](https://uat2.mosip.net//preregistration/v1/qrCode/generate) - Post and generate a QR code for the given appointment and application.
3. [**Onscreen-Acknowledgement**](https://uat2.mosip.net//preregistration/v1//proxy/masterdata/templates/eng/Onscreen-Acknowledgement) - Get a pdf of the given appointment and application to use during the physical process.
4. [**notify**](https://uat2.mosip.net//preregistration/v1/notification/notify) - Post a notification to a given email id and/or phone number.
