package restfulbooker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTest extends BaseTest {

  @Test
  public void deleteBooking() {

    Response responseCreate = createBooking();
    responseCreate.print();

    int id = responseCreate.jsonPath().getInt("bookingid");

    Response responseDeleteBooking = RestAssured.given(requestSpecification).auth().preemptive()
        .basic("admin", "password123")
        .delete("/booking/" + id);

    Assert.assertEquals(responseDeleteBooking.getStatusCode(), 201,
        "Status code should be 201 but is " + responseDeleteBooking.getStatusCode());

    Response response = RestAssured.given(requestSpecification).get("/booking/"+id);
    response.print();

    Assert.assertEquals(response.getBody().asString(),"Not Found", "Body should be 'Not Found'");

  }

}
