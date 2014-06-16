package br.com.altamira.data.service;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper>
{
    private final ObjectMapper objectMapper;
    public JacksonConfig()
    {
        objectMapper = new ObjectMapper();
        //objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        //objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    	Hibernate4Module hm = new Hibernate4Module();
    	objectMapper.registerModule(hm);
    }
    @Override
    public ObjectMapper getContext(Class<?> objectType)
    {
        return objectMapper;
    }
}