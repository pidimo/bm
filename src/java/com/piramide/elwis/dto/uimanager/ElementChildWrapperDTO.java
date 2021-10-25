package com.piramide.elwis.dto.uimanager;

import java.io.Serializable;

/**
 * help to manager the heritage in styles class, save the information of the child of an style class
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: ElementChildWrapperDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class ElementChildWrapperDTO implements Serializable {
    private String childName;

    public ElementChildWrapperDTO() {
        childName = null;
    }

    public ElementChildWrapperDTO(String eChildName) {
        childName = eChildName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}
