package com.piramide.elwis.web.dashboard.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 1:39:14 PM
 */
public class ContainerConfigurationForm extends DefaultForm {

    private String[] left;
    private String[] right;
    private String[] availableComponents;


    public String[] getLeft() {
        return left;
    }

    public void setLeft(String[] left) {
        this.left = left;
    }

    public String[] getRight() {
        return right;
    }

    public void setRight(String[] right) {
        this.right = right;
    }

    public String[] getAvailableComponents() {
        return availableComponents;
    }

    public void setAvailableComponents(String[] availableComponents) {
        this.availableComponents = availableComponents;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }
}
