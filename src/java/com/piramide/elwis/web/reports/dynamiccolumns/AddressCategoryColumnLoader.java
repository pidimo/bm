package com.piramide.elwis.web.reports.dynamiccolumns;

import com.piramide.elwis.utils.ContactConstants;

/**
 * Jatun S.R.L.
 * Address categories dynamic column loader
 *
 * @author Miky
 * @version $Id: AddressCategoryColumnLoader.java 04-sep-2008 18:17:33 $
 */
public class AddressCategoryColumnLoader extends CategoryColumnLoader {
    public String getTableId() {
        return ContactConstants.ADDRESS_CATEGORY;
    }

    @Override
    public String getSecondTableId() {
        return ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY;
    }
}
