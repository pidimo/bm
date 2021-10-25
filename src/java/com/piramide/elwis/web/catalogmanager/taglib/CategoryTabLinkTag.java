package com.piramide.elwis.web.catalogmanager.taglib;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.dto.catalogmanager.CategoryTabDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.util.URLParameterProcessor;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class CategoryTabLinkTag extends BaseHandlerTag {
    public static final String TAG_NAME = "CATEGORY_TAB_LINK_KEY";

    private String id;

    private String action;

    private String categoryConstant;

    private String finderName;

    private String styleClass;

    private List<Object> finderValues;

    private boolean showStartSeparator = false;

    @Override
    public int doStartTag() throws JspException {
        pageContext.setAttribute(getTagName(), this);

        finderValues = new LinkedList<Object>();

        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        ResponseUtils.write(pageContext, buildHtmlLCode().toString());

        return EVAL_PAGE;
    }

    private StringBuilder buildHtmlLCode() {
        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        List<CategoryTabDTO> categoryTabDTOs = getCategoryTabs();
        StringBuilder source = new StringBuilder("");

        if (isShowStartSeparator() && !categoryTabDTOs.isEmpty()) {
            source.append("|");
        }

        for (int i = 0; i < categoryTabDTOs.size(); i++) {
            CategoryTabDTO categoryTabDTO = categoryTabDTOs.get(i);
            StringBuffer url = new StringBuffer(buildUrl(categoryTabDTO));

            URLParameterProcessor.addModuleParameters(url,
                    request,
                    pageContext.getServletContext(),
                    true,
                    true);

            source.append(buildLinkHtmlSourceCode(response.encodeURL(contextPath + new String(url)),
                    (String) categoryTabDTO.get("label")));

            if (i < categoryTabDTOs.size() - 1) {
                source.append("|");
            }
        }

        return source;
    }

    @SuppressWarnings(value = "unchecked")
    private List<CategoryTabDTO> getCategoryTabs() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.setOp("buildCategoryTabs");
        cmd.putParam("companyId", getCompanyId());
        cmd.putParam("table", getCategoryConstant());
        cmd.putParam("finderName", getFinderName());
        cmd.putParam("params", processFinderValues());

        List<CategoryTabDTO> result = new ArrayList<CategoryTabDTO>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            result = (List<CategoryTabDTO>) resultDTO.get("categoryTabs");
        } catch (AppLevelException e) {
            //
        }

        return result;
    }

    private Object[] processFinderValues() {
        List<Object> partial = new LinkedList<Object>();

        for (Object element : finderValues) {
            try {
                partial.add(new Integer(element.toString()));
            } catch (NumberFormatException e) {
                //
            } catch (NullPointerException e) {
                //
            }
        }

        return partial.toArray();
    }

    private Integer getCompanyId() {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        User user = RequestUtils.getUser(request);

        return (Integer) user.getValue(Constants.COMPANYID);
    }

    private String buildUrl(CategoryTabDTO categoryTabDTO) {
        String url = getAction();
        if (url.contains("?")) {
            url += "&";
        } else {
            url += "?";
        }

        url += "categoryTabId=" + categoryTabDTO.get("categoryTabId")
                + "&dto(categoryTabId)=" + categoryTabDTO.get("categoryTabId");

        return url;
    }

    private StringBuilder buildLinkHtmlSourceCode(String action, String label) {
        StringBuilder source = new StringBuilder();
        source.append("<")
                .append(A_TAG.NAME.getHtmlCode())
                .append(" ")
                .append(A_TAG.PROPERTY_HREF.getHtmlCode())
                .append("=")
                .append("\"")
                .append(action)
                .append("\"");
        if (null != getStyleClass() && !"".equals(getStyleClass().trim())) {
            source.append(" ")
                    .append(A_TAG.PROPERTY_CLASS.getHtmlCode())
                    .append("=")
                    .append("\"")
                    .append(getStyleClass())
                    .append("\"")
                    .append(">");
        }

        source.append(label)
                .append("</")
                .append(A_TAG.NAME.getHtmlCode())
                .append(">");

        return source;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategoryConstant() {
        return categoryConstant;
    }

    public void setCategoryConstant(String categoryConstant) {
        this.categoryConstant = categoryConstant;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static String getTagName(String id) {
        return TAG_NAME + "_" + id;
    }

    public void addFinderValue(Object object) {
        finderValues.add(object);
    }

    public boolean isShowStartSeparator() {
        return showStartSeparator;
    }

    public void setShowStartSeparator(boolean showStartSeparator) {
        this.showStartSeparator = showStartSeparator;
    }

    private static enum A_TAG {
        NAME("a"),
        PROPERTY_HREF("href"),
        PROPERTY_CLASS("class");

        private String htmlCode;

        private A_TAG(String htmlCode) {
            this.htmlCode = htmlCode;
        }

        public String getHtmlCode() {
            return htmlCode;
        }
    }

    private String getTagName() {
        return TAG_NAME + "_" + getId();
    }
}
