/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.api.impl;

import org.openmrs.api.impl.AdministrationServiceImpl;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.ordertemplates.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.api.dao.OrderTemplatesDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;

@Transactional
public class OrderTemplatesServiceImpl extends BaseOpenmrsService implements OrderTemplatesService {
	
	OrderTemplatesDao dao;
	
	private static final Logger log = LoggerFactory.getLogger(AdministrationServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(OrderTemplatesDao dao) {
		this.dao = dao;
	}
}
