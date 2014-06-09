package br.com.altamira.master.data.service;

import java.io.IOException;

import javax.inject.Named;





import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.com.altamira.master.data.model.Request;

@Named
public class RequestSerializer extends StdSerializer<Request> {

	public RequestSerializer() {
		super(Request.class);
	}
	
	@Override
	public void serialize(Request value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeObjectField("createdDate", value.getCreatedDate());
        jgen.writeStringField("createName", value.getCreatorName());
        jgen.writeEndObject();
		
	}

}