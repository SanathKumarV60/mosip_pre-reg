package net.mosip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class envManager {

    private static final String ENV_FILE_PATH = "../src/main/java/net/mosip/.env";

    public static void main(String[] args) {
        try {
            // Read existing environment variables
            Map<String, String> existingVars = readEnvFile();

            // Update a specific variable
            String varToUpdate = "dob";
            String newValue = "YYYY/MM/DD";
            updateEnv(varToUpdate, newValue);

            System.out.println("Updated environment variables:");
            existingVars = readEnvFile();  // Re-read the file to get updated values
            for (String var : existingVars.keySet()) {
                System.out.println(var + "=" + existingVars.get(var));
            }

            // Retrieve the updated value
            String updatedValue = getEnv(varToUpdate);
            System.out.println("\nRetrieved value for '" + varToUpdate + "': " + updatedValue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(System.getProperty("user.dir"));
    }

    // Read environment variables from .env file
    public static Map<String, String> readEnvFile() throws IOException {
        Map<String, String> vars = new HashMap<>();
        File envFile = new File(ENV_FILE_PATH);
		if (!envFile.exists()) {
            if (envFile.createNewFile()) {
                //System.out.println(".env file created successfully.");
            } else {
                //System.err.println("Error creating .env file.");
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(ENV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length >= 1) {
                    String key = parts[0].trim();
                    String value = parts.length > 1 ? parts[1].trim() : "";
                    vars.put(key, value);
                } else {
                    System.err.println("Invalid line format in .env file: " + line);
                }
            }
        }
        return vars;
    }

    // Update a specific environment variable
    public static void updateEnv(String varName, String newValue) throws IOException {
        Map<String, String> varMap = readEnvFile();
        varMap.put(varName, newValue);  // Update in memory

        // Write changes back to the file
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ENV_FILE_PATH))) {
            for (Map.Entry<String, String> entry : varMap.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
        //System.out.println("Variable '" + varName + "' updated successfully.");
    }

    // Retrieve the value of an environment variable
    public static String getEnv(String varName) throws IOException {
        Map<String, String> varMap = readEnvFile();
        return varMap.get(varName);
    }
}
