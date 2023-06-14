import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.zackmanning.hostfully.Main;
import me.zackmanning.hostfully.model.Booking;
import me.zackmanning.hostfully.model.BookingCreate;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingResourceTest {

    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        Client client = ClientBuilder.newClient();

        target = client.target(Main.BASE_URI);
    }

    @AfterEach
    public void tearDown() {
        server.shutdownNow();
    }

    private BookingCreate dummyBookingCreate() {
        LocalDate random = LocalDate.ofInstant(Instant.ofEpochSecond(new Random().nextInt()), ZoneId.systemDefault());
        return new BookingCreate(
                random.minus(3, ChronoUnit.DAYS).toString(),
                random.toString());
    }

    /**
     * Let's verify that we can create a booking
     */
    @Test
    public void testBookingCreate() {
        BookingCreate input = dummyBookingCreate();

        Booking booking = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Booking.class);

        assertEquals(input.getStart(), booking.getStart().toString());
        assertEquals(input.getEnd(), booking.getEnd().toString());
        assertTrue(booking.getId() != 0);
    }

    @Test
    public void testBookingGet() {
        BookingCreate input = dummyBookingCreate();

        Booking booking = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Booking.class);

        Booking gotBooking = target.path("booking/" + booking.getId()).request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Booking.class);

        assertEquals(booking.getId(), gotBooking.getId());
        assertEquals(booking.getStart(), gotBooking.getStart());
        assertEquals(booking.getEnd(), gotBooking.getEnd());
    }

    @Test
    public void testBookingsGet() {
        Booking booking = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(dummyBookingCreate(), MediaType.APPLICATION_JSON), Booking.class);
        Booking booking1 = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(dummyBookingCreate(), MediaType.APPLICATION_JSON), Booking.class);

        Booking[] gotBookings = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Booking[].class);

        assertEquals(booking.getId(), gotBookings[0].getId());
        assertEquals(booking.getStart(), gotBookings[0].getStart());
        assertEquals(booking.getEnd(), gotBookings[0].getEnd());

        assertEquals(booking1.getId(), gotBookings[1].getId());
        assertEquals(booking1.getStart(), gotBookings[1].getStart());
        assertEquals(booking1.getEnd(), gotBookings[1].getEnd());
    }

    @Test
    public void testBookingCancel() {
        BookingCreate input = dummyBookingCreate();

        Booking booking = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Booking.class);

        Response cancelled = target.path("booking/" + booking.getId()).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(204, cancelled.getStatus());
    }

    @Test
    public void testBookingConflict() {
        BookingCreate input = dummyBookingCreate();

        Booking booking = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Booking.class);

        Response conflict = target.path("booking").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON));

        assertEquals(409, conflict.getStatus());
    }

}
