package me.zackmanning.hostfully.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import me.zackmanning.hostfully.model.Block;
import me.zackmanning.hostfully.model.BlockCreate;
import me.zackmanning.hostfully.persist.BlockStore;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

@Path("block")
public class BlockResource {

    private final BlockStore store;

    public BlockResource(BlockStore store) {
        this.store = store;
    }

    /**
     * API method to return all Blocks
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Block> all() {
        return store.all();
    }

    /**
     * API method to return a Block based on ID
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Block get(@PathParam("id") int id) {
        Optional<Block> block = store.get(id);

        if (!block.isPresent()) {
            throw new NotFoundException();
        }

        return block.get();
    }

    /**
     * API method to create a new Block
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Block create(BlockCreate input) {
        Block newBlock = new Block(
                new Random().nextInt(),
                Instant.parse(input.getStart()),
                Instant.parse(input.getEnd()));

        store.add(newBlock);

        return newBlock;
    }

    /**
     * API method to update a Block
     * TODO ideally, we'd use a PATCH method here
     */
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") int id, Block toUpdate) {
        if (id != toUpdate.getId()) {
            throw new BadRequestException();
        }

        store.update(toUpdate);
    }

    /**
     * API method to delete a Block
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") int id) {
        store.delete(id);
    }
}
