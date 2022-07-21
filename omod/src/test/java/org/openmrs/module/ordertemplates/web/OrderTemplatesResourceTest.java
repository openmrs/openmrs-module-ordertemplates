/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ordertemplates.web.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.web.model.OrderTemplate;
import org.openmrs.module.ordertemplates.web.resource.OrderTemplatesResource;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestUtil;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class, RestUtil.class })
public class OrderTemplatesResourceTest {
	
	@Mock
	OrderTemplatesService service;
	
	@Mock
	ConceptService conceptService;
	
	@Mock
	AdministrationService administrationService;
	
	@Mock
	RestService restService;
	
	private OrderTemplatesResourceTestWrapper resource;
	
	ObjectMapper jackson = new ObjectMapper();
	
	@Before
	public void setup() throws Exception {
		resource = new OrderTemplatesResourceTestWrapper();
		PowerMockito.mockStatic(RestUtil.class);
		PowerMockito.mockStatic(Context.class);
		when(Context.isAuthenticated()).thenReturn(true);
		when(Context.getService(OrderTemplatesService.class)).thenReturn(service);
		when(Context.getConceptService()).thenReturn(conceptService);
		when(Context.getAdministrationService()).thenReturn(administrationService);
		when(Context.getService(RestService.class)).thenReturn(restService);
		
		when(restService.getResourceBySupportedClass(OrderTemplate.class)).thenReturn(resource);
	}
	
	@Ignore
	public void testCreateOrderTemplate() throws IOException {
		// setup
		String json = "{" + "\"name\": \"Abacavir/dolutegravir/lamivudine template\","
		        + "\"concept\": \"65f83cd4-64e3-4en7-a5f3-364d3b14a634\","
		        + "\"drug\": \"pe2323fa-6fa0-4618-fb59-6765997d844m\"," + "\"template\": {"
		        + "\"dosingType\": \"org.openmrs.SimpleDosingInstructions\"," + "\"instructions\": {" + "\"dose\": 1,"
		        + "\"doseUnits\": \"1513AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
		        + "\"route\": \"160240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
		        + "\"frequency\": \"160862AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"" + "}" + "}}";
		SimpleObject payLoad = jackson.readValue(json, SimpleObject.class);
		
		Concept concept1 = new Concept(1);
		concept1.setUuid("65f83cd4-64e3-4en7-a5f3-364d3b14a634");
		Drug drug1 = new Drug(1);
		drug1.setConcept(concept1);
		drug1.setUuid("65f83cd4-64e3-4en7-a5f3-364d3b14a634");
		OrderTemplate expected = new OrderTemplate(concept1, drug1);
		expected.setName("Abacavir/dolutegravir/lamivudine template");
		expected.setTemplate(payLoad.get("template").toString());
		when(conceptService.getConcept("65f83cd4-64e3-4en7-a5f3-364d3b14a634")).thenReturn(new Concept(2));
		when(conceptService.getDrug("65f83cd4-64e3-4en7-a5f3-364d3b14a634")).thenReturn(new Drug(2));
		when(service.saveOrderTemplate(any(OrderTemplate.class))).thenReturn(expected);
		
		// replay
		OrderTemplate created = (OrderTemplate) resource.create(payLoad, new RequestContext());
		
		Assert.assertThat(created, is(expected));
	}
	
	@Test
	public void testGetOrderTemplate() {
		
	}
	
	@Test
	public void testGetOne() {
		
	}
	
	/**
	 * This is a small wrapper class to the Resource. Its necessary because for some reasons, there
	 * no appropriate Converters of Concept and Drug resources in the test environment.
	 */
	public class OrderTemplatesResourceTestWrapper extends OrderTemplatesResource {
		
		@PropertySetter("drug")
		public void setDrug(OrderTemplate instance, String uuid) {
			Drug existing = Context.getConceptService().getDrug(uuid);
			instance.setDrug(existing);
		}
		
		@PropertySetter("concept")
		public void setConcept(OrderTemplate instance, String uuid) {
			Concept existing = Context.getConceptService().getConcept(uuid);
			instance.setConcept(existing);
		}
	}
	
}
