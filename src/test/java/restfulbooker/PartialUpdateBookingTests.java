package restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PartialUpdateBookingTests extends BaseTest {

  @Test
  public void PartialUpdateBooking() {

    Response responseCreate = createBooking();
    responseCreate.print();

    int id = responseCreate.jsonPath().getInt("bookingid");

    JSONObject jsonObject = new JSONObject();
    JSONObject bookingDates = new JSONObject();

    jsonObject.put("firstname", "Mike");
    bookingDates.put("checkin", "2018-01-05");
    bookingDates.put("checkout", "2019-01-07");
    jsonObject.put("bookingdates", bookingDates);

    Response responsePartialUpdate = RestAssured.given(requestSpecification).auth().preemptive().basic("admin", "password123")
        .contentType(ContentType.JSON).body(jsonObject.toString())
        .patch("/booking/" + id);
    Assert.assertEquals(responseCreate.getStatusCode(), 200,
        "Status code should be 200 but is " + responsePartialUpdate.getStatusCode());

    Response response = RestAssured.given(requestSpecification).get("/booking/"+id);
    response.print();

    SoftAssert softAssert = new SoftAssert();
    String actualFirstName = responsePartialUpdate.jsonPath().getString("firstname");
    softAssert.assertEquals(actualFirstName, "Mike");

    String actualCheckIn = responsePartialUpdate.jsonPath().getString("bookingdates.checkin");
    softAssert.assertEquals(actualCheckIn, "2018-01-05");

    softAssert.assertAll();
  }
}
