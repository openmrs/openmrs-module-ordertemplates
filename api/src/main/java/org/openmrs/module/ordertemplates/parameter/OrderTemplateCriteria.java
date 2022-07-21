/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.ordertemplates.parameter;

import org.openmrs.Concept;
import org.openmrs.Drug;

/**
 * @author Arthur D. Mugume, Samuel Male [UCSF] date: 19/07/2022
 */
public class OrderTemplateCriteria {
	
	private Concept concept;
	
	private Drug drug;
	
	private boolean includeRetired = false;
	
	public OrderTemplateCriteria() {
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	
	public Drug getDrug() {
		return drug;
	}
	
	public void setDrug(Drug drug) {
		this.drug = drug;
	}
	
	public boolean isIncludeRetired() {
		return includeRetired;
	}
	
	public void setIncludeRetired(boolean includeRetired) {
		this.includeRetired = includeRetired;
	}
}
