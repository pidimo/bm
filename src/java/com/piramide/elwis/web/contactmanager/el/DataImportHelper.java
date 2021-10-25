package com.piramide.elwis.web.contactmanager.el;

import com.piramide.elwis.cmd.contactmanager.dataimport.configuration.*;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class DataImportHelper {
    private List<String> hiddenElements = new ArrayList<String>();

    public String getOrganizationAndContactPersonMultipleSelect(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        List groups = DataImportDelegate.i.getOrganizationAndContactPersonGroups(companyId);
        String contain = "";
        for (Object object : groups) {
            CompoundGroup compoundGroup = (CompoundGroup) object;
            contain += buildSelectOptions(compoundGroup, JSPHelper.getMessage(request, compoundGroup.getResourceKey()), request, 10, 10);
        }

        String selectTag = "<select style=\"height:260px;width:100%;\" id=\"availableColumnsId\" class=\"multipleSelect\" name=\"dto(availableColumns)\" multiple>";
        String endSelecTag = "</select>";

        return selectTag + "\n" + contain + "\n" + endSelecTag + "\n" + buildColumNameHiddens();
    }

    public String getOrganizationMultipleSelect(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CompoundGroup organizationGroup = DataImportDelegate.i.getOrganizationGroup(companyId);

        String selectTag = "<select style=\"height:260px;width:100%;\" id=\"availableColumnsId\" class=\"multipleSelect\" name=\"dto(availableColumns)\" multiple>";
        String contain = buildSelectOptions(organizationGroup, JSPHelper.getMessage(request, organizationGroup.getResourceKey()), request, 10, 10);
        String endSelecTag = "</select>";

        return selectTag + "\n" + contain + "\n" + endSelecTag + "\n" + buildColumNameHiddens();
    }

    public String getContactMultipleSelect(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CompoundGroup contactGroup = DataImportDelegate.i.getContactGroup(companyId);

        String selectTag = "<select style=\"height:260px;width:100%;\" id=\"availableColumnsId\" class=\"multipleSelect\" name=\"dto(availableColumns)\" multiple>";
        String contain = buildSelectOptions(contactGroup, JSPHelper.getMessage(request, contactGroup.getResourceKey()), request, 10, 10);
        String endSelecTag = "</select>";

        return selectTag + "\n" + contain + "\n" + endSelecTag + "\n" + buildColumNameHiddens();
    }

    public String getEmptyMultipleSelect() {
        String selectTag = "<select style=\"height:260px;width:100%;\" id=\"availableColumnsId\" class=\"multipleSelect\" name=\"dto(availableColumns)\" multiple>";

        String endSelecTag = "</select>";
        return selectTag + "\n" + endSelecTag;
    }

    private String buildSelectOptions(CompoundGroup compoundGroup,
                                      String mainGroupName,
                                      HttpServletRequest request,
                                      Integer groupPadding,
                                      Integer optionPadding) {
        String htmlCode = "<optgroup label=\"" + getResource(compoundGroup.getResourceKey(), request) + "\" style=\"padding-left: " + groupPadding + "px;\">\n";
        String endOptGroupTag = "</optgroup>";

        if (!compoundGroup.getColumns().isEmpty()) {
            List<Column> columns = compoundGroup.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                if (column instanceof StaticColumn) {
                    String resource = getResource(((StaticColumn) column).getResourceKey(), request);
                    htmlCode += "<option value=\"" + compoundGroup.getGroupId() + "_" + column.getColumnId() + "\" style=\"padding-left: " + optionPadding + "px;\">" +
                            resource +
                            "</option>\n";
                    addHiddenElement(compoundGroup.getGroupId(), column.getColumnId(), resource, mainGroupName);
                }
                if (column instanceof DinamicColumn) {
                    String translation = getTranslation(((DinamicColumn) column), request);
                    htmlCode += "<option value=\"" + compoundGroup.getGroupId() + "_" + column.getColumnId() + "\" style=\"padding-left: " + optionPadding + "px;\">" +
                            translation +
                            "</option>\n";
                    addHiddenElement(compoundGroup.getGroupId(), column.getColumnId(), translation, mainGroupName);
                }
            }
        }
        List<Group> groups = compoundGroup.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            if (group instanceof CompoundGroup) {
                htmlCode += buildSelectOptions((CompoundGroup) group, mainGroupName, request, groupPadding + 10, optionPadding + 10);
            }
            if (group instanceof SingleGroup) {
                htmlCode += "<optgroup label=\"" + getResource(group.getResourceKey(), request) + "\" style=\"padding-left: " + (groupPadding + 10) + "px;\">\n";
                List<Column> columns = ((SingleGroup) group).getColumns();
                for (int j = 0; j < columns.size(); j++) {
                    Column column = columns.get(j);
                    if (column instanceof StaticColumn) {
                        String resource = getResource(((StaticColumn) column).getResourceKey(), request);
                        htmlCode += "<option value=\"" + group.getGroupId() + "_" + column.getColumnId() + "\" style=\"padding-left: " + (optionPadding + 10) + "px;\">" +
                                resource +
                                "</option>\n";
                        addHiddenElement(group.getGroupId(), column.getColumnId(), resource, mainGroupName);
                    }
                    if (column instanceof DinamicColumn) {
                        String translation = getTranslation(((DinamicColumn) column), request);
                        htmlCode += "<option value=\"" + group.getGroupId() + "_" + column.getColumnId() + "\" style=\"padding-left: " + (optionPadding + 10) + "px;\">" +
                                translation +
                                "</option>\n";
                        addHiddenElement(group.getGroupId(), column.getColumnId(), translation, mainGroupName);
                    }
                }
                htmlCode += endOptGroupTag;
            }
        }

        return htmlCode + endOptGroupTag;
    }

    private String filter(String cad) {
        return com.piramide.elwis.web.common.el.Functions.filterForHtml(cad);
    }

    public String getColumnLabel(Column column, HttpServletRequest request) {
        if (column instanceof StaticColumn) {
            return getResource(((StaticColumn) column).getResourceKey(), request);
        }
        if (column instanceof DinamicColumn) {
            return getTranslation((DinamicColumn) column, request);
        }

        return null;
    }


    private String getResource(String resourceKey, HttpServletRequest request) {
        return JSPHelper.getMessage(request, resourceKey);
    }

    public String getTranslation(DinamicColumn dinamicColumn, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        String language = (String) user.getValue("locale");
        String translation = dinamicColumn.getDefaultText();
        if (null != dinamicColumn.getTranslation(language)) {
            translation = dinamicColumn.getTranslation(language).getText();
        }

        return filter(translation);
    }


    private void addHiddenElement(Integer groupId, Integer columnId, String text, String mainGroupName) {
        String columnNameHiddenElement = "<input id=\"columnName_" + groupId + "_" + columnId + "_Id\" type=\"hidden\" value=\"" + text + "\" name=\"dto(columnName_" + groupId + "_" + columnId + ")\"/>";
        String groupNameHiddenElement = "<input id=\"mainGroupName_" + groupId + "_" + columnId + "_Id\" type=\"hidden\" value=\"" + mainGroupName + "\" name=\"dto(mainGroupName_" + groupId + "_" + columnId + ")\"/>";
        hiddenElements.add(columnNameHiddenElement);
        hiddenElements.add(groupNameHiddenElement);
    }

    private String buildColumNameHiddens() {
        String result = "";
        for (String hidden : hiddenElements) {
            result += hidden + "\n";
        }
        return result;
    }
}
