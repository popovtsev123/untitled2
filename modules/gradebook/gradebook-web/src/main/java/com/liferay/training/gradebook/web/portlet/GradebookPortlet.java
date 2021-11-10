package com.liferay.training.gradebook.web.portlet;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import javax.portlet.Portlet;
import org.osgi.service.component.annotations.Component;
/**
 * @author liferay
 */
@Component(
		immediate = true,
		property = {
				"com.liferay.portlet.display-category=category.training",
				"com.liferay.portlet.instanceable=false",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=" + GradebookPortletKeys.GRADEBOOK,
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
)
public class GradebookPortlet extends MVCPortlet {
}
