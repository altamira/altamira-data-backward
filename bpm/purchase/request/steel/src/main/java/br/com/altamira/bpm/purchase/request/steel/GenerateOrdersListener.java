package br.com.altamira.bpm.purchase.request.steel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;

@Named("GenerateOrders")
public class GenerateOrdersListener implements ExecutionListener  {

	private final static Logger LOGGER = Logger.getLogger(GenerateOrdersListener.class.getName());

	@Inject
	private RuntimeService runtimeService;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Collection<String> ordersList = new ArrayList<String>();
		
		ordersList.add("1");
		ordersList.add("2");
		ordersList.add("3");
		
		runtimeService.setVariable(execution.getProcessInstanceId(), "ordersList", ordersList);
		//execution.setVariable("ordersList", ordersList);
		LOGGER.info(ordersList.size() + " Purchase Orders Generated ! ");	
	}

}
