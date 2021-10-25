package com.jatun.bm.integration.udabol.webservice;

import javax.ws.rs.FormParam;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class SupportCaseForm {

    @FormParam("company")
    private String company;

    @FormParam("title")
    private String title;

    @FormParam("caseTypeId")
    private String caseTypeId;

    @FormParam("productId")
    private String productId;

    @FormParam("assignedUserCode")
    private String assignedUserCode;

    @FormParam("stateId")
    private String stateId;

    @FormParam("openDate")
    private String openDate;

    @FormParam("expireDate")
    private String expireDate;

    @FormParam("openUserCode")
    private String openUserCode;

    @FormParam("severityId")
    private String severityId;

    @FormParam("priorityId")
    private String priorityId;

    @FormParam("workLevelId")
    private String workLevelId;

    @FormParam("description")
    private String description;

    @FormParam("personTypeId")
    private String personTypeId;

    @FormParam("contactCode")
    private String contactCode;


    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        this.contactCode = contactCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(String caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAssignedUserCode() {
        return assignedUserCode;
    }

    public void setAssignedUserCode(String assignedUserCode) {
        this.assignedUserCode = assignedUserCode;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getOpenUserCode() {
        return openUserCode;
    }

    public void setOpenUserCode(String openUserCode) {
        this.openUserCode = openUserCode;
    }

    public String getSeverityId() {
        return severityId;
    }

    public void setSeverityId(String severityId) {
        this.severityId = severityId;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getWorkLevelId() {
        return workLevelId;
    }

    public void setWorkLevelId(String workLevelId) {
        this.workLevelId = workLevelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonTypeId() {
        return personTypeId;
    }

    public void setPersonTypeId(String personTypeId) {
        this.personTypeId = personTypeId;
    }

    public String validate() {
        StringBuffer errors = new StringBuffer();

        if (isNullOrBlank(company)) {
            errors.append("company").append(" is required\n");
        }
        if (isNullOrBlank(title)) {
            errors.append("title").append(" is required\n");
        }
        if (!isIntegerValue(caseTypeId)) {
            errors.append("caseTypeId").append(" is required and as integer\n");
        }
        if (!isIntegerValue(productId)) {
            errors.append("productId").append(" is required and as integer\n");
        }
        if (isNullOrBlank(assignedUserCode)) {
            errors.append("assignedUserCode").append(" is required\n");
        }
        if (!isIntegerValue(stateId)) {
            errors.append("stateId").append(" is required and as integer\n");
        }
        if (!isIntegerValue(openDate)) {
            errors.append("openDate").append(" is required and as integer\n");
        }
        if (isNullOrBlank(openUserCode)) {
            errors.append("openUserCode").append(" is required\n");
        }

        if (isNullOrBlank(contactCode)) {
            errors.append("contactCode").append(" is required\n");
        }
        if (!isIntegerValue(severityId)) {
            errors.append("severityId").append(" is required and as integer\n");
        }
        if (!isIntegerValue(priorityId)) {
            errors.append("priorityId").append(" is required and as integer\n");
        }
        if (!isIntegerValue(workLevelId)) {
            errors.append("workLevelId").append(" is required and as integer\n");
        }
        if (isNullOrBlank(description)) {
            errors.append("description").append(" is required\n");
        }
        if (!isIntegerValue(personTypeId)) {
            errors.append("personTypeId").append(" is required and as integer\n");
        }

        return (errors.length() > 0) ? errors.toString() : null;
    }

    private boolean isNullOrBlank(String value) {
        return (value == null || value.trim().isEmpty());
    }

    private boolean isIntegerValue(String value) {
        boolean isIntegerValue = false;
        if (!isNullOrBlank(value)) {
            try {
                Integer valueInt = new Integer(value.trim());
                isIntegerValue = valueInt != null;
            } catch (NumberFormatException e) {
                isIntegerValue = false;
            }
        }
        return isIntegerValue;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());

        buffer.append("\ncompany:").append(company)
                .append("\ntitle:").append(title)
                .append("\n caseTypeId:").append(caseTypeId)
                .append("\n productId:").append(productId)
                .append("\n assignedUserCode:").append(assignedUserCode)
                .append("\n stateId:").append(stateId)
                .append("\n openDate:").append(openDate)
                .append("\n expireDate:").append(expireDate)
                .append("\n openUserCode:").append(openUserCode)
                .append("\n contactCode:").append(contactCode)
                .append("\n severityId:").append(severityId)
                .append("\n priorityId:").append(priorityId)
                .append("\n workLevelId:").append(workLevelId)
                .append("\n personTypeId:").append(personTypeId)
                .append("\n description:").append(description);
        return buffer.toString();
    }
}
