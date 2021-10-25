package com.piramide.elwis.web.dashboard.component.persistence;

import com.piramide.elwis.web.dashboard.component.util.ResourceReader;

/**
 * @author : ivan
 */
public class DaoFactory {
    public static AbstractDao getDao(String className) {
        AbstractDao absDao;
        try {
            absDao = (AbstractDao) ResourceReader.getClassInstance(className);
        } catch (ClassCastException cle) {
            throw new RuntimeException("The class :" + className + " is not sub of AbstractDao ... ", cle);
        } catch (NullPointerException npe) {
            throw new RuntimeException("Cannot instanciate AbstractDao from class : " + className + " because ", npe);
        }
        return absDao;
    }
}
