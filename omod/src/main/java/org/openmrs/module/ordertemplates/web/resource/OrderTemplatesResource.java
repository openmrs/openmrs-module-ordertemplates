package org.openmrs.module.ordertemplates.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.ordertemplates.parameter.OrderTemplateCriteriaBuilder;
import org.openmrs.module.ordertemplates.api.OrderTemplatesService;
import org.openmrs.module.ordertemplates.web.controller.OrderTemplatesRestController;
import org.openmrs.module.ordertemplates.model.OrderTemplate;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.*;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

@Resource(name = RestConstants.VERSION_1 + OrderTemplatesRestController.ORDER_TEMPLATES_REST_NAMESPACE + "/orderTemplate", supportedClass = OrderTemplate.class, supportedOpenmrsVersions = { "2.0 - 2.*" })
public class OrderTemplatesResource extends DelegatingCrudResource<OrderTemplate> {
	
	@Override
	public OrderTemplate getByUniqueId(@NotNull String uuid) {
		return getService().getOrderTemplateByUuid(uuid);
	}

	@Override
	protected void delete(OrderTemplate orderTemplate, String retireReason, RequestContext requestContext)
	        throws ResponseException {
		getService().retireOrderTemplate(getService().getOrderTemplateByUuid(orderTemplate.getUuid()), retireReason);
	}
	
	@Override
	public OrderTemplate newDelegate() {
		return new OrderTemplate();
	}
	
	@Override
	public OrderTemplate save(OrderTemplate orderTemplate) {
		return getService().saveOrderTemplate(orderTemplate);
	}
	
	@Override
	public void purge(OrderTemplate orderTemplate, RequestContext requestContext) throws ResponseException {
		getService().purgeOrderTemplate(orderTemplate);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription resourceDescription = new DelegatingResourceDescription();
		if (representation instanceof RefRepresentation) {
			this.addSharedResourceDescriptionProperties(resourceDescription);
			resourceDescription.addProperty("drug", Representation.REF);
			resourceDescription.addProperty("concept", Representation.REF);
			resourceDescription.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		} else if (representation instanceof DefaultRepresentation) {
			this.addSharedResourceDescriptionProperties(resourceDescription);
			resourceDescription.addProperty("drug", Representation.DEFAULT);
			resourceDescription.addProperty("concept", Representation.DEFAULT);
			resourceDescription.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		} else if (representation instanceof FullRepresentation) {
			this.addSharedResourceDescriptionProperties(resourceDescription);
			resourceDescription.addProperty("drug", Representation.FULL);
			resourceDescription.addProperty("concept", Representation.FULL);
		} else if (representation instanceof CustomRepresentation) {
			resourceDescription = null;
		}
		return resourceDescription;
	}

	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl  model = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof RefRepresentation) {
			addSharedModelProperties(model);
			model.property("drug", new RefProperty("#/definitions/DrugGetRef"));
			model.property("concept", new RefProperty("#/definitions/ConceptGetRef"));
		} else if (rep instanceof DefaultRepresentation) {
			addSharedModelProperties(model);
			model.property("drug", new RefProperty("#/definitions/DrugGet"));
			model.property("concept", new RefProperty("#/definitions/ConceptGet"));
		} else if (rep instanceof FullRepresentation) {
			addSharedModelProperties(model);
			model.property("drug", new RefProperty("#/definitions/DrugGetFull"));
			model.property("concept", new RefProperty("#/definitions/ConceptGetFull"));
		} else if (rep instanceof CustomRepresentation) {
			model = null;
		}
		return model;
	}

	private void addSharedModelProperties(ModelImpl model) {
		model.property("uuid", new StringProperty().example("uuid"));
		model.property("display", new StringProperty());
		model.property("name", new StringProperty());
		model.property("description", new StringProperty());
		model.property("template", new StringProperty());
		model.property("retired", new BooleanProperty());
	}

	private void addSharedResourceDescriptionProperties(DelegatingResourceDescription resourceDescription) {
		resourceDescription.addSelfLink();
		resourceDescription.addProperty("uuid");
		resourceDescription.addProperty("display");
		resourceDescription.addProperty("name");
		resourceDescription.addProperty("description");
		resourceDescription.addProperty("template");
		resourceDescription.addProperty("retired");
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription resourceDescription = new DelegatingResourceDescription();
		resourceDescription.addProperty("name");
		resourceDescription.addProperty("description");
		resourceDescription.addProperty("template");
		resourceDescription.addProperty("uuid");
		resourceDescription.addProperty("drug");
		resourceDescription.addProperty("concept");
		resourceDescription.addProperty("retired");
		return resourceDescription;
	}

	@Override
	public Model getCREATEModel(Representation rep) {
		ModelImpl model = (ModelImpl) super.getCREATEModel(rep);
		addSharedModelProperties(model);
		model.property("drug", new RefProperty("#/definitions/DrugCreate"));
		model.property("concept", new RefProperty("#/definitions/ConceptCreate"));
		return model;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return this.getCreatableProperties();
	}

	@Override
	public Model getUPDATEModel(Representation rep) {
		return getCREATEModel(rep);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return new NeedsPaging<OrderTemplate>(getService().getAllOrderTemplates(context.getIncludeAll()), context);
	}
	
	@PropertyGetter("display")
	public String getDisplay(OrderTemplate orderTemplate) {
		return orderTemplate.getName();
	}
	
	private OrderTemplatesService getService() {
		return Context.getService(OrderTemplatesService.class);
	}
	
	@PropertySetter("template")
	public void setTemplate(OrderTemplate instance, Object prop) throws JsonGenerationException, JsonMappingException,
	        IOException {
		if (prop instanceof String) {
			instance.setTemplate((String) prop);
		} else if (prop instanceof Map) {
			instance.setTemplate(new ObjectMapper().writeValueAsString(prop));
		} else {
			instance.setTemplate(prop.toString());
			
		}
	}
	
	@Override
	protected PageableResult doSearch(RequestContext requestContext) {
		Concept concept = null;
		Drug drug = null;
		String drugUuid = requestContext.getParameter("drug");
		String conceptUuid = requestContext.getParameter("concept");
		if (StringUtils.isNotBlank(drugUuid)) {
			drug = Context.getConceptService().getDrugByUuid(drugUuid);
		}
		if (StringUtils.isNotBlank(conceptUuid)) {
			concept = Context.getConceptService().getConceptByUuid(conceptUuid);
		}
		OrderTemplateCriteriaBuilder builder = new OrderTemplateCriteriaBuilder();
		builder.setDrug(drug).setConcept(concept);
		List<OrderTemplate> orderTemplates = getService().getOrderTemplateByCriteria(builder.build());
		return new NeedsPaging(orderTemplates, requestContext);
	}
}
