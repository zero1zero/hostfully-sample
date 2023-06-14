package me.zackmanning.hostfully;

import com.fasterxml.jackson.core.util.JacksonFeature;
import me.zackmanning.hostfully.persist.BlockStore;
import me.zackmanning.hostfully.persist.BookingStore;
import me.zackmanning.hostfully.resource.BlockResource;
import me.zackmanning.hostfully.resource.BookingResource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig()
                .register(JacksonFeature.class)
                .register(CORSFilter.class)
                .registerInstances(new BlockResource(new BlockStore()))
                .registerInstances(new BookingResource(new BookingStore()));

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        while (true) ;
    }
}

