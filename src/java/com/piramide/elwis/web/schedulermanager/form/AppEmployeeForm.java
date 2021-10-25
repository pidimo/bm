package com.piramide.elwis.web.schedulermanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 14, 2005
 * Time: 3:16:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class AppEmployeeForm extends DefaultForm {

    private Log log = LogFactory.getLog(AppEmployeeForm.class);

    public Object[] getArray2() {
        List list = (List) getDto("array2");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setArray2(Object[] array21) {
        if (array21 != null) {
            setDto("array2", Arrays.asList(array21));
        }
    }

    public Object[] getList1() {
        return (Object[]) this.getDto("list1");
    }

    public void setList1(Object[] list1) {
        this.setDto("list1", Arrays.asList(list1));
    }

}
