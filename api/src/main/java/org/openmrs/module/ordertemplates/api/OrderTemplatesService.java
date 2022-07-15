/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.ordertemplates.OrderTemplatesConstants;
import org.openmrs.module.ordertemplates.model.OrderTemplate;

/**
 * This interface defines an API of interacting with {@link OrderTemplate} objects
 */
public interface OrderTemplatesService extends OpenmrsService {
	
	/**
	 * Gets an OrderTemplate by id
	 * 
	 * @param orderTemplateId the OrderTemplate id
	 * @return the OrderTemplate with given id, or null if none exists
	 */
	@Authorized({ OrderTemplatesConstants.MANAGE_ORDER_TEMPLATES })
	OrderTemplate getOrderTemplate(Integer orderTemplateId);
	
	/**
	 * Gets an OrderTemplate based on the {@code uuid}
	 * 
	 * @param uuid - uuid of the OrderTemplate to be returned
	 * @return the OrderTemplate
	 */
	@Authorized({ OrderTemplatesConstants.MANAGE_ORDER_TEMPLATES })
	OrderTemplate getOrderTemplateByUuid(String uuid);
	
	/**
	 * Saves an instance of an OrderTemplate
	 * 
	 * @param orderTemplate - the OrderTemplate to be saved
	 */
	@Authorized({ OrderTemplatesConstants.MANAGE_ORDER_TEMPLATES })
	OrderTemplate saveOrderTemplate(OrderTemplate orderTemplate);
	
	/**
	 * Retires a OrderTemplate
	 * 
	 * @param orderTemplate - OrderTemplate to be retired
	 * @param reason - the reason for voiding the OrderTemplate
	 */
	@Authorized({ OrderTemplatesConstants.MANAGE_ORDER_TEMPLATES })
	OrderTemplate retireOrderTemplate(OrderTemplate orderTemplate, String reason);
	
	/**
	 * Un-retire a previously retired OrderTemplate
	 * 
	 * @param orderTemplate - OrderTemplate to be un-retired
	 */
	@Authorized({ OrderTemplatesConstants.MANAGE_ORDER_TEMPLATES })
	OrderTemplate unRetireOrderTemplate(OrderTemplate orderTemplate);
	
	/**
	 * Completely remove an OrderTemplate from the persistent storage. This should be called with
	 * caution because we don't want to lose data. For most cases the data <i>should</i> just be
	 * voided (see #retireOrderTemplate(orderTemplate, reason))
	 * 
	 * @param orderTemplate - the OrderTemplate to be purged
	 */
	@Authorized({ OrderTemplatesConstants.MANAGE_ORDER_TEMPLATES })
	void purgeOrderTemplate(OrderTemplate orderTemplate);
}
