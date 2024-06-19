package org.example.springbootlog.filter.log.util;

import org.example.springbootlog.filter.log.wrapper.RequestWrapper;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Create {
    public static String header(RequestWrapper request) {
        StringBuilder headerString = new StringBuilder();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        int headerSize = headerNames.size();
        for(int i = 0; i < headerSize; i++) {
            String headerName = headerNames.get(i);
            String value = request.getHeader(headerName);
            headerString
                    .append(headerName)
                    .append("=")
                    .append("\"")
                    .append(value)
                    .append("\"");
            if(i < headerSize - 1) {
                headerString.append(", ");
            }
        }
        return headerString.toString();
    }

    public static String body(String contentType, InputStream inputStream) throws IOException {
        boolean visible = isVisible(contentType);
        if(visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            return new String(content);
        }
        return "Binary Content";
    }

    private static boolean isVisible(String contentType) {
        MediaType mediaType = MediaType.valueOf(contentType == null ? "application/json" : contentType);
        final List<MediaType> VISIBLE_TYPES = Arrays.asList(
                MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("application/*+xml"),
                MediaType.MULTIPART_FORM_DATA);
        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}
