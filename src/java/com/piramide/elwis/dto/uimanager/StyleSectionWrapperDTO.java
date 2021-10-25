package com.piramide.elwis.dto.uimanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Used as wrapper to tansport sections of xml style sheet file.
 *
 * @author miky
 * @version $Id: StyleSectionWrapperDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class StyleSectionWrapperDTO implements Serializable {
    private String sectionName;
    private String sectionResource;
    private String sectionConfigurable;
    private boolean isSectionView;
    private List elements;

    public StyleSectionWrapperDTO() {
        this.elements = new LinkedList();
    }

    /*public StyleSectionWrapperDTO(StyleElementWrapperDTO elementDTO, String sName, String sResource, String sConfigurable) {
        sectionName = sName;
        sectionResource = sResource;
        sectionConfigurable = sConfigurable;
        elements = new LinkedList();
        elements.add(elementDTO);
    }*/

    public StyleSectionWrapperDTO(String sName, String sResource, String sConfigurable, boolean isView) {
        sectionName = sName;
        sectionResource = sResource;
        sectionConfigurable = sConfigurable;
        isSectionView = isView;
        elements = new LinkedList();
    }

    public void addElementDTO(StyleElementWrapperDTO elementDTO) {
        elements.add(elementDTO);
    }

    /*public StyleElementWrapperDTO getElementDTO(int index) {
        return (StyleElementWrapperDTO) elements.get(index);
    }

    public void removeElementDTO(StyleElementWrapperDTO elementDTO) {
        elements.remove(elementDTO);
    }

    public void removeElementDTO(int index) {
        elements.remove(index);
    }*/

    public List getElements() {
        return elements;
    }

    public void setElements(List list) {
        elements = list;
    }

    /*public int getSize() {
        return elements.size();
    }*/

    private void verifySize(int index, List list) {
        if (index >= list.size()) {
            while (index > list.size() - 1) {
                list.add(new StyleElementWrapperDTO());
            }
        }
    }

    public StyleElementWrapperDTO getElement(int index) {
        verifySize(index, elements);
        return (StyleElementWrapperDTO) elements.get(index);
    }

    public void setElement(int index, StyleElementWrapperDTO data) {
        verifySize(index, elements);
        elements.set(index, data);
    }

    public static Map initMapSectionWrapperDTO(Map mapSection, String keySection, String sName, String sResource, String sConfigurable, boolean isView) {
        StyleSectionWrapperDTO sectionWrapperDTO = new StyleSectionWrapperDTO(sName, sResource, sConfigurable, isView);
        mapSection.put(keySection, sectionWrapperDTO);

        return mapSection;
    }

    public static Map addToMapElementDTO(Map mapSection, String keySection, StyleElementWrapperDTO elementDTO) {
        if (mapSection.containsKey(keySection)) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) mapSection.get(keySection);
            sectionWrapperDTO.getElements().add(elementDTO);
        }
        return mapSection;
    }

    ///////////////////////////////////
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sName) {
        sectionName = sName;
    }

    public String getSectionResource() {
        return sectionResource;
    }

    public void setSectionResource(String sResource) {
        sectionResource = sResource;
    }

    public String getSectionConfigurable() {
        return sectionConfigurable;
    }

    public void setSectionConfigurable(String sConfigurable) {
        sectionConfigurable = sConfigurable;
    }

    public boolean getIsSectionView() {
        return isSectionView;
    }

    public void setIsSectionView(boolean isSectionView) {
        this.isSectionView = isSectionView;
    }

}
