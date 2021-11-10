package com.liferay.training.gradebook.web.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.BaseManagementToolbarDisplayContext;
import  com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import  com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import  com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import  com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItem;
import  com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;
import  com.liferay.portal.kernel.dao.search.SearchContainer;
import  com.liferay.portal.kernel.language.LanguageUtil;
import  com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import  com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import  com.liferay.portal.kernel.portlet.PortalPreferences;
import  com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import  com.liferay.portal.kernel.portlet.PortletURLUtil;
import  com.liferay.portal.kernel.theme.ThemeDisplay;
import  com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.gradebook.web.constants.GradebookPortletKeys;
import com.liferay.training.gradebook.web.constants.MVCCommandNames;

import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.servlet.http.HttpServletRequest;



public class AssignmentsManagementToolbarDisplayContext extends BaseManagementToolbarDisplayContext {
    public AssignmentsManagementToolbarDisplayContext(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse, HttpServletRequest httpServletRequest) {
        super(liferayPortletRequest, liferayPortletResponse, httpServletRequest);
        _portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(liferayPortletRequest);
        _themeDisplay = (ThemeDisplay) httpServletRequest.getAttribute(WebKeys.THEME_DISPLAY);
    }
    public CreationMenu getCreationMenu(){

        return new CreationMenu(){
            {
                addDropdownItem(dropdownItem ->  {
                    dropdownItem.setHref(liferayPortletResponse.createRenderURL(),"mvcRenderCommandName",
                            MVCCommandNames.EDIT_ASSIGNMENT,"redirect",currentURLObj.toString());
                    dropdownItem.setLabel(LanguageUtil.get(request,"add-assignment"));
                });
            }
        };
    }
    @Override
    public String getClearResultsURL() {
        return getSearchActionURL();
    }


    public String getDisplayStyle() {
        String displayStyle = ParamUtil.getString(request, "displayStyle");
        if (Validator.isNull(displayStyle)) {
            displayStyle = _portalPreferences.getValue(
                    GradebookPortletKeys.GRADEBOOK, "assignments-display-style",
                    "descriptive");
        }
        else {
            _portalPreferences.setValue(
                    GradebookPortletKeys.GRADEBOOK, "assignments-display-style",
                    displayStyle);
            request.setAttribute(
                    WebKeys.SINGLE_PAGE_APPLICATION_CLEAR_CACHE, Boolean.TRUE);
        }
    return displayStyle;
    }
    public String getOrderByCol() {
        return ParamUtil.getString(request, "orderByCol", "title");
    }

    public String getOrderByType() {
        return ParamUtil.getString(request, "orderByType", "asc");
    }
    @Override
    public String getSearchActionURL() {
        PortletURL searchURL = liferayPortletResponse.createRenderURL();
        searchURL.setProperty(
                "mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENTS);
        String navigation = ParamUtil.getString(
                request, "navigation", "entries");
        searchURL.setParameter("navigation", navigation);
        searchURL.setParameter("orderByCol", getOrderByCol());
        searchURL.setParameter("orderByType", getOrderByType());
        return searchURL.toString();
    }
    @Override
    public List<ViewTypeItem> getViewTypeItems() {
        PortletURL portletURL = liferayPortletResponse.createRenderURL();
        portletURL.setParameter(
                "mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENTS);
        int delta =
                ParamUtil.getInteger(request, SearchContainer.DEFAULT_DELTA_PARAM);
        if (delta > 0) {
            portletURL.setParameter("delta", String.valueOf(delta));
        }
        String orderByCol =
                ParamUtil.getString(request, "orderByCol", "title");
        String orderByType =
                ParamUtil.getString(request, "orderByType", "asc");
        portletURL.setParameter("orderByCol", orderByCol);
        portletURL.setParameter("orderByType", orderByType);
        int cur =
                ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM);
        if (cur > 0) {
            portletURL.setParameter("cur", String.valueOf(cur));
        }
        return new ViewTypeItemList(portletURL, getDisplayStyle()) {
            {
                addCardViewTypeItem();
                addListViewTypeItem();
                addTableViewTypeItem();
            }
        };
    }
    @Override
    protected List<DropdownItem> getOrderByDropdownItems() {
        return new DropdownItemList() {
            {
                add(
                        dropdownItem -> {
                            dropdownItem.setActive("title".equals(getOrderByCol()));
                                    dropdownItem.setHref(
                                            _getCurrentSortingURL(), "orderByCol","title");
                                            dropdownItem.setLabel(
                                                    LanguageUtil.get(request, "title"));
                        });

                add(
                        dropdownItem -> {
                            dropdownItem.setActive(
                                    "createDate".equals(getOrderByCol()));
                            dropdownItem.setHref(
                                    _getCurrentSortingURL(), "orderByCol","createDate");
                            dropdownItem.setLabel(
                                    LanguageUtil.get(request, "create-date"));
                        });
            }
        };
    }
    private PortletURL _getCurrentSortingURL() throws PortletException {
        PortletURL sortingURL = PortletURLUtil.clone(
                currentURLObj, liferayPortletResponse);
        sortingURL.setParameter(
                "mvcRenderCommandName", MVCCommandNames.VIEW_ASSIGNMENTS);
        // Reset current page.
        sortingURL.setParameter(SearchContainer.DEFAULT_CUR_PARAM, "0");
        String keywords = ParamUtil.getString(request, "keywords");
        if (Validator.isNotNull(keywords)) {
            sortingURL.setParameter("keywords", keywords);
        }
        return sortingURL;
    }
    private final PortalPreferences _portalPreferences;
    private final ThemeDisplay _themeDisplay;
}

