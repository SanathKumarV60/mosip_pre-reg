package net.mosip;

import java.io.IOException;
import java.io.Console;

import net.mosip.login.Login;
import net.mosip.applications.Display;
import net.mosip.delete.Delete;
import net.mosip.cancel.Cancel;
import net.mosip.register.demographic.runDemographicDetails;
import net.mosip.register.demographic.Retrieve;

public class App 
{
    public static void main( String[] args ) throws IOException {
        new Login();
        Login.loginProc();
        
        menu();
    }

    public static void menu() throws IOException{
        while(true) {
            Console console = System.console();
            int choice = -1;
            if (console != null) {
                System.out.println("""
                    MENU
                    -----
                    1. View all open applications
                    2. Cancel Booking
                    3. Delete Application
                    4. New Application
                    5. View applications with pending appointments
                    6. View applications with pending document uploads
                    7. View particular application details (Use this to Book or Upload Documents)
                    0. Exit""");
                choice = Integer.parseInt(console.readLine("Please enter a choice:\s"));
                System.out.println("------------------------------");
            } else {
                System.err.println("ERROR: Could not take in choice!");
            }

            if (choice == 0){
                System.out.println("EXITING CONSOLE!");
                System.out.println("------------------------------");
                break;
            }

            switch(choice){
                case 1:
                    new Display();
                    Display.display_applications();
                    break;
                case 2:
                    new Cancel();
                    Cancel.cancel_application();
                    break;
                case 3:
                    new Delete();
                    Delete.delete_application();
                    break;
                case 4:
                    new runDemographicDetails();
                    runDemographicDetails.fill();
                    break;
                case 5:
                    new Display();
                    Display.display_not_booked();
                    break;
                case 6:
                    new Display();
                    Display.display_no_doc();
                    break;
                case 7:
                    new Display();
                    String resp = Display.display_applications();
                    if (resp == "") {
                        System.err.println("MESSAGE: No records found for the given user id");
                        System.out.println("------------------------------");
                        break;
                    }
                    String applicationId = console.readLine("Which application would you like to retrieve the details of?: ");
                    System.out.println("------------------------------");
                    envManager.updateEnv("applicationId", applicationId);
                    new Retrieve();
                    Retrieve.retrieve();
                    break;
                default:
                    System.err.println("ERROR: Please Enter a Valid Choice!");
                    System.out.println("------------------------------");
            }
        }
    }
}
