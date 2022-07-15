package org.openmrs.module.ordertemplates.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.ordertemplates.api.dao.OrderTemplatesDao;
import org.openmrs.module.ordertemplates.model.OrderTemplate;

import java.util.List;

/**
 * Hibernate implementation of the OrderTemplatesDao
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
