package net.mosip.register.upload;

import java.io.IOException;

import net.mosip.envManager;

public class RunUpload {
    public static void main(String[] args) throws IOException {
        runUpload(envManager.getEnv("applicationId"));
    }

    public static void runUpload(String applicationId) throws IOException{
        ApplicantType.getType(applicationId);
        Choose.chooseUploadType();
    }
}

