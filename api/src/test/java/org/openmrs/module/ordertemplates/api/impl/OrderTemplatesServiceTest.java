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
import org.openmrs.module.ordertemplates.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.model.OrderTemplate;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Arthur D. Mugume, Samuel Male date: 22/09/2021
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
	
	@Test
	public void getOrderTemplatesByDrugs_shouldReturnTemplatesForAllGivenDrugs() {
		Drug abacavir = conceptService.getDrug(10055);
		Drug levonorgestrel = conceptService.getDrug(10056);
		Drug paracetamol = conceptService.getDrug(10057);
		List<OrderTemplate> results = orderTemplatesService.getOrderTemplatesByDrugs(Arrays.asList(abacavir, levonorgestrel, paracetamol));
		assertThat(results.size(), is(3));
	}

	@Test
	public void getOrderTemplatesByDrugs_shouldReturnAllTemplatesIncludingRetired() {
		// drug 10059 has one active template, drug 10060 has one retired template
		Drug mebendazole900 = conceptService.getDrug(10059);
		Drug mebendazole600 = conceptService.getDrug(10060);
		List<OrderTemplate> results = orderTemplatesService.getOrderTemplatesByDrugs(Arrays.asList(mebendazole900, mebendazole600));
		assertThat(results.size(), is(2));
	}

	@Test
	public void getOrderTemplatesByDrugUuids_shouldExcludeRetiredWhenFlagIsFalse() {
		List<OrderTemplate> results = orderTemplatesService.getOrderTemplatesByDrugUuids(
		    Arrays.asList("pe2323fa-6fa0-4618-fb59-6765997d844m", "qf2323fa-6fa0-4618-fb59-6765997d844m"), false);
		assertThat(results.size(), is(1));
		assertThat(results.get(0).getName(), is("Mebendazole 900mg template"));
	}

	@Test
	public void getOrderTemplatesByDrugUuids_shouldIncludeRetiredWhenFlagIsTrue() {
		List<OrderTemplate> results = orderTemplatesService.getOrderTemplatesByDrugUuids(
		    Arrays.asList("pe2323fa-6fa0-4618-fb59-6765997d844m", "qf2323fa-6fa0-4618-fb59-6765997d844m"), true);
		assertThat(results.size(), is(2));
	}

	@Test
	public void getOrderTemplatesByCriteria_shouldIncludedRetired() {
		
		OrderTemplateCriteriaBuilder builder = new OrderTemplateCriteriaBuilder();
		List<OrderTemplate> orderTemplates = orderTemplatesService.getOrderTemplateByCriteria(builder.build());
		assertThat(orderTemplates.size(), is(5));
		
		builder.setIncludeRetired(true);
		orderTemplates = orderTemplatesService.getOrderTemplateByCriteria(builder.build());
		assertThat(orderTemplates.size(), is(6));
	}
}
