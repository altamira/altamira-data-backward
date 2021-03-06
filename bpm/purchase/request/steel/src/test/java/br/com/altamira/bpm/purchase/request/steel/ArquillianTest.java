package br.com.altamira.bpm.purchase.request.steel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ArquillianTest {

	private static final String PROCESS_DEFINITION_KEY_STEEL = "br.com.altamira.bpm.purchase.request.steel";
	private static final String PROCESS_DEFINITION_KEY_ORDER = "br.com.altamira.bpm.purchase.request.order";

	@Deployment
	public static WebArchive createDeployment() {
		// resolve given dependencies from Maven POM
		File[] libs = Maven.resolver().offline(false)
				.loadPomFromFile("pom.xml").importRuntimeAndTestDependencies()
				.resolve().withTransitivity().asFile();

		return ShrinkWrap
				.create(WebArchive.class, "steel.war")
				// add needed dependencies
				.addAsLibraries(libs)
				// prepare as process application archive for camunda BPM
				// Platform
				.addAsWebResource("META-INF/processes.xml",
						"WEB-INF/classes/META-INF/processes.xml")
				// enable CDI
				.addAsWebResource("WEB-INF/beans.xml", "WEB-INF/beans.xml")
				// boot JPA persistence unit
				//.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
				// add your own classes (could be done one by one as well)
				.addPackages(false,
						"br.com.altamira.bpm.purchase.request.steel") // not
																		// recursive
																		// to
																		// skip
																		// package
																		// 'nonarquillian'
				// add process definition
				.addAsResource("process.bpmn")
				// add process image for visualizations
				.addAsResource("process.png")
		// now you can add additional stuff required for your test case
		;
	}

	@Inject
	private ProcessEngine processEngine;

	/**
	 * Tests that the process is executable and reaches its end.
	 */
	@Test
	public void testProcessExecution() throws Exception {
		cleanUpRunningProcessInstances();

		HashMap<String, Object> processVariables = new HashMap<String, Object>();
		
		processVariables.put("REQUEST_ID", "1");
		
		// Create new request
		processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY_STEEL, processVariables);

		// Check if a request process was done
		assertEquals(1, processEngine.getHistoryService().createHistoricProcessInstanceQuery().processDefinitionKey(PROCESS_DEFINITION_KEY_STEEL).count());
		
		// Check if a order process is instantiated by REQUEST_CREATED_MESSAGE
		assertEquals(1, processEngine.getRuntimeService().createExecutionQuery().processDefinitionKey(PROCESS_DEFINITION_KEY_ORDER).count());
		
	}

	//@Resource(mappedName = "java:jboss/mail/Default")
	@Resource(lookup = "java:jboss/mail/Default")
	private Session mailSession;

	// private static final String HOST = "mail.example.org";
	private static final String USER = "junit@altamira.com.br";
	// private static final String PWD = "toomanysecrets";
	private static final String EMAIL = "gerencia.ti@altamira.com.br";

	private final static Logger LOGGER = Logger.getLogger(ArquillianTest.class
			.getName());

	@Test
	public void SendMailTest() {
		try {
			MimeMessage m = new MimeMessage(mailSession);
			Address from = new InternetAddress(USER);
			Address[] to = new InternetAddress[] { new InternetAddress(EMAIL) };

			m.setFrom(from);
			m.setRecipients(Message.RecipientType.TO, to);
			m.setSubject("JBoss AS 7 Mail");
			m.setSentDate(new java.util.Date());
			m.setContent("Mail sent from JBoss AS 7", "text/plain");
			Transport.send(m);
			LOGGER.log(Level.INFO, "Mail sent!");

		} catch (javax.mail.MessagingException e) {
			e.printStackTrace();
			LOGGER.log(Level.WARNING, "Error in Sending Mail: " + e);
			fail(e.getMessage());
		}
	}

	/**
	 * Helper to delete all running process instances, which might disturb our
	 * Arquillian Test case Better run test cases in a clean environment, but
	 * this is pretty handy for demo purposes
	 */
	private void cleanUpRunningProcessInstances() {
		List<ProcessInstance> runningInstances = processEngine
				.getRuntimeService().createProcessInstanceQuery()
				.processDefinitionKey(PROCESS_DEFINITION_KEY_STEEL).list();
		for (ProcessInstance processInstance : runningInstances) {
			processEngine.getRuntimeService().deleteProcessInstance(
					processInstance.getId(),
					"deleted to have a clean environment for Arquillian");
		}

		runningInstances = processEngine
				.getRuntimeService().createProcessInstanceQuery()
				.processDefinitionKey(PROCESS_DEFINITION_KEY_ORDER).list();
		for (ProcessInstance processInstance : runningInstances) {
			processEngine.getRuntimeService().deleteProcessInstance(
					processInstance.getId(),
					"deleted to have a clean environment for Arquillian");
		}
	}
}
