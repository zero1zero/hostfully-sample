package me.zackmanning.hostfully.persist;

import me.zackmanning.hostfully.model.Block;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class BlockStore {

    private final Map<Integer, Block> db = new LinkedHashMap<>();

    public Collection<Block> all() {
        return db.values();
    }

    public Optional<Block> get(int id) {
        return Optional.ofNullable(this.db.get(id));
    }

    public void add(Block block) {
        this.db.put(block.getId(), block);
    }

    public void update(Block block) {
        this.db.put(block.getId(), block);
    }

    public void delete(int id) {
        this.db.remove(id);
    }
}
