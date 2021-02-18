package restfulbooker;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HealthCheckTest extends BaseTest {

  @Test
  public void healthCheckTest() {

    given().spec(requestSpecification)
        .when()
        .get("/ping")
        .then()
        .assertThat()
        .statusCode(201);
  }

  @Test
  public void headersAndCookies() {

    Header header = new Header("Test header","Header value");
    requestSpecification.header(header);

    Cookie cookie = new Cookie.Builder("Test cookie", "Test value").build();
    requestSpecification.cookie(cookie);

    Response response = RestAssured.given(requestSpecification).log().all().get("/ping");
    Headers headers = response.getHeaders();
    Header serverHeader = headers.get("Server");

    System.out.println(serverHeader.toString());
    Cookies cookies = response.getDetailedCookies();
    System.out.println(cookies.toString());
  }


  @Test
  public void getBookingJson() {

    requestSpecification.pathParam("bookingId", 5);

    Response response = RestAssured.given(requestSpecification).get("booking/{bookingId}");
    response.print();
    Assert.assertEquals(response.getStatusCode(), 200,
        "Status code should be 200 but is " + response.getStatusCode());

    SoftAssert softAssert = new SoftAssert();
    String actualFirstName = response.jsonPath().getString("firstname");
    softAssert.assertEquals(actualFirstName, "Jim");

    String actualCheckIn = response.jsonPath().getString("bookingdates.checkin");
    softAssert.assertEquals(actualCheckIn, "2020-06-23");

    softAssert.assertAll();
  }

  @Test
  public void getBookingXml() {

    Header xmlHeader = new Header("Accept","application/xml");
    requestSpecification.header(xmlHeader);

    requestSpecification.pathParam("bookingId", 5);

    Response response = RestAssured.given(requestSpecification).get("booking/{bookingId}");
    response.print();
    Assert.assertEquals(response.getStatusCode(), 200,
        "Status code should be 200 but is " + response.getStatusCode());

    SoftAssert softAssert = new SoftAssert();
    String actualFirstName = response.xmlPath().getString("booking.firstname");
    softAssert.assertEquals(actualFirstName, "Susan");

    String actualCheckIn = response.xmlPath().getString("booking.bookingdates.checkin");
    softAssert.assertEquals(actualCheckIn, "2021-01-24");

    softAssert.assertAll();
  }

}
