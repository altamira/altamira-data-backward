package br.com.altamira.bpm.purchase.request.steel;

import java.util.HashMap;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.camunda.bpm.engine.RuntimeService;

import br.com.altamira.erp.entity.model.Request;

public class RequestService {

	@Inject
	private RuntimeService runtimeService;
	
	@POST
	public void StartProcess(Request request) {
		
		HashMap<String, Object> processVariables = new HashMap<String, Object>();
		
		processVariables.put("REQUEST_ID", request.getId());
		
		runtimeService.startProcessInstanceByKey("br.com.altamira.bpm.purchase.request.steel", processVariables);
	}
}
