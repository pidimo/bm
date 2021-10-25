package com.piramide.elwis.web.reports.dynamiccolumns;

import com.piramide.elwis.utils.ContactConstants;

/**
 * Jatun S.R.L.
 * Contact person categories dynamic column loader
 *
 * @author Miky
 * @version $Id: ContactPersonCategoryColumnLoader.java 10-sep-2008 14:49:35 $
 */
public class ContactPersonCategoryColumnLoader extends CategoryColumnLoader {
    public String getTableId() {
        return ContactConstants.CONTACTPERSON_CATEGORY;
    }

    @Override
    public String getSecondTableId() {
        return ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY;
    }
}
