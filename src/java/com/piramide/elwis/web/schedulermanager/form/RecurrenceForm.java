package com.piramide.elwis.web.schedulermanager.form;

import net.java.dev.strutsejb.web.DefaultForm;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: May 5, 2005
 * Time: 2:29:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecurrenceForm extends DefaultForm {

    public Object[] getException() {
        List list = (List) getDto("exception");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setException(Object[] exception1) {

        if (exception1 != null) {
            setDto("exception", Arrays.asList(exception1));
        }
    }
}