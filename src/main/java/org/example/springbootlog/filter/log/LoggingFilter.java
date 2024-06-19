package org.example.springbootlog.filter.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootlog.filter.log.util.Create;
import org.example.springbootlog.filter.log.wrapper.RequestWrapper;
import org.example.springbootlog.filter.log.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
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
            doFilterWrapped(new RequestWrapper(request), new ResponseWrapper(response), filterChain);
        }
    }

    protected void doFilterWrapped(RequestWrapper requestWrapper, ResponseWrapper responseWrapper, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put("traceId", UUID.randomUUID().toString());
        logRequest(requestWrapper);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long endTime = System.currentTimeMillis();

        logResponse(responseWrapper, startTime, endTime);
        responseWrapper.copyBodyToResponse();
        MDC.clear();
    }

    private static void logRequest(RequestWrapper request) throws IOException {
        String queryString = request.getQueryString();
        logger.info("Request : method=[{}], uri=[{}], clientIp=[{}], contentType=[{}], headers=[{}], param=[{}], body=[{}]",
                request.getMethod(),
                queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString,
                request.getRemoteAddr(),
                request.getContentType(),
                Create.header(request),
                request.getQueryString(),
                Create.body(request.getContentType(), request.getInputStream()));
    }

    private static void logResponse(ContentCachingResponseWrapper response, long startTime, long entTime) throws IOException {
        logger.info("Response : elapsedTime=[{}ms], body=[{}]",
                entTime - startTime,
                Create.body(response.getContentType(), response.getContentInputStream()));
    }
}
