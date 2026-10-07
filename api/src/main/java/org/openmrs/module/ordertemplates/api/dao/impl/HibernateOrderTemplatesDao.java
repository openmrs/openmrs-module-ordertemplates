package org.openmrs.module.ordertemplates.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.ordertemplates.api.dao.OrderTemplatesDao;
import org.openmrs.module.ordertemplates.parameter.OrderTemplateCriteria;
import org.openmrs.module.ordertemplates.model.OrderTemplate;

import java.util.List;
import java.util.Objects;

/**
 * Hibernate implementation of the OrderTemplatesDao
 * 
 * @author Arthur D. Mugume, Samuel Male [UCSF] date: 20/07/2022
 */
public class HibernateOrderTemplatesDao implements OrderTemplatesDao {
	
	private DbSessionFactory sessionFactory;
	
	@Override
	public OrderTemplate getOrderTemplate(Integer orderTemplateId) {
		return (OrderTemplate) sessionFactory.getCurrentSession().get(OrderTemplate.class, orderTemplateId);
	}
	
	@Override
	public OrderTemplate getOrderTemplateByUuid(String uuid) {
		return (OrderTemplate) sessionFactory.getCurrentSession()
		        .createQuery("select ot from OrderTemplate ot where ot.uuid = :uuid").setParameter("uuid", uuid)
		        .uniqueResult();
	}
	
	@Override
	public List<OrderTemplate> getOrderTemplatesByDrug(Drug drug) {

		if (drug == null) {
			throw new IllegalArgumentException("Drug is required");
		}

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrderTemplate.class);
		if (drug.getDrugId() != null) {
			criteria.add(Restrictions.eq("drug", drug));
		}
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderTemplateId"));
		return criteria.list();
	}

	@Override
	public List<OrderTemplate> getOrderTemplatesByDrugs(List<Drug> drugs) {

		if (drugs == null || drugs.isEmpty()) {
			throw new IllegalArgumentException("At least one drug is required");
		}
		if (drugs.stream().anyMatch(Objects::isNull)) {
			throw new IllegalArgumentException("Drug list must not contain null elements");
		}

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrderTemplate.class);
		criteria.add(Restrictions.in("drug", drugs));
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderTemplateId"));
		return criteria.list();
	}

	@Override
	public List<OrderTemplate> getOrderTemplatesByDrugUuids(List<String> uuids, boolean includeRetired) {

		if (uuids == null || uuids.isEmpty()) {
			throw new IllegalArgumentException("At least one drug UUID is required");
		}
		if (uuids.stream().anyMatch(u -> u == null || u.trim().isEmpty())) {
			throw new IllegalArgumentException("UUID list must not contain null or blank entries");
		}

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrderTemplate.class);
		criteria.createAlias("drug", "d");
		criteria.add(Restrictions.in("d.uuid", uuids));
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderTemplateId"));
		return criteria.list();
	}
	
	@Override
	public List<OrderTemplate> getOrderTemplatesByConcept(Concept concept) {
		
		if (concept == null) {
			throw new IllegalArgumentException("Concept is required");
		}
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrderTemplate.class);
		if (concept.getConceptId() != null) {
			criteria.add(Restrictions.eq("concept", concept));
		}
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderTemplateId"));
		return criteria.list();
	}
	
	@Override
	public List<OrderTemplate> getOrderTemplateByCriteria(OrderTemplateCriteria searchCriteria) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrderTemplate.class);
		Concept concept = searchCriteria.getConcept();
		Drug drug = searchCriteria.getDrug();
		
		if (drug != null && drug.getDrugId() != null) {
			criteria.add(Restrictions.eq("drug", drug));
		}
		if (concept != null && concept.getConceptId() != null) {
			criteria.add(Restrictions.eq("concept", concept));
		}
		if (!searchCriteria.isIncludeRetired()) {
			criteria.add(Restrictions.eq("retired", false));
		}
		
		criteria.addOrder(org.hibernate.criterion.Order.desc("orderTemplateId"));
		return criteria.list();
	}
	
	@Override
	public List<OrderTemplate> getAllOrderTemplates(boolean includeRetired) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrderTemplate.class);
		if (!includeRetired) {
			criteria.add(Restrictions.eq("retired", false));
		}
		return criteria.list();
	}
	
	@Override
	public OrderTemplate saveOrderTemplate(OrderTemplate orderTemplate) {
		sessionFactory.getCurrentSession().saveOrUpdate(orderTemplate);
		return orderTemplate;
	}
	
	@Override
	public void deleteOrderTemplate(OrderTemplate orderTemplate) {
		sessionFactory.getCurrentSession().delete(orderTemplate);
	}
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
