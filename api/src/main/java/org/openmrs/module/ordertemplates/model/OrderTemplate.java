package org.openmrs.module.ordertemplates.model;

import org.openmrs.BaseOpenmrsMetadata;

import javax.persistence.*;

@Entity
@Table(name = "order_template")
public class OrderTemplate extends BaseOpenmrsMetadata {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_template_id")
	private Integer orderTemplateId;
	
	@Override
	public Integer getId() {
		return orderTemplateId;
	}
	
	@Override
	public void setId(Integer id) {
		this.orderTemplateId = id;
	}
}
