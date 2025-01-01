package TestSuite;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.util.Properties;
import static org.hamcrest.Matchers.*;

public class API_TestAutomation {

    // Properties object to hold configuration values
    private Properties properties;

    // This method runs before any tests to load properties from a configuration file
    @BeforeClass
    public void setUp() {
        properties = new Properties();
        try {
            // Load the configuration file that contains API base URI and other parameters
            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method runs after all tests are completed, serving as a placeholder for any cleanup logic
    @AfterClass
    public void tearDown() {
        // Placeholder for cleanup logic (e.g., closing connections, resetting states)
    }

    // Test method to verify that random cat facts can be retrieved via the API
    @Test
    public void verifyRandomCatFact() {
        // Set the base URI for the API using the value from properties file
        RestAssured.baseURI = properties.getProperty("catFactURL");

        // Send a GET request to the API with query parameters for animal type and number of facts
        Response response = RestAssured.given()
                .queryParam("animal_type", properties.getProperty("animalType"))  // Set animal type
                .queryParam("amount", Integer.parseInt(properties.getProperty("numberOfFacts")))  // Set the number of facts
                .when()
                .get("/facts/random");  // Request random facts

        // Log the API response body in the TestNG report for visibility
        Reporter.log("API Response Body: " + response.asString(), true);

        // Perform validations to ensure the API response is correct
        response.then()
                .statusCode(200)  // Ensure the HTTP status code is 200 (OK)
                .body("[0].text", not(emptyString()));  // Ensure the "text" field in the response is not empty
    }
}
