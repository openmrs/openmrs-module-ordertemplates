/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.api.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.module.ordertemplates.model.OrderTemplate;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

/**
 * Unit tests for the OrderTemplatesDao
 * 
 * @author Arthur D. Mugume, Samuel Male [UCSF] date: 19/07/2022
 */
public class OrderTemplatesDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	OrderTemplatesDao orderTemplatesDao;
	
	private static ObjectMapper jsonPrinter = new ObjectMapper();
	
	private static final String tempalte1 = "{"
	        + "                          \"dosingType\": \"org.openmrs.SimpleDosingInstructions\","
	        + "                          \"instructions\": {" + "                            \"dose\": 300,"
	        + "                            \"doseUnits\": \"161553AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
	        + "                            \"route\": \"160240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
	        + "                            \"frequency\": \"160858AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
	        + "                          }" + "                        }";
	
	@Before
	public void setup() throws Exception {
		executeDataSet("testdata/OrderTemplateServiceTest-initialData.xml");
		updateSearchIndex();
	}
	
	@Test
	public void getOrderTemplate_shouldGetOrderTemplateById() throws Exception {
		OrderTemplate existing = orderTemplatesDao.getOrderTemplate(1);
		testOrderTemplate1(existing);
	}
	
	@Test
	public void getOrderTemplateByUuid_shouldGetOrderTemplateByUuid() throws Exception {
		OrderTemplate existing = orderTemplatesDao.getOrderTemplateByUuid("01b8f6b7-dc0e-4346-b818-f3e9cd24dfdb");
		testOrderTemplate1(existing);
	}
	
	@Test
	public void getAllOrderTemplates_shouldAllOrderTemplates() throws Exception {
		List<OrderTemplate> existingTemplates = orderTemplatesDao.getAllOrderTemplates(true);
		assertThat(existingTemplates.size(), is(6));
		testOrderTemplate1(existingTemplates.get(0));
		assertThat(existingTemplates.get(1).getName(), is("Levonorgestrel 1.5mg template"));
		assertThat(existingTemplates.get(2).getName(), is("Paracetamol 500mg template"));
		assertThat(existingTemplates.get(5).getRetired(), is(true));
	}
	
	@Test
	public void getAllOrderTemplates_shouldGetAllUnRetiredOrderTemplates() throws Exception {
		List<OrderTemplate> existingTemplates = orderTemplatesDao.getAllOrderTemplates(false);
		assertThat(existingTemplates.size(), is(5));
		testOrderTemplate1(existingTemplates.get(0));
		assertThat(existingTemplates.get(1).getName(), is("Levonorgestrel 1.5mg template"));
	}
	
	@Test
	public void saveOrderTemplate_shouldSaveNewTemplate() {
		// Setup
		final String template = "{" + "  dosingType: \"org.openmrs.SimpleDosingInstructions\"," + "  instructions: {"
		        + "    dose: 1," + "    doseUnits: \"1513AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
		        + "    route: \"160240AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\","
		        + "    frequency: \"160862AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"," + "  }" + "}";
		OrderTemplate incoming = new OrderTemplate(new Concept(100014), new Drug(10058));
		incoming.setTemplate(template);
		incoming.setName("Abacavir/dolutegravir/lamivudine template");
		
		// Replay
		OrderTemplate existing = orderTemplatesDao.saveOrderTemplate(incoming);
		Assert.assertNotNull(existing.getId());
	}
	
	@Test
	public void saveOrderTemplate_shouldUpdateExistingTemplate() {
		OrderTemplate existing = orderTemplatesDao.getOrderTemplate(2);
		assertThat(existing.getDescription(), isEmptyOrNullString());
		existing.setDescription("Levonorgestrel order template");
		OrderTemplate updated = orderTemplatesDao.saveOrderTemplate(existing);
		assertThat(updated.getDescription(), is("Levonorgestrel order template"));
	}
	
	@Test
	public void deleteOrderTemplate_shouldRemoveFromDB() {
		OrderTemplate existing = orderTemplatesDao.getOrderTemplate(1);
		testOrderTemplate1(existing);
		orderTemplatesDao.deleteOrderTemplate(existing);
		existing = orderTemplatesDao.getOrderTemplate(1);
		Assert.assertNull(existing);
	}
	
	private static void testOrderTemplate1(OrderTemplate existing) {
		Assert.assertNotNull(existing);
		assertThat(existing.getUuid(), is("01b8f6b7-dc0e-4346-b818-f3e9cd24dfdb"));
		assertThat(existing.getName(), is("Abacavir 300mg template"));
		assertThat(existing.getDrug().getId(), is(10055));
		assertThat(existing.getConcept().getId(), is(100011));
		assertThat(existing.getTemplate().replaceAll("\\s+", ""), is(tempalte1.replaceAll("\\s+", "")));
	}
}
