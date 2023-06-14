package me.zackmanning.hostfully.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class Block {
    private final int id;
    private final Instant start;
    private final Instant end;

    @JsonCreator
    public Block(@JsonProperty("id") int id,
                 @JsonProperty("start") Instant start,
                 @JsonProperty("end") Instant end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public boolean isOverlapping(Block Block) {
        return start.isBefore(Block.getEnd()) && Block.getStart().isBefore(end);
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public int getId() {
        return id;
    }
}
