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

import org.openmrs.module.ordertemplates.model.OrderTemplate;

/**
 * This interface defines database methods for the OrderTemplates domain
 */
public interface OrderTemplatesDao {

    /**
     * Gets an OrderTemplate by id
     * @param orderTemplateId the OrderTemplate id
     * @return the OrderTemplate with given id, or null if none exists
     */
    OrderTemplate getOrderTemplate(Integer orderTemplateId);

    /**
     * Gets an OrderTemplate based on the {@code uuid}
     * @param uuid - uuid of the OrderTemplate to be returned
     * @return the OrderTemplate
     */
    OrderTemplate getOrderTemplateByUuid(Integer uuid);

    /**
     * Returns all OrderTemplates in the systems
     * @param includeVoided if false, will limit the results to non-voided templates
     */
    OrderTemplate getAllOrderTemplates(boolean includeVoided);

    /**
     * Saves an instance of an OrderTemplate
     * @param orderTemplate - the OrderTemplate to be saved
     */
    OrderTemplate saveOrderTemplate(OrderTemplate orderTemplate);

    /**
     * Remove an OrderTemplate from the database
     * @param orderTemplate - the OrderTemplate to be purged
     */
    void deleteOrderTemplate(OrderTemplate orderTemplate);
}
