package org.openmrs.module.ordertemplates.parameter;

import org.openmrs.Concept;
import org.openmrs.Drug;

/**
 * @author Arthur D. Mugume, Samuel Male [UCSF] date: 19/07/2022
 */
public class OrderTemplateCriteriaBuilder {
	
	private final OrderTemplateCriteria criteria;
	
	public OrderTemplateCriteriaBuilder() {
		criteria = new OrderTemplateCriteria();
	}
	
	public OrderTemplateCriteriaBuilder setConcept(Concept concept) {
		criteria.setConcept(concept);
		return this;
	}
	
	public OrderTemplateCriteriaBuilder setDrug(Drug drug) {
		criteria.setDrug(drug);
		return this;
	}
	
	public OrderTemplateCriteriaBuilder setIncludeRetired(boolean includeRetired) {
		criteria.setIncludeRetired(includeRetired);
		return this;
	}
	
	public OrderTemplateCriteria build() {
		return criteria;
	}
}
