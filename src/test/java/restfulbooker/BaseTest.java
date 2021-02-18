package restfulbooker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

  protected RequestSpecification requestSpecification;

  @BeforeMethod
  public void setUp() {
    requestSpecification = new RequestSpecBuilder()
        .setBaseUri("https://restful-booker.herokuapp.com").build();
  }

  protected Response createBooking() {
    JSONObject jsonObject = new JSONObject();
    JSONObject bookingDates = new JSONObject();

    jsonObject.put("firstname", "Alex");
    jsonObject.put("lastname", "Brown");
    jsonObject.put("totalprice", 111);
    jsonObject.put("depositpaid", true);

    bookingDates.put("checkin", "2018-01-01");
    bookingDates.put("checkout", "2019-01-01");

    jsonObject.put("bookingdates", bookingDates);
    jsonObject.put("additionalneeds", "Breakfast");

    return RestAssured.given(requestSpecification).contentType(ContentType.JSON).body(jsonObject.toString())
        .post("/booking");
  }
}
