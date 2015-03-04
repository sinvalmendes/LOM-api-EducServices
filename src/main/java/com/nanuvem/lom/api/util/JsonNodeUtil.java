package com.nanuvem.lom.api.util;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.nanuvem.lom.api.MetadataException;

public class JsonNodeUtil {

    public static JsonNode validate(String json, String validationErrorMessage) {
        JsonNode jsonNode;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory factory = objectMapper.getJsonFactory();
            jsonNode = objectMapper.readTree(factory.createJsonParser(json));
        } catch (Exception e) {
            if (validationErrorMessage == null || validationErrorMessage.isEmpty()) {
                validationErrorMessage = e.getMessage();
            }
            throw new MetadataException(validationErrorMessage);
        }
        return jsonNode;
    }

}
