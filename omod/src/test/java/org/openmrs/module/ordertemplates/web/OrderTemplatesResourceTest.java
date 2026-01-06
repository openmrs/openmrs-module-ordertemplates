package org.openmrs.module.ordertemplates.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.ordertemplates.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.model.OrderTemplate;
import org.openmrs.module.ordertemplates.web.resource.OrderTemplatesResource;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderTemplatesResourceTest extends BaseModuleWebContextSensitiveTest {
	
	@Autowired
	OrderTemplatesService service;
	
	ObjectMapper jackson = new ObjectMapper();
	
	OrderTemplatesResource resource;
	
	@Before
	public void setup() throws Exception {
		executeDataSet("testdata/OrderTemplateServiceTest-initialData.xml");
		resource = new OrderTemplatesResource();
	}
	
	@Test
	public void testCreateOrderTemplate() throws IOException {
		// setup
		String json = "{" + "\"name\": \"Abacavir/dolutegravir/lamivudine template\","
		        + "\"uuid\": \"35f83cd4-64ev-4en7-a5f3-364d3b14a63m\","
		        + "\"concept\": \"65f83cd4-64e3-4en7-a5f3-364d3b14a634\","
		        + "\"drug\": \"pe2323fa-6fa0-4618-fb59-6765997d844m\"," + "\"template\": {"
		        + "\"dosingType\": \"org.openmrs.SimpleDosingInstructions\"," + "\"instructions\": {" + "\"dose\": 1,"
		        + "\"doseUnits\": \"1513AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
		        + "\"route\": \"160240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
		        + "\"frequency\": \"160862AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"" + "}" + "}}";
		SimpleObject payLoad = jackson.readValue(json, SimpleObject.class);
		String templateUuid = "35f83cd4-64ev-4en7-a5f3-364d3b14a63m";
		assertThat(service.getOrderTemplateByUuid(templateUuid), nullValue());
		
		// replay and verify
		SimpleObject created = (SimpleObject) resource.create(payLoad, new RequestContext());
		assertThat(created, notNullValue());
		OrderTemplate wasCreated = service.getOrderTemplateByUuid(templateUuid);
		assertThat(wasCreated, notNullValue());
		assertThat(wasCreated.getId(), notNullValue());
		assertThat(wasCreated.getName(), is("Abacavir/dolutegravir/lamivudine template"));
	}
	
	@Test
	public void testRetrieveOne() {
		OrderTemplate existing = resource.getByUniqueId("01b8f6b7-dc0e-4346-b818-f3e9cd24dfdb");
		assertThat(existing, notNullValue());
		assertThat(existing.getName(), is("Abacavir 300mg template"));
	}
}
