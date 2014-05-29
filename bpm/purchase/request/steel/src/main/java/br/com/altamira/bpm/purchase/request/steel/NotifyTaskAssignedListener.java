package br.com.altamira.bpm.purchase.request.steel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.annotation.Resource;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;

public class NotifyTaskAssignedListener implements TaskListener {
	
	//@Resource(lookup = "java:jboss/mail/Default")
	@Resource(name = "java:jboss/mail/Default")
	private Session mailSession;
	
	// TODO: Set Mail Server Properties
	private static final String HOST =  "mail.example.org";
	private static final String USER = "admin@example.org";
	private static final String PWD = "toomanysecrets";

	private final static Logger LOGGER = Logger.getLogger(NotifyTaskAssignedListener.class.getName());

	public void notify(DelegateTask delegateTask) {

		String assignee = delegateTask.getAssignee();
		String taskId = delegateTask.getId();

		if (mailSession == null) {
			LOGGER.log(Level.WARNING, "RESOURCE INJECTION FAIL, DO IT MANUAL : " + "java:jboss/mail/Default");
			try {
				InitialContext ctx;
				ctx = new InitialContext();
	            mailSession = (Session) ctx.lookup("java:jboss/mail/Default");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (assignee != null) {

			// Get User Profile from User Management
			IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
			User user = identityService.createUserQuery().userId(assignee).singleResult();

			if (user != null) {

				// Get Email Address from User Profile
				String recipient = user.getEmail();

				if (recipient != null && !recipient.isEmpty()) {

					/*Email email = new SimpleEmail();
					email.setHostName(HOST);
					email.setAuthentication(USER, PWD);

					try {
						email.setFrom("noreply@camunda.org");
						email.setSubject("Task assigned: "
								+ delegateTask.getName());
						email.setMsg("Please complete: http://localhost:8080/camunda/app/tasklist/default/#/task/"
								+ taskId);

						email.addTo(recipient);

						email.send();
						LOGGER.info("Task Assignment Email successfully sent to user '"
								+ assignee
								+ "' with address '"
								+ recipient
								+ "'.");

					} catch (Exception e) {
						LOGGER.log(Level.WARNING,
								"Could not send email to assignee", e);
					}*/
					
		            try    {
		                MimeMessage m = new MimeMessage(mailSession);
		                Address from = new InternetAddress(USER);
		                Address[] to = new InternetAddress[] {new InternetAddress(user.getEmail()) };

		                m.setFrom(from);
		                m.setRecipients(Message.RecipientType.TO, to);
		                m.setSubject(delegateTask.getName());
		                m.setSentDate(new java.util.Date());
		                m.setContent("<html><head></head><body><h1>" + delegateTask.getName() + "</h1><p>Uma nova tarefa foi atribuida a você.<br><br>Para executa-la <a href=\"http://localhost:8080/camunda/app/tasklist/default/#/task/"
								+ taskId + "\">clique aqui</a>.</p></body></html>", "text/html;charset=utf-8");
		                Transport.send(m);
		                LOGGER.log(Level.INFO, "Task Assigned Mail Notify sent!");
		                LOGGER.info("A Task Assignment Mail Notify of '" + delegateTask.getName() + "' was successfully sent to user '"
								+ assignee
								+ "' with address '"
								+ recipient
								+ "'.");
		            }
		            catch (javax.mail.MessagingException e)
		            {
		                e.printStackTrace();
		                LOGGER.log(Level.WARNING, "Error in Sending Mail: "+e);
		            }					

				} else {
					LOGGER.warning("Not sending email to user " + assignee
							+ "', user has no email address.");
				}

			} else {
				LOGGER.warning("Not sending email to user " + assignee
						+ "', user is not enrolled with identity service.");
			}

		}

	}

}
