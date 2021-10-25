package com.piramide.elwis.dto.uimanager;

import com.piramide.elwis.utils.UIManagerConstants;

import java.io.Serializable;

/**
 * Alfacentauro Team
 * Used as wrapper to tansport attributes of xml style sheet file.
 *
 * @author miky
 * @version $Id: StyleAttributeWrapperDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class StyleAttributeWrapperDTO implements Serializable {
    private Integer attributeId;
    private String attributeName;
    private String attributeResource;
    private String attributeConfigurable;
    private String attributeXmlValue;
    private String attributeValue;
    private String attributeNewValue;
    private String attributeType;
    private boolean isDefault;
    private boolean isUrl;
    private String attributeArguments;
    private String backgroundKey;
    private boolean isInherit;
    private int attributePosition;
    private boolean overwrite;

    public StyleAttributeWrapperDTO() {
        attributeName = null;
        attributeResource = null;
        attributeConfigurable = null;
        attributeXmlValue = null;
        attributeValue = null;
        attributeNewValue = null;
        attributeType = null;
        isDefault = false;
        isUrl = false;
        attributeArguments = null;
        backgroundKey = null;
        isInherit = false;
        attributePosition = -1;
        overwrite = false;
    }

    public StyleAttributeWrapperDTO(String aName, String aResource, String aConfigurable, String aXmlValue, String aValue, String aNewValue, String aType, boolean aIsUrl, boolean aIsDefault, String aArguments, boolean aIsInherit, int aPosition, boolean aOverwrite) {
        attributeId = null;
        attributeName = aName;
        attributeResource = aResource;
        attributeConfigurable = aConfigurable;
        attributeXmlValue = aXmlValue;
        attributeValue = aValue;
        attributeNewValue = aNewValue;
        attributeType = aType;
        isDefault = aIsDefault;
        isUrl = aIsUrl;
        isInherit = aIsInherit;
        attributeArguments = aArguments;
        attributePosition = aPosition;
        overwrite = aOverwrite;

        if (aIsUrl) {
            backgroundKey = UIManagerConstants.IS_MOSAIC_KEY;
        }
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String aName) {
        attributeName = aName;
    }

    public String getAttributeResource() {
        return attributeResource;
    }

    public void setAttributeResource(String aResource) {
        attributeResource = aResource;
    }

    public String getAttributeConfigurable() {
        return attributeConfigurable;
    }

    public void setAttributeConfigurable(String aConfigurable) {
        attributeConfigurable = aConfigurable;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String aValue) {
        attributeValue = aValue;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(String aType) {
        attributeType = aType;
    }

    public String getAttributeNewValue() {
        return attributeNewValue;
    }

    public void setAttributeNewValue(String aNewValue) {
        attributeNewValue = aNewValue;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDef) {
        isDefault = isDef;
    }

    public boolean getIsUrl() {
        return isUrl;
    }

    public void setIsUrl(boolean isUrl) {
        this.isUrl = isUrl;
    }

    public String getAttributeArguments() {
        return attributeArguments;
    }

    public void setAttributeArguments(String aArguments) {
        attributeArguments = aArguments;
    }

    public String getBackgroundKey() {
        return backgroundKey;
    }

    public void setBackgroundKey(String backgroundKey) {
        this.backgroundKey = backgroundKey;
    }

    public String getAttributeXmlValue() {
        return attributeXmlValue;
    }

    public void setAttributeXmlValue(String aXmlValue) {
        attributeXmlValue = aXmlValue;
    }

    public boolean getIsInherit() {
        return isInherit;
    }

    public void setIsInherit(boolean isInherit) {
        this.isInherit = isInherit;
    }

    public int getAttributePosition() {
        return attributePosition;
    }

    public void setAttributePosition(int aPosition) {
        attributePosition = aPosition;
    }

    public boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean aOverwrite) {
        overwrite = aOverwrite;
    }

}
