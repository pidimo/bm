package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.exception.CreateDocumentException;
import com.softartisans.wordwriter.WordTemplate;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Document generation variables constants.
 * Used in document generation.
 *
 * @author Tayes
 * @version $Id: GenerateDocument.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class GenerateDocument {


    private Log log = LogFactory.getLog(this.getClass());
    private static final String REPEAT_BLOCK_CAMPAIGN = "campaign";
    private static final String REPEAT_BLOCK_SALESPROCESS = "actionpositions";
    private static final String REPEAT_BLOCK_INVOICE_POSITION = "invoicepositions";
    private static final String REPEAT_BLOCK_INVOICE_VAT = "invoicevats";
    private boolean isAction;

    public GenerateDocument() {
        isAction = false;
    }

    public void setAction(Boolean action) {
        isAction = action.booleanValue();
    }

    private CreateDocumentException getError(String msg) {
        log.debug("Catch WordWriterExeption:" + msg);
        CreateDocumentException exception = new CreateDocumentException(msg);
        String[] errors = {"Word 6", "campaign", "data source:"};
        String[] keyErrors = {"Document.error.wordVersion", "Document.error.campaign", "Document.error.invalidVariable"};
        String[] argDelimiter = {null, null, "\""};
        boolean[] hasArg = {false, false, true};
        String result;
        for (int i = 0; i < errors.length; i++) {
            String error = errors[i];
            if (msg.indexOf(error) > 0) {
                result = keyErrors[i];
                exception = new CreateDocumentException(result);

                if (hasArg[i]) {
                    log.debug("Has parameters:" + i);
                    int ini = msg.indexOf(argDelimiter[i]);
                    if (ini > 0) {
                        log.debug("Found parameter in:" + ini);
                        String arg = msg.substring(ini, msg.indexOf(argDelimiter[i], ini + 1) + 1);
                        exception.setArg1(arg);
                    }
                }
            }
        }
        return exception;
    }


    public byte[] renderCampaignDocument(Object[][] values, String[] names, byte[] template) throws CreateDocumentException {

        try {
            return generateCampaignDoc(template, values, names);

        } catch (Exception e) {
            throw getError(e.getMessage());
        }
    }


    public byte[] renderNormalDocument(Object[] values, String[] fieldNames, byte[] template) throws CreateDocumentException {
        log.debug("[RenderDocument]Init..");
        try {
            return generateNormalWord(template, values, fieldNames);

        } catch (Exception e) {
            throw getError(e.getMessage());
        }
    }


    public byte[] renderSalesProcessDocument(Object[] values, String[] fieldNames, Object[][] repeat, byte[] template) throws CreateDocumentException {
        log.debug("[RenderDocument]Init..");
        try {
            return generateSalesProcessDoc(template, values, fieldNames, repeat);
        } catch (Exception e) {
            throw getError(e.getMessage());
        }
    }

    /**
     * Method for generate un SalesProcess Document
     *
     * @param template
     * @param values
     * @param fieldNames
     * @param repeat
     * @return
     * @throws Exception
     */

    private byte[] generateSalesProcessDoc(byte[] template, Object[] values, String[] fieldNames, Object[][] repeat) throws Exception {
        log.debug("FIELD_NAMES:" + Arrays.asList(fieldNames));
        log.debug("FIELD_VALUES:" + Arrays.asList(values));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WordTemplate oWW;
        InputStream is = new ByteArrayInputStream(template);
        oWW = new WordTemplate();
        oWW.open(is);
        oWW.setDataSource(values, fieldNames);
        oWW.setRepeatBlock(repeat, VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS, REPEAT_BLOCK_SALESPROCESS, -1, true);
        oWW.process();
        oWW.save(outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Generate normal document
     *
     * @param template
     * @param values
     * @param fieldNames
     * @return
     * @throws Exception
     */

    private byte[] generateNormalWord(byte[] template, Object[] values, String[] fieldNames) throws Exception {
        log.debug("FIELD_NAMES:" + Arrays.asList(fieldNames));
        log.debug("FIELD_VALUES:" + Arrays.asList(values));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WordTemplate oWW;
        InputStream is = new ByteArrayInputStream(template);
        oWW = new WordTemplate();
        oWW.open(is);
        oWW.setDataSource(values, fieldNames);
        oWW.process();
        oWW.save(outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Generate campaign document
     *
     * @param template
     * @param values
     * @param names
     * @return
     * @throws Exception
     */

    private byte[] generateCampaignDoc(byte[] template, Object[][] values, String[] names) throws Exception {
        log.debug("FIELD_NAMES:" + Arrays.asList(names));
        log.debug("FIELD_VALUES:" + Arrays.asList(values));

        Object[] var1 = values[0];
        Object[] var = new Object[names.length];

        log.debug("Size variable:" + var.length);
        for (int i = 0; i < names.length; i++) {
            var[i] = var1[i];
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WordTemplate oWW;
        InputStream is = new ByteArrayInputStream(template);
        oWW = new WordTemplate();
        oWW.open(is);
        //oWW.setDataSource(var, VariableConstants.FIELDS_NOACTIONS);
        oWW.setDataSource(var, names);
        oWW.setRepeatBlock(values, names, REPEAT_BLOCK_CAMPAIGN, -1, true);
        oWW.process();
        oWW.save(outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Method to generate invoice related documents (invoice, invoice reminders)
     *
     * @param values
     * @param fieldNames
     * @param positionRepeat
     * @param invoiceVatRepeat
     * @param template
     * @return
     * @throws CreateDocumentException
     */
    public byte[] renderInvoiceDocument(Object[] values, String[] fieldNames, Object[][] positionRepeat, Object[][] invoiceVatRepeat, byte[] template) throws CreateDocumentException {
        log.debug("[RenderDocument]Init..");
        try {
            return generateInvoiceDoc(template, values, fieldNames, positionRepeat, invoiceVatRepeat);
        } catch (Exception e) {
            log.debug("Error in generate INVOICE document..." + e.getMessage());
            if (e.getMessage().indexOf(REPEAT_BLOCK_INVOICE_POSITION) > 0) {
                //bookmark (invoice position table) for invoice positions is not defined
                return renderInvoiceDocument(values, fieldNames, null, invoiceVatRepeat, template);
            } else if (e.getMessage().indexOf(REPEAT_BLOCK_INVOICE_VAT) > 0) {
                //bookmark (vats table) for vats is not defined
                return renderInvoiceDocument(values, fieldNames, positionRepeat, null, template);
            } else {
                throw getError(e.getMessage());
            }
        }
    }

    /**
     * Generate document (invoice, invoice reminder)
     *
     * @param template
     * @param values
     * @param fieldNames
     * @param positionRepeat
     * @param invoiceVatRepeat
     * @return
     * @throws Exception
     */
    private byte[] generateInvoiceDoc(byte[] template, Object[] values, String[] fieldNames, Object[][] positionRepeat, Object[][] invoiceVatRepeat) throws Exception {
        log.debug("FIELD_NAMES:" + Arrays.asList(fieldNames));
        log.debug("FIELD_VALUES:" + Arrays.asList(values));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        WordTemplate oWW;
        InputStream is = new ByteArrayInputStream(template);
        oWW = new WordTemplate();
        oWW.open(is);
        oWW.setDataSource(values, fieldNames);

        if (positionRepeat != null) {
            oWW.setRepeatBlock(positionRepeat, VariableConstants.INVOICE_POSITION_FIELDS, REPEAT_BLOCK_INVOICE_POSITION, -1, true);
        }
        if (invoiceVatRepeat != null) {
            oWW.setRepeatBlock(invoiceVatRepeat, VariableConstants.INVOICE_VAT_FIELDS, REPEAT_BLOCK_INVOICE_VAT, -1, true);
        }

        oWW.process();
        oWW.save(outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Specifies the host for the office server.
     */
    private static final String stringHost = "localhost";

    /**
     * Specifies the port for the office server.
     */
    private static final String stringPort = "8100";


    public void convertDocument(String template, String name) throws Exception {
        String stringUrl = "file://" + template;
        String path = "file://" + name;
        // Converting the document to the favoured type

/* Bootstraps a component context with the jurt base components registered. Component context to be granted to a component for running. Arbitrary values can be retrieved from the context. */
        XComponentContext xcomponentcontext = com.sun.star.comp.helper.Bootstrap.createInitialComponentContext(null);

/* Gets the service manager instance to be used (or null). This method has been added for convenience, because the service manager is a often used object. */
        XMultiComponentFactory xmulticomponentfactory = xcomponentcontext.getServiceManager();

/* Creates an instance of the component UnoUrlResolver which supports the services specified by the factory. */
        Object objectUrlResolver = xmulticomponentfactory.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", xcomponentcontext);

// Create a new url resolver
        XUnoUrlResolver xurlresolver = (XUnoUrlResolver) UnoRuntime.queryInterface(XUnoUrlResolver.class, objectUrlResolver);

// Resolves an object that is specified as follow: // uno:<connection description>;<protocol description>;<initial object name>

        Object objectInitial = xurlresolver.resolve("uno:socket,host=" + stringHost + ",port=" + stringPort + ";urp;StarOffice.ServiceManager");

// Create a service manager from the initial object
        xmulticomponentfactory = (XMultiComponentFactory) UnoRuntime.queryInterface(XMultiComponentFactory.class, objectInitial);

// Query for the XPropertySet interface.
        XPropertySet xpropertysetMultiComponentFactory = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xmulticomponentfactory);

// Get the default context from the office server.
        Object objectDefaultContext = xpropertysetMultiComponentFactory.getPropertyValue("DefaultContext");

// Query for the interface XComponentContext.
        xcomponentcontext = (XComponentContext) UnoRuntime.queryInterface(XComponentContext.class, objectDefaultContext);

/* A desktop environment contains tasks with one or more frames in which components can be loaded. Desktop is the environment for components which can instanciate within frames. */
        XComponentLoader xcomponentloader = (XComponentLoader) UnoRuntime.queryInterface(
                XComponentLoader.class, xmulticomponentfactory.createInstanceWithContext("com.sun.star.frame.Desktop", xcomponentcontext));

// Preparing properties for loading the document
        PropertyValue propertyvalue[] = new PropertyValue[2];
// Setting the flag for hidding the open document
        propertyvalue[0] = new PropertyValue();
        propertyvalue[0].Name = "Hidden";
        propertyvalue[0].Value = Boolean.TRUE;

// Setting the flag for hidding the open document
        propertyvalue[1] = new PropertyValue();
        propertyvalue[1].Name = "UpdateDocMode";
        propertyvalue[1].Value = new Integer(1);

        /*propertyvalue[1].Name = "InputStream";
     propertyvalue[1].Value = new XByteArrayInputStream(file);*/

        log.debug("Converting" + path);

// Loading the wanted document
        Object objectDocumentToStore = xcomponentloader.loadComponentFromURL(stringUrl, "_blank", 0, propertyvalue);
        //Object objectDocumentToStore = xcomponentloader.loadComponentFromURL("private:stream/", "_blank", 0, propertyvalue);
        log.debug("OBJECT:" + objectDocumentToStore);
// Getting an object that will offer a simple way to store a document to a URL.
        XStorable xstorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, objectDocumentToStore);

// Preparing properties for converting the document
        propertyvalue = new PropertyValue[2];
// Setting the flag for overwriting
        propertyvalue[0] = new PropertyValue();
        propertyvalue[0].Name = "Overwrite";
        propertyvalue[0].Value = Boolean.TRUE;
        // Setting the filter name
        propertyvalue[1] = new PropertyValue();
        propertyvalue[1].Name = "FilterName";
        //propertyvalue[1].Value = stringConvertType;
        propertyvalue[1].Value = "swriter: HTML (StarWriter)";

        log.debug("Convert to type HTMLto file" + path);
// Storing and converting the document
        xstorable.storeToURL(path, propertyvalue);
        XCloseable xcloseable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, xstorable);
        // Closing the converted document
        if (xcloseable != null) {
            xcloseable.close(false);
        } else {
            // If Xcloseable is not supported (older versions, // use dispose() for closing the document
            XComponent xComponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, xstorable);
            xComponent.dispose();
        }
    }
}
