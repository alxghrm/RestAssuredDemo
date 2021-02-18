package restfulbooker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetBookingIdsTests extends BaseTest {

  @Test
  public void getBookingIdsWithoutFilter() {
    Response response = RestAssured.given(requestSpecification).get("/booking");
    response.print();
    Assert.assertEquals(response.getStatusCode(), 200,
        "Status code should be 200 but is " + response.getStatusCode());
    List<Integer> bookingIds = response.jsonPath().getList("bookingid");
    Assert.assertFalse(bookingIds.isEmpty(), "List of ids is empty");
  }

  @Test
  public void getBookingIdWithFilter() {

    requestSpecification.queryParam("firstname","Eric");
    requestSpecification.queryParam("lastname","Brown");

    Response response = RestAssured.given(requestSpecification).get("/booking");
    response.print();
    Assert.assertEquals(response.getStatusCode(), 200,
        "Status code should be 200 but is " + response.getStatusCode());
    List<Integer> bookingIds = response.jsonPath().getList("bookingid");
    Assert.assertFalse(bookingIds.isEmpty(), "List of ids is empty");

  }

}
