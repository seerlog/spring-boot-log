package org.example.springbootlog.filter.log.dto;

import lombok.Data;
import org.example.springbootlog.filter.log.wrapper.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Data
public class ResponseLog {
    private HttpStatus httpStatus;
    private String responseBody;
    private String elapsedTime;

    public static ResponseLog of(ResponseWrapper responseWrapper, long elapsedTime) {
        ResponseLog responseLog = new ResponseLog();
        responseLog.setHttpStatus(HttpStatus.valueOf(responseWrapper.getStatus()));
        responseLog.setResponseBody(new String(responseWrapper.getContentAsByteArray()));
        responseLog.setElapsedTime(withMillis(elapsedTime));
        return responseLog;
    }

    private static String withMillis(long elapsedTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(elapsedTime);
        stringBuilder.append("ms");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "httpStatus=" + httpStatus +
                ", responseBody=" + responseBody +
                ", elapsedTime=" + elapsedTime ;
    }
}
