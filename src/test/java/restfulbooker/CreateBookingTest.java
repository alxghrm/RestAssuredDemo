package restfulbooker;

import bookings.Booking;
import bookings.Bookingdates;
import bookings.Bookingid;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreateBookingTest extends BaseTest {


  @Test
  public void createBookingTest() {

    Response response = createBooking();
    response.print();

    Assert.assertEquals(response.getStatusCode(), 200,
        "Status code should be 200 but is " + response.getStatusCode());

    SoftAssert softAssert = new SoftAssert();
    String actualFirstName = response.jsonPath().getString("booking.firstname");
    softAssert.assertEquals(actualFirstName, "Alex");

    String actualCheckIn = response.jsonPath().getString("booking.bookingdates.checkin");
    softAssert.assertEquals(actualCheckIn, "2018-01-01");

    softAssert.assertAll();
  }

  @Test
  public void createBookingWithPojo() {

    Bookingdates bookingdates = new Bookingdates("2021-04-20", "2021-05-20");
    Booking booking = new Booking("John", "Smith", 200, false, bookingdates, "Baby crib");


    Response response = RestAssured.given(requestSpecification).contentType(ContentType.JSON)
        .body(booking)
        .post("/booking");

    Bookingid bookingid = response.as(Bookingid.class);

    Assert.assertEquals(response.getStatusCode(), 200,
        "Status code should be 200 but is " + response.getStatusCode());

    response.print();
    System.out.println(bookingid.getBooking().toString());


    Assert.assertEquals(bookingid.getBooking().toString(), booking.toString());
  }
}