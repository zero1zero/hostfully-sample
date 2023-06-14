package me.zackmanning.hostfully.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.zackmanning.hostfully.model.Booking;
import me.zackmanning.hostfully.model.BookingCreate;
import me.zackmanning.hostfully.persist.BookingStore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

@Path("booking")
public class BookingResource {

    private final BookingStore store;

    public BookingResource(BookingStore store) {
        this.store = store;
    }

    /**
     * API method to return all Bookings
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Booking> all() {
        return store.all();
    }

    /**
     * API method to return a Booking based on ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Booking get(@PathParam("id") int id) {
        Optional<Booking> booking = store.get(id);

        if (!booking.isPresent()) {
            throw new NotFoundException();
        }

        return booking.get();
    }

    /**
     * API method to create a new Booking
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Booking create(BookingCreate input) {
        Booking newBooking = new Booking(
                new Random().nextInt() & Integer.MAX_VALUE,
                LocalDate.parse(input.getStart(), DateTimeFormatter.ISO_LOCAL_DATE),
                LocalDate.parse(input.getEnd(), DateTimeFormatter.ISO_LOCAL_DATE));

        for (Booking booking : store.all()) {
            if (newBooking.isOverlapping(booking)) {
                throw new ClientErrorException(Response.Status.CONFLICT);
            }
        }

        store.add(newBooking);

        return newBooking;
    }

    /**
     * API method to delete a Booking
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") int id) {
        store.delete(id);
    }
}
