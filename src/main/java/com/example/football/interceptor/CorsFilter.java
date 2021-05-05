package com.example.football.interceptor;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorsFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(CorsFilter.class.getSimpleName());
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        LOG.info("is called");

        response.addHeader("Access-Control-Allow-Origin", "http://localhost:5502");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
        response.addHeader("Access-Control-Allow-Headers",
                "Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addIntHeader("Access-Control-Max-Age", 10);
        filterChain.doFilter(request, response);

        //        response.setHeader("Access-Control-Allow-Origin", "*");
        //        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        //        response.setHeader("Access-Control-Max-Age", "3600");
        //        response.setHeader("Access-Control-Allow-Headers",
        //                "X-PINGOTHER,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
        //        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        //        LOG.info("response. Access-Control-Allow-Origi = " + response.getHeader("Access-Control-Allow-Origin"));
        //        if ("OPTIONS".equals(request.getMethod())) {
        //            response.setStatus(HttpServletResponse.SC_OK);
        //        } else {
        //            filterChain.doFilter(request, response);
        //        }


    }
}

