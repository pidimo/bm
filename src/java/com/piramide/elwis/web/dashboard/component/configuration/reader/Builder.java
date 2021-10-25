package com.piramide.elwis.web.dashboard.component.configuration.reader;


import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author : ivan
 */
public class Builder {
    private Log log = LogFactory.getLog(this.getClass());

    public static Builder i = new Builder();

    private BuildStructure build = null;
    private List componentList = new ArrayList();


    private Builder() {
    }


    public Component findComponentById(int id) {
        for (int i = 0; i < componentList.size(); i++) {
            Component component = (Component) componentList.get(i);

            if (id == component.getId()) {
                return component;
            }
        }

        return null;
    }

    public void initialize(String xmlFilePath)
            throws IOException, JDOMException {
        log.debug("initialize('" + xmlFilePath + "')");
        build = new BuildStructure(xmlFilePath);
        componentList = build.createStructure();
    }

    public void initialize(InputStream inputStream)
            throws IOException, JDOMException {
        log.debug("initialize(java.io.InputStream)");
        build = new BuildStructure(inputStream);
        componentList = build.createStructure();
    }


    public List getComponentList() {
        return componentList;
    }

}
