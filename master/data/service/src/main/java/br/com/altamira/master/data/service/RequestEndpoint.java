package br.com.altamira.master.data.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

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
		Hibernate4Module hm = new Hibernate4Module();
    	objectMapper.registerModule(hm);
    	List<Request> list = requestDao.getAll();
    	String r = objectMapper.writeValueAsString(list);
    	return Response.ok(r).build();
	}
}
