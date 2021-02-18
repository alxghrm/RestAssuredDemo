package restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateBookingTest extends BaseTest {


  @Test
  public void updateBooking(){

    Response responseCreate = createBooking();
    responseCreate.print();

    int id = responseCreate.jsonPath().getInt("bookingid");

    JSONObject jsonObject = new JSONObject();
    JSONObject bookingDates = new JSONObject();

    jsonObject.put("firstname", "Tom");
    jsonObject.put("lastname", "Grey");
    jsonObject.put("totalprice", 111);
    jsonObject.put("depositpaid", true);

    bookingDates.put("checkin", "2018-01-01");
    bookingDates.put("checkout", "2019-01-01");

    jsonObject.put("bookingdates", bookingDates);
    jsonObject.put("additionalneeds", "Breakfast");

    Response responseUpdate = RestAssured.given(requestSpecification).auth().preemptive().basic("admin", "password123")
        .contentType(ContentType.JSON).body(jsonObject.toString())
        .put("/booking/" + id);

    Assert.assertEquals(responseUpdate.getStatusCode(), 200,
        "Status code should be 200 but is " + responseUpdate.getStatusCode());

    SoftAssert softAssert = new SoftAssert();
    String actualFirstName = responseUpdate.jsonPath().getString("firstname");
    softAssert.assertEquals(actualFirstName, "Tom");

    String actualCheckIn = responseUpdate.jsonPath().getString("bookingdates.checkin");
    softAssert.assertEquals(actualCheckIn, "2018-01-01");

    softAssert.assertAll();
  }
}