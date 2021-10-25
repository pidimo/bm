package com.piramide.elwis.web.schedulermanager.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: May 13, 2005
 * Time: 5:11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentListForm extends SearchForm {

    private Log log = LogFactory.getLog(com.piramide.elwis.web.schedulermanager.form.AppointmentListForm.class);

    public Integer getViewerUserId() {
        return viewerUserId;
    }

    public void setViewerUserId(Integer viewerUserId) {
        this.viewerUserId = viewerUserId;
    }

    public String getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(String submitButton) {
        this.submitButton = submitButton;
    }

    private Integer viewerUserId;

    private String submitButton;
}
