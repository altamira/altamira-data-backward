package br.com.altamira.bpm.purchase.request.steel;

import java.util.logging.Logger;

import javax.inject.Named;
import javax.mail.Session;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import javax.annotation.Resource;

@Named("NotifyOrderConfirmationPending")
public class NotifyOrderConfirmationPending implements JavaDelegate {
	
	private static final String RESOURCE_NAME = "java:jboss/mail/Default";
	
	@Resource(mappedName = RESOURCE_NAME)
	private Session mailSession;
	
	private final static Logger LOGGER = Logger.getLogger(NotifyOrderConfirmationPending.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		LOGGER.info("A Order Confirmation Pending Mail Notification was sent for Order " + execution.getVariable("orderId"));
		
		/*
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
				.activityInstanceIdIn(execution.getCurrentActivityId())
				.taskDefinitionKey("CheckOutPurchaseOrderUserTask")
				.singleResult();
		
		String assignee = task.getAssignee();
		String taskId = execution.getId();

		if (mailSession == null) {
			LOGGER.warning("Resource injection fail '" + RESOURCE_NAME + "', do it manually by context.lookup.");
			try {
				InitialContext ctx;
				ctx = new InitialContext();
	            mailSession = (Session) ctx.lookup(RESOURCE_NAME);
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
					
		            try    {
		                MimeMessage m = new MimeMessage(mailSession);
		                Address from = new InternetAddress(FROM_USER);
		                Address[] to = new InternetAddress[] {new InternetAddress(recipient) };

		                m.setFrom(from);
		                m.setRecipients(Message.RecipientType.TO, to);
		                m.setSubject("Pedido " + execution.getVariable("orderId") + " Pendente de Confirmacao");
		                m.setSentDate(new java.util.Date());
		                m.setContent("<html><head></head><body><h1>" + task.getName() + "</h1><p>O Pedido " + execution.getVariable("orderId") + " esta pendente de confirmacao de recebimento.<br><br>Para executa-la <a href=\"http://localhost:8080/camunda/app/tasklist/default/#/task/"
								+ taskId + "\">clique aqui</a>.</p></body></html>", "text/html;charset=utf-8");
		                Transport.send(m);
		                LOGGER.log(Level.INFO, "Task Assigned Mail Notify sent!");
		                LOGGER.info("A Order Confirmation Pending Mail Notification of order '" + execution.getVariable("orderId") + "' was successfully sent to user '"
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

		}*/

	}
		
}
