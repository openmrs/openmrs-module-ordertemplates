package org.openmrs.module.ordertemplates.web.resource;

import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
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
	public Schema<?> getGETSchema(Representation rep) {
		Schema<?> model = super.getGETSchema(rep);
		if (rep instanceof RefRepresentation) {
			addSharedModelProperties(model);
			model.addProperty("drug", new Schema<Drug>().$ref("#/components/schemas/DrugGetRef")).addProperty("concept",
			    new Schema<Concept>().$ref("#/components/schemas/ConceptGetRef"));
		} else if (rep instanceof DefaultRepresentation) {
			addSharedModelProperties(model);
			model.addProperty("drug", new Schema<Drug>().$ref("#/components/schemas/DrugGet")).addProperty("concept",
			    new Schema<Concept>().$ref("#/components/schemas/ConceptGet"));
		} else if (rep instanceof FullRepresentation) {
			addSharedModelProperties(model);
			model.addProperty("drug", new Schema<Drug>().$ref("#/components/schemas/DrugGetFull")).addProperty("concept",
			    new Schema<Concept>().$ref("#/components/schemas/ConceptGetFull"));
		} else if (rep instanceof CustomRepresentation) {
			model = null;
		}
		return model;
	}
	
	private void addSharedModelProperties(Schema<?> model) {
		model.addProperty("uuid", new StringSchema().example("uuid")).addProperty("display", new StringSchema())
		        .addProperty("name", new StringSchema()).addProperty("description", new StringSchema())
		        .addProperty("template", new StringSchema()).addProperty("retired", new BooleanSchema());
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
	public Schema<?> getCREATESchema(Representation rep) {
		Schema<?> model = super.getCREATESchema(rep);
		addSharedModelProperties(model);
		model.addProperty("drug", new Schema<Drug>().$ref("#/components/schemas/DrugCreate")).addProperty("concept",
		    new Schema<Concept>().$ref("#/components/schemas/ConceptCreate"));
		return model;
	}

	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		return this.getCreatableProperties();
	}
	
	@Override
	public Schema<?> getUPDATESchema(Representation rep) {
		return getCREATESchema(rep);
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
