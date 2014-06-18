package br.com.altamira.data.services;

import br.com.altamira.data.model.Material;
import br.com.altamira.data.model.Request;
import br.com.altamira.data.model.RequestItem;
import br.com.altamira.data.dao.RequestDao;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.inject.Inject;
import javax.inject.Named;

@RunWith(Arquillian.class)
public class RequestEndpointTest {

	@Inject 
	@Named("createInstace")
	Request requestTest;

	@Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Request.class, RequestItem.class, Material.class, RequestDao.class)
            .addClasses(GenericType.class, Asset.class)
            .addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
            .addAsResource("META-INF/jbossas-ds.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
	 

	@Named
	@Produces
	public Request createInstace() {

		Request request = new Request();

		request.setCreated(DateTime.now().toDate());
		request.setCreator("Arquillian Unit Test");
		request.setSent(null);

		return request;
	}

	@Test
	@InSequence(1)
	public void testCreate() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		Invocation.Builder builder = client.target(
				"http://localhost:8080/master-data/rest/request/").request(
				"application/json");

		builder.accept(MediaType.APPLICATION_JSON);
		builder.header("Content-Type", MediaType.APPLICATION_JSON);

		Response response = builder.post(Entity.entity(requestTest,
				MediaType.APPLICATION_JSON));

		// Check the results
		Assert.assertEquals(Response.Status.CREATED.getStatusCode(),
				response.getStatus());

		// Check Entity
		Request entity = response.readEntity(Request.class);

		Assert.assertNotNull(entity.getId());

		requestTest.setId(entity.getId());
		
		Assert.assertEquals(requestTest, entity);
		
		// Check Json serialize format
		
		StringBuilder body = new StringBuilder();
		
		body.append("");
		
		Assert.assertEquals(body, response.readEntity(String.class));

	}

	@Test
	@InSequence(2)
	public void testFindById() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		Invocation.Builder builder = client.target(
				"http://localhost:8080/master-data/rest/request/"
						+ requestTest.getId().toString()).request(
				"application/json");

		Response response = builder.get();
		Request entity = response.readEntity(Request.class);

		// Check the results
		Assert.assertEquals(Response.Status.OK.getStatusCode(),
				response.getStatus());
		Assert.assertEquals(requestTest.getId(), entity.getId());
	}

	@Test
	@InSequence(3)
	public void testListAll() throws Exception {

		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		Invocation.Builder builder = client.target(
				"http://localhost:8080/master-data/rest/request?start="
						+ requestTest.getId() + "&max=1").request(
				"application/json");

		Response response = builder.get();

		List<Request> entity = response
				.readEntity(new GenericType<List<br.com.altamira.data.model.Request>>() {
				});

		// Check the results
		Assert.assertEquals(Response.Status.OK.getStatusCode(),
				response.getStatus());
		Assert.assertNotNull(entity.isEmpty());
		Assert.assertEquals(requestTest.getId(), entity.get(0).getId());

	}

	@Test
	@InSequence(4)
	public void testUpdate() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		Invocation.Builder builder = client.target(
				"http://localhost:8080/master-data/rest/request/").request(
				"application/json");

		builder.accept(MediaType.APPLICATION_JSON);
		builder.header("Content-Type", MediaType.APPLICATION_JSON);

		Response response = builder.put(Entity.entity(requestTest,
				MediaType.APPLICATION_JSON));
		Request entity = response.readEntity(Request.class);

		// Check the results
		Assert.assertEquals(Response.Status.OK.getStatusCode(),
				response.getStatus());
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals(requestTest.getId(), entity.getId());

	}

	@Test
	@InSequence(5)
	public void testDeleteById() throws Exception {

		// Do the test
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeatures.class);
		Invocation.Builder builder = client.target(
				"http://localhost:8080/master-data/rest/request/" + requestTest.getId())
				.request("application/json");
		
		Response response = builder.delete();

		// Check the results
		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(),
				response.getStatus());

		Invocation.Builder checkDelete = client.target(
				"http://localhost:8080/master-data/rest/request/" + requestTest.getId())
				.request("application/json");

		Response checkResponse = checkDelete.get();
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
