package me.zackmanning.hostfully.persist;

import me.zackmanning.hostfully.model.Booking;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class BookingStore {

    private final Map<Integer, Booking> db = new LinkedHashMap<>();

    public Collection<Booking> all() {
        return db.values();
    }

    public Optional<Booking> get(int id) {
        return Optional.ofNullable(this.db.get(id));
    }

    public void add(Booking booking) {
        this.db.put(booking.getId(), booking);
    }

    public void delete(int id) {
        this.db.remove(id);
    }
}
