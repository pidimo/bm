package com.piramide.elwis.web.common.dynamicsearch.util;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.structure.DynamicSearchStructure;
import com.piramide.elwis.web.common.dynamicsearch.xml.BuildSearchStructureOfXml;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchFactory {
    private final Log log = LogFactory.getLog(this.getClass());

    public  static DynamicSearchFactory i = new DynamicSearchFactory();
    private DynamicSearchStructure dynamicSearchStructure;

    public DynamicSearchFactory() {
        dynamicSearchStructure = null;
    }

    public DynamicSearchStructure getDynamicSearchStructure() {
        return dynamicSearchStructure;
    }

    public void initializeStructure(ServletContext context) {
        BuildSearchStructureOfXml buildSearchStructureOfXml = new BuildSearchStructureOfXml();

        try {
            for (int j = 0; j < DynamicSearchConstants.CONFIG_FILES.length; j++) {
                String configFile = DynamicSearchConstants.CONFIG_FILES[j];
                log.debug("Loading dynamic search file:" + configFile);

                InputStream inputStream = context.getResourceAsStream(configFile);
                buildSearchStructureOfXml.buildStructure(inputStream);
            }

            dynamicSearchStructure = buildSearchStructureOfXml.getStructure();
        } catch (Exception e) {
            log.info("Error in initialize dynamic search config:", e);
            throw new RuntimeException("Dynamic search config initialization failed!!", e);
        }
    }
}
