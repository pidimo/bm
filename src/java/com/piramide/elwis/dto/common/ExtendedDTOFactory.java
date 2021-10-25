package com.piramide.elwis.dto.common;

import net.java.dev.strutsejb.SysLevelException;
import net.java.dev.strutsejb.dto.ComponentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Provides extended factory methods for DTO.
 *
 * @author Fernando Montaño
 * @version $Id: ExtendedDTOFactory.java 9703 2009-09-12 15:46:08Z fernando $
 * @see net.java.dev.strutsejb.dto.DTOFactory
 */

public class ExtendedDTOFactory {

    public static final ExtendedDTOFactory i = new ExtendedDTOFactory();
    protected final Log log = LogFactory.getLog(getClass());

    private ExtendedDTOFactory() {
    }

    private final Map businessPropCache = new HashMap();

    /**
     * Copies values of business property in the ComponentDTO to the Object.
     * Each value of ComponentDTO will be converted to type of setter in the
     * Object by the following manner:<BR>
     * <BR>
     * - if value is null, skip it.<BR>
     * - if types of both value and setter are same, set the value to the
     * Object as is.<BR>
     * - if value is String and setter type is one of number wrapper
     * (eg Integer), parse and convert it to setter type.<BR>
     * - otherwise, throw SysLevelException.<BR>
     *
     * @param dto      ComponentDTO to read
     * @param obj      Object to write
     * @param isUpdate if this is update (not create)
     */
    public void copyFromDTO(ComponentDTO dto, Object obj, boolean isUpdate) {

        //parse String values of ParamDTO to non-String objects
        dto.parseValues();
        //fill values into the Object
        final List businessProps = selectBusinessProps(obj);
        for (Iterator it = businessProps.iterator(); it.hasNext();) {

            //get the value from DTO
            final PropertyDescriptor pd = (PropertyDescriptor) it.next();
            final Object value = retriveValueFromDTO(pd, dto);

            //skip set null value (dto do not contains the property)
            if (value == null) {
                continue;
            }

            //prim key should be skipped when update
            if (isUpdate) {
                final boolean isPrimKey = pd.getName().equals(dto.getPrimKeyName());
                if (isPrimKey) {
                    continue;
                }
            }

            //call setter method to set property value
            final Method writeMethod = pd.getWriteMethod();
            try {
                //set to null the property if value is null or empty string
                if ("".equals(value)) {
                    writeMethod.invoke(obj, new Object[]{null});
                } else {
                    writeMethod.invoke(obj, new Object[]{value});
                }


            } catch (IllegalArgumentException e) {
                throw new SysLevelException(e);
            } catch (IllegalAccessException e) {
                throw new SysLevelException(e);
            } catch (InvocationTargetException e) {
                throw new SysLevelException(e);
            }
        }
    }

    //selects properties that has both getter and setter
    private List selectBusinessProps(Object obj) {

        //check if cache is available
        final String className = obj.getClass().getName();
        List businessProps = (List) businessPropCache.get(className);
        if (businessProps != null) {
            return businessProps;
        }

        //introspecting the Object
        final BeanInfo bi;
        try {
            bi = Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            throw new SysLevelException(e);
        }

        //select business properties
        businessProps = new LinkedList();
        final PropertyDescriptor[] pd = bi.getPropertyDescriptors();
        for (int i = 0; i < pd.length; i++) {
            if (hasAccessors(pd[i]) && isNotCMRField(pd[i])) {
                businessProps.add(pd[i]);
            }
        }

        //put the business property list into cache
        businessPropCache.put(className, businessProps);
        return businessProps;
    }

    private boolean hasAccessors(PropertyDescriptor pd) {
        return (pd.getReadMethod() != null) && (pd.getWriteMethod() != null);
    }

    private boolean isNotCMRField(PropertyDescriptor pd) {
        final Method mtd = pd.getReadMethod();
        final Class cls = mtd.getReturnType();
        final boolean isEJBLocalObject =
                EJBLocalObject.class.isAssignableFrom(cls);
        final boolean isCollection = Collection.class.isAssignableFrom(cls);
        final boolean isCMRField = isEJBLocalObject || isCollection;
        return !isCMRField;
    }

    private Object retriveValueFromDTO(PropertyDescriptor pd,
                                       ComponentDTO dto) {

        if (!dto.containsKey(pd.getName())) //if dto does not contains the bean property
        {
            return null;
        }

        //get value if dto contains the bean property name
        final Object value = dto.get(pd.getName());
        final Class propType = pd.getPropertyType();

        if (value != null && value.getClass() == propType) {
            return value;
        }

        //ensure that the value null will be set in the bean property
        if (value == null) {
            return "";
        }
        if (value instanceof String && "".equals(((String) value).trim())) {
            return "";
        }


        //parse the value as String
        final String s = (String) value;
        if (propType == Boolean.class) {
            return Boolean.valueOf(s);
        } else if (propType == Byte.class) {
            return Byte.valueOf(s);
        } else if (propType == Double.class) {
            return Double.valueOf(s);
        } else if (propType == Float.class) {
            return Float.valueOf(s);
        } else if (propType == Integer.class) {
            return Integer.valueOf(s);
        } else if (propType == Long.class) {
            return Long.valueOf(s);
        } else if (propType == Short.class) {
            return Short.valueOf(s);
        } else if (propType == BigDecimal.class) {
            return BigDecimal.valueOf(Long.parseLong(s));
        } else if (propType == BigInteger.class) {
            return BigInteger.valueOf(Long.parseLong(s));
        }
        throw new SysLevelException("type not supported: " + propType);
    }
}
