package com.liveperson.dist;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.io.IOException;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

public class SimpleApp extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new SimpleApp().run(args);
    }
    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
    }

    @Override
    public void run(Configuration configuration, Environment environment) {
        environment.jersey().register(new RestResource());

        final FilterRegistration.Dynamic filter = environment.servlets().addFilter("filter", MyServletFilter.class);
        filter.getInitParameters().put("param", "world!");
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }

    @Path("/")
    public static class RestResource {
        @GET
        public Response root(@Context HttpServletRequest httpRequest) {
            return Response.ok("Hello " + httpRequest.getAttribute("key")).build();
        }
    }

    public static class MyServletFilter implements Filter {
        String param = "default";

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            param = filterConfig.getInitParameter("param");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            request.setAttribute("key", param);
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {
        }
    }
}
