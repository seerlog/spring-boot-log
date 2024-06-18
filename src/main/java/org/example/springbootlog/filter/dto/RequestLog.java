package org.example.springbootlog.filter.dto;

import lombok.Data;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Data
public class RequestLog {
    private String httpMethod;
    private String requestUri;
    private String clientIp;
    private String headers;
    private String requestParam;
    private String requestBody;

    public static RequestLog of(ContentCachingRequestWrapper requestWrapper) {
        RequestLog requestLog = new RequestLog();
        requestLog.setHttpMethod(requestWrapper.getMethod());
        requestLog.setRequestUri(requestWrapper.getRequestURI());
        requestLog.setClientIp(requestWrapper.getRemoteAddr());
        requestLog.setHeaders(requestWrapper.getHeaderNames().toString());
        requestLog.setRequestParam(requestWrapper.getParameterMap().toString());
        requestLog.setRequestBody(new String(requestWrapper.getContentAsByteArray()));
        return requestLog;
    }

    @Override
    public String toString() {
        return "httpMethod=" + httpMethod +
                ", requestUri=" + requestUri +
                ", clientIp=" + clientIp +
                ", headers=" + headers +
                ", requestParam=" + requestParam +
                ", requestBody=" + requestBody;
    }
}
