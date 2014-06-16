package br.com.altamira.master.data.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import br.com.altamira.master.data.model.Request;

public class RequestSerializer extends JsonSerializer<Request> {

	@Override
	public void serialize(Request value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeObjectField("created", value.getCreated());
		jgen.writeStringField("creator", value.getCreator());
		jgen.writeObjectField("sent", value.getSent() != null ? value.getSent() : "");
		jgen.writeObjectField("items", value.getItems() != null ? value.getItems() : "[]");
		jgen.writeEndObject();
	}

}