package org.openmrs.module.ordertemplates.web.controller;

import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 + OrderTemplatesRestController.ORDER_TEMPLATES_REST_NAMESPACE)
public class OrderTemplatesRestController extends MainResourceController {
	
	public static final String ORDER_TEMPLATES_REST_NAMESPACE = "/ordertemplates";
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return RestConstants.VERSION_1 + ORDER_TEMPLATES_REST_NAMESPACE;
	}
}
