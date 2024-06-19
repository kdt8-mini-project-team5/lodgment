package com.accommodation.accommodation.global.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogAPIFilter implements Filter {

    private static final Logger apiLogger = LoggerFactory.getLogger("API_LOGGER");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 코드
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;


        long startTime = System.currentTimeMillis();

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        apiLogger.info("Method: {}, URI: {}, Status: {}, Duration: {} ms",
                httpRequest.getMethod(), httpRequest.getRequestURI(),
                httpResponse.getStatus(), duration);


    }

    @Override
    public void destroy() {
        // 정리 코드
    }
}
