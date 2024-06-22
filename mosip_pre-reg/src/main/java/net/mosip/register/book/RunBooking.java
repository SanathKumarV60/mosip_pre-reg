package net.mosip.register.book;

import java.io.IOException;

import net.mosip.envManager;
import net.mosip.register.confirmation.RunCofirmation;

public class RunBooking {
    public static void main(String[] args) throws IOException {
        runBooking(envManager.getEnv("applicationId"));
    }

    public static void runBooking(String applicationId) throws IOException {
        MasterData.main(null);
        Centers.getCenters();
        TimeSlots.getSlots();
        Appointment.getAppointment(applicationId);
        RunCofirmation.getConfirmation(applicationId);
    }
}
