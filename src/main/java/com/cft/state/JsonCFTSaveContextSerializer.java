package com.cft.state;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonCFTSaveContextSerializer implements CFTSaveContextSerializer {
    private final ObjectMapper jsonMapper;

    public JsonCFTSaveContextSerializer(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public CFTState.SaveContext readSaveContextStream(InputStream stream) throws IOException {
        return this.jsonMapper.readValue(stream, CFTState.SaveContext.class);
    }

    @Override
    public void writeToSaveContextStream(CFTState.SaveContext saveContext, OutputStream stream) throws IOException {
        this.jsonMapper.writeValue(stream, saveContext);
    }
}
