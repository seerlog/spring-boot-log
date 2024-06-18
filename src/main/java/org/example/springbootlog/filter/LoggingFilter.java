package org.example.springbootlog.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        /** 비동기 요청 */
        if(isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        }
        /** 동기 요청 */
        else {
            MDC.put("traceId", UUID.randomUUID().toString());

            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            long startTime = System.currentTimeMillis();
            logger.info("Request URI: {}", requestWrapper.getRequestURI());
            filterChain.doFilter(requestWrapper, responseWrapper);
            logger.info("Response Status: {}", responseWrapper.getStatus());
            long endTime = System.currentTimeMillis();
            logger.info("Elapsed Time: {}ms", endTime - startTime);

            MDC.clear();
        }

    }

}
