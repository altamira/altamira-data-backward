package br.com.altamira.master.data.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import br.com.altamira.master.data.dao.RequestDao;
import br.com.altamira.master.data.model.Request;

@Path("request")
public class RequestEndpoint {

	@Inject
	private RequestDao requestDao;
	
	@GET
	@Produces("application/json")
	public Response getAll() throws JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Version version = new Version(1, 0, 0, "SNAPSHOT", "br.com.altamira", "master.data.service.serializer"); // maven/OSGi style version
		SimpleModule module = new SimpleModule("RequestSerializer", version);
		module = module.addSerializer(new RequestSerializer());
		//objectMapper.registerModule(module);
		objectMapper.registerModule(new JodaModule());
		objectMapper.registerModule(new Hibernate4Module());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());
		   
		List<Request> list = requestDao.getAll();
		
		list.add(new Request(1l, DateTime.now().toDate(), "my1"));
		list.add(new Request(2l, DateTime.now().toDate(), "my2"));
		String serialized = objectMapper.writeValueAsString(list);
		
		return Response.ok(serialized).build();
		
		/*Hibernate4Module hm = new Hibernate4Module();
    	objectMapper.registerModule(hm);
    	List<Request> list = requestDao.getAll();
    	String r = objectMapper.writeValueAsString(list);
    	return Response.ok(r).build();*/
    	
		/*List<Request> list = requestDao.getAll();
    	ObjectMapper mapper = new ObjectMapper();
    	 
    	SimpleModule module = new SimpleModule();
    	module.addSerializer(Request.class, new RequestSerializer());
    	mapper.registerModule(module);
    	 
    	String serialized = mapper.writeValueAsString(list);*/
	}
}
