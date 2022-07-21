/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.api.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.module.ordertemplates.parameter.OrderTemplateCriteriaBuilder;
import org.openmrs.module.ordertemplates.web.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.web.model.OrderTemplate;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * @author smallGod date: 22/09/2021
 */
public class OrderTemplatesServiceTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	OrderTemplatesService orderTemplatesService;
	
	@Autowired
	ConceptService conceptService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	EncounterService encounterService;
	
	@Before
	public void setup() throws Exception {
		executeDataSet("testdata/OrderTemplateServiceTest-initialData.xml");
		updateSearchIndex();
	}
	
	@Test
	public void getOrderTemplatesByCriteria_shouldGetByConcept() {
		
		OrderTemplateCriteriaBuilder builder = new OrderTemplateCriteriaBuilder();
		builder.setConcept(conceptService.getConcept(100011));
		List<OrderTemplate> orderTemplates = orderTemplatesService.getOrderTemplateByCriteria(builder.build());
		assertThat(orderTemplates.size(), is(1));
		
		builder.setConcept(conceptService.getConcept(100013));
		orderTemplates = orderTemplatesService.getOrderTemplateByCriteria(builder.build());
		assertThat(orderTemplates.size(), is(2));
	}
	
	@Test
	public void getOrderTemplatesByConcept_shouldGetByConcept() {
		
		Concept concept = conceptService.getConcept(100011);
		List<OrderTemplate> orderTemplates = orderTemplatesService.getOrderTemplatesByConcept(concept);
		assertThat(orderTemplates.size(), is(1));
	}
	
	@Test
	public void getOrderTemplatesByCriteria_shouldGetByDrug() {
		
		OrderTemplateCriteriaBuilder builder = new OrderTemplateCriteriaBuilder();
		builder.setDrug(conceptService.getDrug(10055));
		List<OrderTemplate> orderTemplates = orderTemplatesService.getOrderTemplateByCriteria(builder.build());
		assertThat(orderTemplates.size(), is(1));
	}
	
	@Test
	public void getOrderTemplatesByDrug_shouldGetByDrug() {
		
		Drug drug = conceptService.getDrug(10055);
		List<OrderTemplate> orderTemplates = orderTemplatesService.getOrderTemplatesByDrug(drug);
		assertThat(orderTemplates.size(), is(1));
	}
}
