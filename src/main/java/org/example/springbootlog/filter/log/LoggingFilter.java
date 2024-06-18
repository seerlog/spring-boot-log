package org.example.springbootlog.filter.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.example.springbootlog.filter.log.dto.RequestLog;
import org.example.springbootlog.filter.log.dto.ResponseLog;
import org.example.springbootlog.filter.log.wrapper.RequestWrapper;
import org.example.springbootlog.filter.log.wrapper.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
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
        logger.info("Request : {} uri=[{}] content-type=[{}]", request.getMethod(), queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString, request.getContentType());
        logPayload("Request", request.getContentType(), request.getInputStream());
    }

    private static void logResponse(ContentCachingResponseWrapper response, long startTime, long entTime) throws IOException {
        logPayload("Response", response.getContentType(), response.getContentInputStream());
        logger.info("Response : elapsedTime={}ms", entTime - startTime);
    }

    private static void logPayload(String prefix, String contentType, InputStream inputStream) throws IOException {
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                String contentString = new String(content);
                logger.info("{} Payload: {}", prefix, contentString);
            }
        } else {
            logger.info("{} Payload: Binary Content", prefix);
        }
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(MediaType.valueOf("text/*"), MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"), MediaType.MULTIPART_FORM_DATA);
        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    private void printRequest(RequestWrapper requestWrapper) {
        logger.info(RequestLog.of(requestWrapper).toString());
    }

    private void printResponse(ResponseWrapper responseWrapper, long startTime, long endTime) {
        logger.info(ResponseLog.of(responseWrapper, endTime - startTime).toString());
    }
}
