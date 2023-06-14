import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.zackmanning.hostfully.Main;
import me.zackmanning.hostfully.model.Block;
import me.zackmanning.hostfully.model.BlockCreate;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockResourceTest {

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

    private BlockCreate dummyBlockCreate() {
        Instant random = Instant.ofEpochSecond(new Random().nextInt());
        return new BlockCreate(
                random.minus(3, ChronoUnit.DAYS).toString(),
                random.toString());
    }

    /**
     * Let's verify that we can create a Block
     */
    @Test
    public void testBlockCreate() {
        BlockCreate input = dummyBlockCreate();

        Block block = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Block.class);

        assertEquals(input.getStart(), block.getStart().toString());
        assertEquals(input.getEnd(), block.getEnd().toString());
        assertTrue(block.getId() != 0);
    }

    @Test
    public void testBlockGet() {
        BlockCreate input = dummyBlockCreate();

        Block Block = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Block.class);

        Block gotBlock = target.path("block/" + Block.getId()).request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Block.class);

        assertEquals(Block.getId(), gotBlock.getId());
        assertEquals(Block.getStart(), gotBlock.getStart());
        assertEquals(Block.getEnd(), gotBlock.getEnd());
    }

    @Test
    public void testBlocksGet() {
        Block block = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(dummyBlockCreate(), MediaType.APPLICATION_JSON), Block.class);
        Block block1 = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(dummyBlockCreate(), MediaType.APPLICATION_JSON), Block.class);

        Block[] gotBlocks = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Block[].class);

        assertEquals(block.getId(), gotBlocks[0].getId());
        assertEquals(block.getStart(), gotBlocks[0].getStart());
        assertEquals(block.getEnd(), gotBlocks[0].getEnd());

        assertEquals(block1.getId(), gotBlocks[1].getId());
        assertEquals(block1.getStart(), gotBlocks[1].getStart());
        assertEquals(block1.getEnd(), gotBlocks[1].getEnd());
    }

    @Test
    public void testBlockCancel() {
        BlockCreate input = dummyBlockCreate();

        Block block = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Block.class);

        Response cancelled = target.path("block/" + block.getId()).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(204, cancelled.getStatus());
    }

    /**
     * Let's verify that we can edit an existing Block
     */
    @Test
    public void testBlockUpdate() {
        BlockCreate input = dummyBlockCreate();

        Block created = target.path("block").request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(input, MediaType.APPLICATION_JSON), Block.class);

        Block toUpdate = new Block(created.getId(), created.getStart(), created.getEnd().plus(2, ChronoUnit.DAYS));

        Block edit = target.path("block/" + toUpdate.getId()).request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(toUpdate, MediaType.APPLICATION_JSON), Block.class);

        Block updated = target.path("block/" + created.getId()).request()
                .accept(MediaType.APPLICATION_JSON)
                .get(Block.class);

        assertEquals(toUpdate.getStart(), updated.getStart());
        assertEquals(toUpdate.getEnd(), updated.getEnd());
    }
}
