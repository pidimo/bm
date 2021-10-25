package com.piramide.elwis.cmd.admin.copycatalog.util;

import com.piramide.elwis.cmd.admin.copycatalog.util.structure.Catalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.structure.Module;
import com.piramide.elwis.utils.ResourceLoader;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ivan
 */
public class BuildCatalogStructure {
    private ReadXmlDefinition reader;

    public BuildCatalogStructure() throws IOException, JDOMException {
        InputStream xmlStream = null;
        xmlStream = ResourceLoader.getResourceAsStream("copycatalog.xml", this.getClass());
        reader = new ReadXmlDefinition(xmlStream);
        xmlStream.close();
    }

    public List<Module> buildStructure() {
        List<Module> result = new ArrayList<Module>();
        List elements = reader.getRootElement().getChildren();

        for (Object obj : elements) {
            Element element = (Element) obj;
            Module module = new Module();
            String modulePath = element.getAttributeValue(ReadXmlDefinition.Module.attrPath.getConstant());
            String moduleId = element.getAttributeValue(ReadXmlDefinition.Module.attrModuleId.getConstant());
            List<Catalog> catalogs = buildCatalogs(element);
            module.setPath(modulePath);
            if (null != moduleId && !"".equals(moduleId.trim())) {
                module.setModuleId(moduleId);
            }

            module.setCatalogs(catalogs);
            result.add(module);
        }

        return result;
    }

    private List<Catalog> buildCatalogs(Element parentElement) {
        List elements = reader.getChildrenOfElementX(ReadXmlDefinition.Catalog.elementName.getConstant(), parentElement);
        List<Catalog> result = new ArrayList<Catalog>();
        for (Object obj : elements) {
            Element element = (Element) obj;
            String className = element.getAttributeValue(ReadXmlDefinition.Catalog.attrClassName.getConstant());
            Catalog catalog = new Catalog();
            catalog.setCopyClassName(className);
            result.add(catalog);
        }
        return result;
    }
}
