package com.piramide.elwis.web.uimanager.form;

import com.piramide.elwis.dto.uimanager.StyleAttributeWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleElementWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleSectionWrapperDTO;
import com.piramide.elwis.utils.UIManagerConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.uimanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleSheetForm.java 12260 2016-01-27 01:55:35Z miguel $
 */
public class StyleSheetForm extends DefaultForm {
    private Map mapSection;

    public StyleSheetForm() {
        super();
        mapSection = new LinkedHashMap();
    }

    public boolean getInitStyle() {

        Object objMap = super.getDto("sectionMap");
        if (objMap != null) {
            this.mapSection = (Map) objMap;
        }
        return true;
    }

    public Map getMapSection() {
        return mapSection;
    }

    public void setMapSection(Map mapSection) {
        this.mapSection = mapSection;
    }

    public StyleSectionWrapperDTO getStyleWrapper(String key) {
        if (mapSection.containsKey(key)) {
            return (StyleSectionWrapperDTO) mapSection.get(key);
        } else {
            mapSection.put(key, new StyleSectionWrapperDTO());
        }
        return (StyleSectionWrapperDTO) mapSection.get(key);

    }

    public void setStyleWrapper(String key, StyleSectionWrapperDTO value) {
        mapSection.put(key, value);
    }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate StyleSheetForm........." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("restoreAll") != null) {
            setDto("op", "delete");
        } else if (getDto("restorePredetermined") == null) {
            validateStyleMapStrucuture(errors, request);
        }

        if (errors.isEmpty()) {
            //set to read in StyleSheetCmd, styles in change
            Map sectionMapWithAllElements = getSectionWithAllElements(request);
            setDto("sectionMap", sectionMapWithAllElements);

            //set all style sheet structure
            Map xmlStyleSheet = Functions.getXmlStyleSheetEstructure(request, null, true);
            setDto(UIManagerConstants.XMLSTYLESHEET_KEY, xmlStyleSheet);
        } else {
            request.setAttribute("hasErrors", new Boolean(true));
        }

        request.setAttribute("companyStyleKey", getDto("companyStyle"));
        request.setAttribute("flagChangeCompanyStyle", getDto("flagChangeCompanyStyle"));

        return errors;
    }

    private void validateStyleMapStrucuture(ActionErrors errors, HttpServletRequest request) {
        log.debug("Executing method validateStyleMapStrucuture..............................");

        Map sectionsMap = getMapSection();
        for (Iterator iterator = sectionsMap.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            //wath section be change
            if (sectionWrapperDTO.getIsSectionView()) {

                for (Iterator iterator2 = sectionWrapperDTO.getElements().iterator(); iterator2.hasNext();) {
                    StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                    //validate
                    for (Iterator iterator3 = elementWrapperDTO.getAttributes().iterator(); iterator3.hasNext();) {
                        StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();
                        String value = attributeWrapperDTO.getAttributeNewValue().trim();

                        if (attributeWrapperDTO.getAttributeConfigurable().equals("true")) {

                            //validate color
                            if (Functions.attributeIsOfTypeColor(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getIsUrl()) ||
                                    (attributeWrapperDTO.getIsUrl() && attributeWrapperDTO.getBackgroundKey().equals(UIManagerConstants.IS_COLOR_KEY))) {

                                if (!attributeWrapperDTO.getIsDefault()) {
                                    if (value.indexOf("/") != -1 && value.indexOf(".") != -1) {
                                        value = "";
                                        attributeWrapperDTO.setAttributeNewValue(value);
                                    }

                                    validateColorInputs(value, elementWrapperDTO, attributeWrapperDTO, errors, request);
                                }
                            }

                            //validate url
                            if (attributeWrapperDTO.getIsUrl() && !attributeWrapperDTO.getIsDefault() && attributeWrapperDTO.getBackgroundKey().equals(UIManagerConstants.IS_MOSAIC_KEY)) {
                                if (GenericValidator.isBlankOrNull(value) || value.indexOf("/") == -1) {
                                    errors.add("attribute", new ActionError("UIManager.error.attributeRequired",
                                            JSPHelper.getMessage(request, attributeWrapperDTO.getAttributeResource()),
                                            JSPHelper.getMessage(request, elementWrapperDTO.getElementResource())));
                                    attributeWrapperDTO.setAttributeNewValue("");
                                }
                            }
                        }
                    }

                }
            }//if
        }
    }

    private void validateColorInputs(String value, StyleElementWrapperDTO elementWrapperDTO, StyleAttributeWrapperDTO attributeWrapperDTO, ActionErrors errors, HttpServletRequest request) {

        if (GenericValidator.isBlankOrNull(value)) {
            errors.add("attribute", new ActionError("UIManager.error.attributeRequired",
                    JSPHelper.getMessage(request, attributeWrapperDTO.getAttributeResource()),
                    JSPHelper.getMessage(request, elementWrapperDTO.getElementResource())));
        } else {

            if (Functions.attributeIsOfTypeGradient(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getAttributeType())) {
                validateGradientColors(value, elementWrapperDTO, attributeWrapperDTO, errors, request);

            } else if (Functions.attributeIsOfTypeBoxShadow(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getAttributeType())) {
                validateBoxShadowColor(value, elementWrapperDTO, attributeWrapperDTO, errors, request);

            } else {
                errors.add(validateColorAsHexadecimal(value, elementWrapperDTO, attributeWrapperDTO, request));
            }
        }
    }

    private ActionErrors validateColorAsHexadecimal(String value, StyleElementWrapperDTO elementWrapperDTO, StyleAttributeWrapperDTO attributeWrapperDTO, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (value != null) {
            if (value.startsWith("#")) {
                try {
                    String hexValue = value.substring(1, value.length());
                    BigInteger bi = new BigInteger(hexValue, 16);
                } catch (NumberFormatException ne) {
                    errors.add("attribute", new ActionError("UIManager.error.hexNumberFormat",
                            JSPHelper.getMessage(request, attributeWrapperDTO.getAttributeResource()),
                            JSPHelper.getMessage(request, elementWrapperDTO.getElementResource())));
                }
            } else {
                errors.add("attribute", new ActionError("UIManager.error.colorFormat", value));
            }
        }
        return errors;
    }

    private void validateGradientColors(String value, StyleElementWrapperDTO elementWrapperDTO, StyleAttributeWrapperDTO attributeWrapperDTO, ActionErrors errors, HttpServletRequest request) {
        if (value != null) {
            String[] colorValues = value.split(Pattern.quote(UIManagerConstants.VALUE_SEPARATOR_KEY));

            if (colorValues.length == 2) {
                ActionErrors colorErrors = validateColorAsHexadecimal(colorValues[0], elementWrapperDTO, attributeWrapperDTO, request);
                if (colorErrors.isEmpty()) {
                    colorErrors = validateColorAsHexadecimal(colorValues[1], elementWrapperDTO, attributeWrapperDTO, request);
                }
                errors.add(colorErrors);
            }
        }
    }

    private void validateBoxShadowColor(String value, StyleElementWrapperDTO elementWrapperDTO, StyleAttributeWrapperDTO attributeWrapperDTO, ActionErrors errors, HttpServletRequest request) {
        //this is RGBA color
    }

    /**
     * add to section map of the form all elements of the section
     *
     * @param request
     * @return Map with all elements
     */
    private Map getSectionWithAllElements(HttpServletRequest request) {

        String paramSection = request.getParameter("paramSection");
        Map formSectionMap = getMapSection();
        Map sectionAllElements = Functions.getXmlStyleSheetEstructure(request, paramSection, false);

        for (Iterator iterator = sectionAllElements.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
            String sectionName = sectionWrapperDTO.getSectionName();

            if (sectionName.equals(paramSection)) {

                List elements = sectionWrapperDTO.getElements();
                for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                    StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();
                    if (!existThisElement(formSectionMap, elementWrapperDTO)) {
                        //add new element
                        StyleSectionWrapperDTO.addToMapElementDTO(formSectionMap, getKeyThisSection(formSectionMap, sectionName), elementWrapperDTO);
                    }
                }
            }
        }

        return formSectionMap;
    }

    /**
     * verif if this element exist in the section map of the form
     *
     * @param formSectionMap    section Map of the form
     * @param elementWrapperDTO the element
     * @return true or false
     */
    private boolean existThisElement(Map formSectionMap, StyleElementWrapperDTO elementWrapperDTO) {

        for (Iterator iterator = formSectionMap.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            List elements = sectionWrapperDTO.getElements();
            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO formElementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();
                if (formElementWrapperDTO.getElementName().equals(elementWrapperDTO.getElementName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * get the key of the section in the Map
     *
     * @param formSectionMap
     * @param sectionName
     * @return String with key of the section
     */
    private String getKeyThisSection(Map formSectionMap, String sectionName) {
        String key = null;

        Set keys = formSectionMap.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String keySection = iterator.next().toString();
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) formSectionMap.get(keySection);

            if (sectionWrapperDTO.getSectionName().equals(sectionName)) {
                key = keySection;
                return key;
            }
        }
        return key;
    }
}