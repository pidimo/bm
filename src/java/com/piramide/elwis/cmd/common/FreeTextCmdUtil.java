package com.piramide.elwis.cmd.common;

import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Freetext util which can be used from commands that uses Entity Beans related to
 * some kind of FreeText bean implentation and their relation with the bean class through
 * relationship.
 *
 * @author Fernando Montaño
 * @version $Id: FreeTextCmdUtil.java 9120 2009-04-17 00:27:45Z fernando $
 */

public class FreeTextCmdUtil {
    public static final FreeTextCmdUtil i = new FreeTextCmdUtil();
    private static Log log = LogFactory.getLog(FreeTextCmdUtil.class);

    private FreeTextCmdUtil() {
    }

    /**
     * Makes the CRUD operations over freetext bean which has a relationship with an entity bean.
     * Notes: The companyId is required in the entity bean, it's manadatory the constructor of the freetext bean has
     * the format (byte[] value, Integer companyId, Integer freetextType).
     *
     * @param paramDTO                the paramDTO
     * @param resultDTO               the resultDTO
     * @param ejbObject               the EntityBean which has the relatioship with a kind of FreeText entity bean.
     * @param ejbPropertyFreeTextName the name of the property Freetext in the entity bean. e.g. if productBean has a
     *                                relationship with ProductFreeText and the relationship cmr name is descriptionText,
     *                                you need to pass the name: DescriptionText.
     *                                Additionally to that name you need to create a method in the entity bean named
     *                                identical as DescriptionText with the difference that it needs a param EJBLocalObject
     *                                in the EJB and the respective casting to correct freetext bean.
     *                                Remember that we needs only a set method and it cannot be an abstract method,
     *                                it must be a simple method in the Bean class and with their respective declaration in the
     *                                the Local interface classs.Example:
     *                                <java>
     *                                Class RoleBean .....
     *                                public void setDescriptionText(EJBLocalObject descriptionText) {
     *                                setDescriptionText((AdminFreeText) descriptionText);
     *                                }
     *                                </java>
     *                                <p/>
     *                                (Note: the property name needs starts with the first character uppercase.)
     * @param freeTextHome            the FreeText localhome interface class.
     * @param freeTextJndi            the freetext jndi name.
     * @param freeTextType            the freetext type.
     * @param paramDescritionTextName the description text property name in the paramDTO
     */
    public void doCRUD(ParamDTO paramDTO, ResultDTO resultDTO, EJBLocalObject ejbObject,
                       String ejbPropertyFreeTextName, Class freeTextHome, String freeTextJndi,
                       int freeTextType, String paramDescritionTextName) {
        log.debug("Executing FreeText CRUD");
        try {

            EJBLocalObject freeTextLocalObject = null;
            byte[] freeTextValue = null;
            //check if bean is null, if true  get the freetext object related to it.
            if (ejbObject != null) {
                log.debug("Reading the freetext object");
                Method ejbFreeTextMethod = ejbObject.getClass().getMethod("get" + ejbPropertyFreeTextName,
                        new Class[0]);
                freeTextLocalObject = (EJBLocalObject) ejbFreeTextMethod.invoke(ejbObject, new Object[0]);
                if (freeTextLocalObject != null) {
                    Method freeTextValueMethod = freeTextLocalObject.getClass().getMethod("getValue", new Class[0]);
                    freeTextValue = (byte[]) freeTextValueMethod.invoke(freeTextLocalObject, new Object[0]);
                }
            }
            //control update and create cases
            if (ejbObject != null && paramDTO.containsKey(paramDescritionTextName)) {
                EJBLocalHome freeTextHomeObject = EJBFactory.i.getEJBLocalHome(freeTextJndi);

                if (CRUDDirector.OP_CREATE.equals(paramDTO.getOp()) || (CRUDDirector.OP_UPDATE.equals(paramDTO.getOp())
                        && freeTextLocalObject == null)) {
                    log.debug("Create or Update when freetext is not defined");
                    Method companyIdMethod = ejbObject.getClass().getMethod("getCompanyId", new Class[0]);
                    Integer companyId = (Integer) companyIdMethod.invoke(ejbObject, new Object[0]);

                    Method freeTextCreateMethod = freeTextHomeObject.getClass().getMethod("create", new Class[]{
                            byte[].class, Integer.class, Integer.class});
                    freeTextLocalObject = (EJBLocalObject) freeTextCreateMethod.invoke(freeTextHomeObject,
                            new Object[]{paramDTO.getAsString(paramDescritionTextName).getBytes(),
                                    companyId, new Integer(freeTextType)});

                    Method setEjbObjectFreeTextMethod = ejbObject.getClass().getMethod("set" + ejbPropertyFreeTextName,
                            new Class[]{EJBLocalObject.class});
                    setEjbObjectFreeTextMethod.invoke(ejbObject, new Object[]{freeTextLocalObject});

                } else if (CRUDDirector.OP_UPDATE.equals(paramDTO.getOp()) && freeTextLocalObject != null) {
                    log.debug("Updating existing freetext relation");
                    Method setFreeTextValueMethod = freeTextLocalObject.getClass().getMethod("setValue",
                            new Class[]{byte[].class});
                    setFreeTextValueMethod.invoke(freeTextLocalObject, new Object[]{
                            paramDTO.getAsString(paramDescritionTextName).getBytes()});

                }
            }

            resultDTO.put(paramDescritionTextName, (freeTextValue != null) ? new String(freeTextValue) : "");

        } catch (NoSuchMethodException e) {
            log.error("Error", e);
        } catch (InvocationTargetException e) {
            log.error("Error", e);
        } catch (IllegalAccessException e) {
            log.error("Error", e);
        }

    }

}