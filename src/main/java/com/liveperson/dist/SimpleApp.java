package com.liveperson.dist;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.io.IOException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

public class SimpleApp extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new SimpleApp().run(args);
    }
    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        environment.jersey().register(new MyJerseyRequestFilter());
        environment.jersey().register(new RestResource());
    }

    @Path("/")
    public static class RestResource {
        @GET
        public Response root(@Context HttpHeaders headers) {
            return Response.ok("Hello " + headers.getHeaderString("key")).build();
        }
    }

    public static class MyJerseyRequestFilter implements ContainerRequestFilter {
        @Override
        public void filter(ContainerRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add("key", "World!");
        }
    }
}
