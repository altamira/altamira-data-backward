package br.com.altamira.data.services;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.altamira.data.model.Request;

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.inject.Inject;

@RunWith(Arquillian.class)
public class RequestEndpointTest {

	@Deployment
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap
				.create(WebArchive.class, "RequestEndpointTest.war")
				.addPackage("br.com.altamira.data.model")
				.addPackage("br.com.altamira.data.dao")
				.addClasses(GenericType.class)
				.addAsResource("META-INF/test-persistence.xml",
						"META-INF/persistence.xml")
				.addAsResource("import.sql")
				.addAsWebInfResource("jbossas-ds.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(war.toString(true));
		return war;
	}

	@Inject
	private Request request;
	
	@Test
	@RunAsClient
	@InSequence(1)
	public void testCreate() throws Exception {

		request.setId(null);
		request.setCreated(new Date());
		request.setCreator("Arquillian Unit Test");
		request.setSent(null);

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		WebTarget target = client
				.target("http://localhost:8080/master-data/rest/request/");

		Response response = target.request("application/json").post(
				Entity.entity(request, MediaType.APPLICATION_JSON));

		// Check the results
		Assert.assertEquals(Response.Status.CREATED.getStatusCode(),
				response.getStatus());

		// Check Entity
		Request entity = response.readEntity(Request.class);

		Assert.assertNotNull(entity.getId());

		request.setId(entity.getId());

		Assert.assertEquals(request, entity);

		// Check json serialize format
		// StringBuilder body = new StringBuilder();

		// body.append("");

		// Assert.assertEquals(body, response.readEntity(String.class));

	}

	@Test
	@RunAsClient
	@InSequence(2)
	public void testFindById() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		WebTarget target = client
				.target("http://localhost:8080/master-data/rest/request/" + request.getId());

		Response response = target.request("application/json").get();
		
		// Check the results
		Assert.assertEquals(Response.Status.OK.getStatusCode(),
				response.getStatus());
		
		// Check Entity
		Request entity = response.readEntity(Request.class);

		Assert.assertEquals(request.getId(), entity.getId());
	}

	@Test
	@RunAsClient
	@InSequence(3)
	// @UsingDataSet("datasets/requests.yml")
	// @ShouldMatchDataSet("datasets/expected-requests.yml")
	public void testListAll() throws Exception {

		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		WebTarget target = client
				.target("http://localhost:8080/master-data/rest/request/");

		Response response = target.request("application/json").get();

		List<Request> entity = response
				.readEntity(new GenericType<List<br.com.altamira.data.model.Request>>() {
				});

		// Check the results
		Assert.assertEquals(Response.Status.OK.getStatusCode(),
				response.getStatus());

		// Check Entity
		Assert.assertNotNull(entity.isEmpty());
		Assert.assertEquals(request.getId(), entity.get(0).getId());

		// Check Json Serialize
		String body = "[{\"id\":1,\"created\":0359372577941,\"creator\":\"Helio.Toda\",\"sent\":\"\"},{\"id\":2,\"created\":1359372577941,\"creator\":\"Helio.Toda\",\"sent\":\"\"},{\"id\":3,\"created\":1359372577941,\"creator\":\"Helio.Toda\",\"sent\":\"\"},{\"id\":4,\"created\":1359372577941,\"creator\":\"Helio.Toda\",\"sent\":\"\"},{\"id\":5,\"created\":1359372577941,\"creator\":\"Helio.Toda\",\"sent\":\"\"}]";
		Assert.assertEquals(body, response.readEntity(String.class));

	}

	@Test
	@RunAsClient
	@InSequence(4)
	public void testUpdate() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		WebTarget target = client
				.target("http://localhost:8080/master-data/rest/request/");

		Response response = target.request("application/json").put(Entity.entity(request,
				MediaType.APPLICATION_JSON));
		Request entity = response.readEntity(Request.class);

		// Check the results
		Assert.assertEquals(Response.Status.OK.getStatusCode(),
				response.getStatus());
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals(request.getId(), entity.getId());

	}

	@Test
	@RunAsClient
	@InSequence(5)
	public void testDeleteById() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		WebTarget target = client
				.target("http://localhost:8080/master-data/rest/request/");

		Response response = target.request("application/json").delete();

		// Check the results
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(),
				response.getStatus());

		WebTarget checkDelete = client.target(
				"http://localhost:8080/master-data/rest/request/"
						+ request.getId());

		Response checkResponse = checkDelete.request("application/json").get();
		
		Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
				checkResponse.getStatus());

	}

	// @Test
	/*
	 * public void testGetCurrent() throws Exception {
	 * 
	 * // Do the test ClientRequest request = new
	 * ClientRequest("http://localhost:8080/master-data/rest/request/current");
	 * request.accept(MediaType.APPLICATION_JSON);
	 * 
	 * ClientResponse<Request> response = request.get(Request.class); Request
	 * requestRES = response.getEntity();
	 * 
	 * // Check the results
	 * Assert.assertEquals(Response.Status.OK.getStatusCode(),
	 * response.getStatus()); Assert.assertNotNull(requestRES.getId());
	 * 
	 * }
	 */
}
