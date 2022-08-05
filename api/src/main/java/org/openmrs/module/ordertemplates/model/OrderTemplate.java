package org.openmrs.module.ordertemplates.model;

import org.hibernate.annotations.Type;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Concept;
import org.openmrs.Drug;

import javax.persistence.*;

@Entity
@Table(name = "order_template")
public class OrderTemplate extends BaseOpenmrsMetadata {
	
	private static final long serialVersionUID = 1L;
	
	public OrderTemplate() {
	}
	
	public OrderTemplate(Concept concept, Drug drug) {
		this.concept = concept;
		this.drug = drug;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_template_id")
	private Integer orderTemplateId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "concept")
	private Concept concept;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "drug")
	private Drug drug;
	
	@Column(name = "template", length = 10000)
	@Type(type = "text")
	private String template;
	
	@Override
	public Integer getId() {
		return orderTemplateId;
	}
	
	@Override
	public void setId(Integer id) {
		this.orderTemplateId = id;
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
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public Integer getOrderTemplateId() {
		return orderTemplateId;
	}
	
	public void setOrderTemplateId(Integer orderTemplateId) {
		this.orderTemplateId = orderTemplateId;
	}
}
