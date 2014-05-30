package br.com.altamira.bpm.purchase.request.steel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;

@Named("MergeRequest")
public class MergeRequestListener implements ExecutionListener  {

	private final static Logger LOGGER = Logger.getLogger(MergeRequestListener.class.getName());

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		LOGGER.log(Level.WARNING, "A NEW REQUEST WAS MERGED TO CURRENT QUOTATION. Request ID: " + execution.getVariable("REQUEST_ID"));
	}

}
