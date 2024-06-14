package net.mosip.register.confirmation;

import java.io.IOException;

import net.mosip.envManager;

public class RunCofirmation {
    public static void main(String[] args) throws IOException {
        getConfirmation(envManager.getEnv("applicationId"));
    }

    public static void getConfirmation(String applicationId) throws IOException {
        GenerateQR.generate(applicationId);
        OnScreenAck.getAck();
    }
}
