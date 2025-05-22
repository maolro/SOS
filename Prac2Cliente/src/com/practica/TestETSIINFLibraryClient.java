package com.practica;
import com.practica.ETSIINFLibraryStub.*;

public class TestETSIINFLibraryClient {

    public static void main(String[] args) {
        try {
            // Create the stub (auto-generated class)
            ETSIINFLibraryStub stub = new ETSIINFLibraryStub("http://localhost:8080/axis2/services/ETSIINFLibrary");

            // Build a Login request
            Login loginRequest = new Login();
            User user = new User();
            user.setName("admin");
            user.setPwd("admin");
            loginRequest.setArgs0(user);

            // Call the web service
            LoginResponse response = stub.login(loginRequest);

            // Get and print the result
            Response result = response.get_return();
            System.out.println("Login success: " + result.getResponse());

        } catch (Exception e) {
            System.err.println("Error calling ETSIINFLibrary service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
