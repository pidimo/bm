package com.piramide.elwis.dto.uimanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Alfacentauro Team
 * Used as wrapper to tansport elements of xml style sheet file.
 *
 * @author miky
 * @version $Id: StyleElementWrapperDTO.java 12327 2016-01-29 19:45:06Z miguel $
 */
public class StyleElementWrapperDTO implements Serializable {
    private Integer styleId;
    private String elementName;
    private String elementResource;
    private String elementConfigurable;
    private String elementClass;
    private String elementExtends;
    private List attributes;
    private List elementChild;

    public StyleElementWrapperDTO() {
        attributes = new LinkedList();
        elementChild = new LinkedList();
    }

    public StyleElementWrapperDTO(String eName, String eResource, String eConfigurable, String eClass, String eExtends) {
        elementName = eName;
        elementResource = eResource;
        elementConfigurable = eConfigurable;
        elementClass = eClass;
        elementExtends = eExtends;
        attributes = new LinkedList();
        styleId = null;
        elementChild = new LinkedList();
    }

    public void addAttributeDTO(StyleAttributeWrapperDTO attributeDTO) {
        attributes.add(attributeDTO);
    }

    public StyleAttributeWrapperDTO getElementDTO(int index) {
        return (StyleAttributeWrapperDTO) attributes.get(index);
    }

    /*public void removeAttributeDTO(StyleAttributeWrapperDTO attributeDTO) {
        attributes.remove(attributeDTO);
    }*/

    /*public void removeAttributeDTO(int index) {
        attributes.remove(index);
    }*/

    public List getAttributes() {
        return attributes;
    }

    public void setAttributes(List list) {
        attributes = list;
    }

    /*public int getSize() {
        return attributes.size();
    }*/

    private void verifySize(int index, List list) {
        if (index >= list.size()) {
            while (index > list.size() - 1) {
                list.add(new StyleAttributeWrapperDTO());
            }
        }
    }

    public StyleAttributeWrapperDTO getAttribute(int index) {
        verifySize(index, attributes);
        return (StyleAttributeWrapperDTO) attributes.get(index);
    }

    public void setAttribute(int index, StyleAttributeWrapperDTO data) {
        verifySize(index, attributes);
        attributes.set(index, data);
    }

    /*public static Map initMapElementWrapperDTO(Map mapElement, String keyElement, String eName, String eResource, String eConfigurable, String eClass) {
        StyleElementWrapperDTO elementWrapperDTO = new StyleElementWrapperDTO(eName,eResource,eConfigurable,eClass);
        mapElement.put(keyElement,elementWrapperDTO);

        return mapElement;
    }

    public static Map addToMapAttributeDTO(Map mapElement, String keyElement, StyleAttributeWrapperDTO attributeDTO) {
        if (mapElement.containsKey(keyElement)) {
            StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) mapElement.get(keyElement);
            elementWrapperDTO.getAttributes().add(attributeDTO);
        }
        return mapElement;
    }*/

    //// get and set of attributes

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String eName) {
        elementName = eName;
    }

    public String getElementResource() {
        return elementResource;
    }

    public void setElementResource(String eResource) {
        elementResource = eResource;
    }

    public String getElementConfigurable() {
        return elementConfigurable;
    }

    public void setElementConfigurable(String eConfigurable) {
        elementConfigurable = eConfigurable;
    }

    public String getElementClass() {
        return elementClass;
    }

    public void setElementClass(String eClass) {
        elementClass = eClass;
    }

    public String getElementExtends() {
        return elementExtends;
    }

    public void setElementExtends(String elementExtends) {
        this.elementExtends = elementExtends;
    }

    //element child
    public void addElementChildDTO(ElementChildWrapperDTO elementChildDTO) {
        elementChild.add(elementChildDTO);
    }

    /*public ElementChildWrapperDTO getElementChildDTO(int index) {
        return (ElementChildWrapperDTO) elementChild.get(index);
    }*/

    public List getElementChild() {
        return elementChild;
    }

    public void setElementChild(List list) {
        elementChild = list;
    }

    public ElementChildWrapperDTO getChildWrapperDTO(int index) {
        verifySizeListChild(index, elementChild);
        return (ElementChildWrapperDTO) elementChild.get(index);
    }

    public void setChildWrapperDTO(int index, ElementChildWrapperDTO data) {
        verifySizeListChild(index, elementChild);
        elementChild.set(index, data);
    }

    private void verifySizeListChild(int index, List list) {
        if (index >= list.size()) {
            while (index > list.size() - 1) {
                list.add(new ElementChildWrapperDTO());
            }
        }
    }


}
