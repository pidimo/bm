package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.web.salesmanager.form.SaleBySalesProcessForm;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class SaleBySalesProcessFromContactsForm extends SaleBySalesProcessForm {

    @Override
    protected boolean existsSalesProcess() {
        return true;
    }
}
