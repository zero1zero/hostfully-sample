package me.zackmanning.hostfully.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Booking {
    private final int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final LocalDate start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final LocalDate end;

    @JsonCreator
    public Booking(@JsonProperty("id") int id,
                   @JsonProperty("start") LocalDate start,
                   @JsonProperty("end") LocalDate end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public boolean isOverlapping(Booking booking) {
        //if either on the same day
        if (booking.getEnd().equals(end)
                || booking.getStart().equals(start)) {
            return true;
        }
        return start.isBefore(booking.getEnd()) && booking.getStart().isBefore(end);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public int getId() {
        return id;
    }
}
