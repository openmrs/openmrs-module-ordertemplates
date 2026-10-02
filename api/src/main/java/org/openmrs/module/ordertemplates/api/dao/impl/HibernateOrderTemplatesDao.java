package org.openmrs.module.ordertemplates.api.dao.impl;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.db.hibernate.HibernateUtil;
import org.openmrs.module.ordertemplates.api.dao.OrderTemplatesDao;
import org.openmrs.module.ordertemplates.parameter.OrderTemplateCriteria;
import org.openmrs.module.ordertemplates.model.OrderTemplate;

import java.util.List;

/**
 * Hibernate implementation of the OrderTemplatesDao
 * 
 * @author Arthur D. Mugume, Samuel Male [UCSF] date: 20/07/2022
 */
public class HibernateOrderTemplatesDao implements OrderTemplatesDao {
	
	private SessionFactory sessionFactory;
	
	@Override
	public OrderTemplate getOrderTemplate(Integer orderTemplateId) {
		return sessionFactory.getCurrentSession().get(OrderTemplate.class, orderTemplateId);
	}
	
	@Override
	public OrderTemplate getOrderTemplateByUuid(String uuid) {
		return sessionFactory.getCurrentSession()
		        .createQuery("select ot from OrderTemplate ot where ot.uuid = :uuid", OrderTemplate.class)
		        .setParameter("uuid", uuid).uniqueResult();
	}
	
	@Override
	public List<OrderTemplate> getOrderTemplatesByDrug(Drug drug) {
		
		if (drug == null) {
			throw new IllegalArgumentException("Drug is required");
		}
		
		if (drug.getDrugId() == null) {
			return sessionFactory.getCurrentSession()
			        .createQuery("select ot from OrderTemplate ot order by ot.orderTemplateId desc", OrderTemplate.class)
			        .list();
		}
		return sessionFactory.getCurrentSession()
		        .createQuery("select ot from OrderTemplate ot where ot.drug = :drug order by ot.orderTemplateId desc",
		            OrderTemplate.class)
		        .setParameter("drug", drug).list();
	}
	
	@Override
	public List<OrderTemplate> getOrderTemplatesByConcept(Concept concept) {
		
		if (concept == null) {
			throw new IllegalArgumentException("Concept is required");
		}
		
		if (concept.getConceptId() == null) {
			return sessionFactory.getCurrentSession()
			        .createQuery("select ot from OrderTemplate ot order by ot.orderTemplateId desc", OrderTemplate.class)
			        .list();
		}
		return sessionFactory.getCurrentSession()
		        .createQuery("select ot from OrderTemplate ot where ot.concept = :concept order by ot.orderTemplateId desc",
		            OrderTemplate.class)
		        .setParameter("concept", concept).list();
	}
	
	@Override
	public List<OrderTemplate> getOrderTemplateByCriteria(OrderTemplateCriteria searchCriteria) {
		
		Concept concept = searchCriteria.getConcept();
		Drug drug = searchCriteria.getDrug();
		boolean filterByDrug = drug != null && drug.getDrugId() != null;
		boolean filterByConcept = concept != null && concept.getConceptId() != null;
		
		StringBuilder hql = new StringBuilder("select ot from OrderTemplate ot where 1 = 1");
		if (filterByDrug) {
			hql.append(" and ot.drug = :drug");
		}
		if (filterByConcept) {
			hql.append(" and ot.concept = :concept");
		}
		if (!searchCriteria.isIncludeRetired()) {
			hql.append(" and ot.retired = false");
		}
		hql.append(" order by ot.orderTemplateId desc");
		
		Query<OrderTemplate> query = sessionFactory.getCurrentSession().createQuery(hql.toString(), OrderTemplate.class);
		if (filterByDrug) {
			query.setParameter("drug", drug);
		}
		if (filterByConcept) {
			query.setParameter("concept", concept);
		}
		return query.list();
	}
	
	@Override
	public List<OrderTemplate> getAllOrderTemplates(boolean includeRetired) {
		if (includeRetired) {
			return sessionFactory.getCurrentSession().createQuery("select ot from OrderTemplate ot", OrderTemplate.class)
			        .list();
		}
		return sessionFactory.getCurrentSession()
		        .createQuery("select ot from OrderTemplate ot where ot.retired = false", OrderTemplate.class).list();
	}
	
	@Override
	public OrderTemplate saveOrderTemplate(OrderTemplate orderTemplate) {
		return HibernateUtil.saveOrUpdate(sessionFactory.getCurrentSession(), orderTemplate);
	}
	
	@Override
	public void deleteOrderTemplate(OrderTemplate orderTemplate) {
		sessionFactory.getCurrentSession().remove(orderTemplate);
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
