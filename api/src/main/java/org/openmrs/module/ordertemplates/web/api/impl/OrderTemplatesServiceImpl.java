/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.web.api.impl;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ordertemplates.parameter.OrderTemplateCriteria;
import org.springframework.transaction.annotation.Transactional;

import org.openmrs.module.ordertemplates.web.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.web.api.dao.OrderTemplatesDao;
import org.openmrs.module.ordertemplates.web.model.OrderTemplate;

import java.util.List;

@Transactional
public class OrderTemplatesServiceImpl extends BaseOpenmrsService implements OrderTemplatesService {
	
	private OrderTemplatesDao dao;
	
	public void setDao(OrderTemplatesDao dao) {
		this.dao = dao;
	}
	
	@Override
	@Transactional(readOnly = true)
	public OrderTemplate getOrderTemplate(Integer orderTemplateId) {
		return dao.getOrderTemplate(orderTemplateId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public OrderTemplate getOrderTemplateByUuid(String uuid) {
		return dao.getOrderTemplateByUuid(uuid);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrderTemplate> getOrderTemplatesByConcept(Concept concept) {
		return dao.getOrderTemplatesByConcept(concept);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrderTemplate> getOrderTemplatesByDrug(Drug drug) {
		return dao.getOrderTemplatesByDrug(drug);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrderTemplate> getOrderTemplateByCriteria(OrderTemplateCriteria criteria) {
		return dao.getOrderTemplateByCriteria(criteria);
	}
	
	@Override
	public List<OrderTemplate> getAllOrderTemplates(boolean includeRetired) {
		return dao.getAllOrderTemplates(includeRetired);
	}
	
	@Override
	public OrderTemplate saveOrderTemplate(OrderTemplate orderTemplate) {
		return dao.saveOrderTemplate(orderTemplate);
	}
	
	@Override
	public OrderTemplate retireOrderTemplate(OrderTemplate orderTemplate, String reason) {
		orderTemplate.setRetired(true);
		orderTemplate.setRetireReason(reason);
		return dao.saveOrderTemplate(orderTemplate);
	}
	
	@Override
	public OrderTemplate unRetireOrderTemplate(OrderTemplate orderTemplate) {
		orderTemplate.setRetired(false);
		orderTemplate.setRetireReason(null);
		return dao.saveOrderTemplate(orderTemplate);
	}
	
	@Override
	public void purgeOrderTemplate(OrderTemplate orderTemplate) {
		dao.deleteOrderTemplate(orderTemplate);
	}
}
