package org.example.springbootlog.filter.log.dto;

import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Data;
import org.example.springbootlog.filter.log.wrapper.RequestWrapper;

@Data
public class RequestLog {
    private String httpMethod;
    private String requestUri;
    private String clientIp;
    private String headers;
    private String requestParam;
    private String requestBody;

    public static RequestLog of(RequestWrapper requestWrapper) {
        RequestLog requestLog = new RequestLog();
        requestLog.setHttpMethod(requestWrapper.getMethod());
        requestLog.setRequestUri(requestWrapper.getRequestURI());
        requestLog.setClientIp(requestWrapper.getRemoteAddr());
        requestLog.setHeaders(requestWrapper.getHeaderNames().toString());
        requestLog.setRequestParam(requestWrapper.getParameterMap().toString());
//        requestLog.setRequestBody(new String(requestWrapper.));
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
