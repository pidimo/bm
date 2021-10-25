package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.common.urlencrypt.filter.UrlEncryptFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.jsp.JspException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This tag class is used to store the hidden names and values in a map in the parent tag (FormTag), this has the objetive
 * to write all hidden values to the url of the action of the form tag.
 * This process is done because is required to hide the hidden values and atributes names, for security purposes.
 *
 * @author Fernando Montaño
 * @version $Id: HiddenTag.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class HiddenTag extends org.apache.struts.taglib.html.BaseFieldTag {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Construct a new instance of this tag.
     */
    public HiddenTag() {
        super();
        this.type = "hidden";

    }

    // ------------------------------------------------------------- Properties


    /**
     * Should the value of this field also be rendered to the response?
     */
    protected boolean write = false;

    public boolean getWrite() {
        return (this.write);
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    // --------------------------------------------------------- Public Methods


    /**
     * Generate the required input tag, followed by the optional rendered text.
     * Support for <code>write</code> property since Struts 1.1.
     * Additionaly this method was changed to store the hidden values in a map in the form
     *
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        /**
         * if the user defines the styleid (id) attribute it means that he wants to use it with
         * javascript, so in this case the hidden must not be stored to add in the url encoded of the action form.
         */
        FormTag formTag = (FormTag) findAncestorWithClass(this, FormTag.class);
        if (getStyleId() == null && formTag != null && UrlEncryptFilter.ENABLE_URL_ENCRYPT) {
            String result = null;
            if (value != null) {
                result = value;
            } else {
                Object value = RequestUtils.lookup(pageContext, name, property,
                        null);
                if (value == null) {
                    result = "";
                } else {
                    result = value.toString();
                }
            }
            if (write) {
                ResponseUtils.write(pageContext, ResponseUtils.filter(result));
            }
            try {
                result = URLEncoder.encode(result, Constants.CHARSET_ENCODING);
            } catch (UnsupportedEncodingException e) {
                log.error("No charset supported", e);
            }


            if (result != null) {
                formTag.getHiddenProperties().put(property, result);
            }

        }

        /**
         * if the user defines the styleid attribute it means that he wants to use it with
         * javascript, so in this case the hidden must be writed.
         */

        else {

            super.doStartTag();// Render the <html:input type="hidden"> tag as before

            // Is rendering the value separately requested?
            if (!write) {
                return (EVAL_BODY_TAG);
            }
            // Calculate the value to be rendered separately
            // * @since Struts 1.1
            String results = null;
            if (value != null) {
                results = ResponseUtils.filter(value);
            } else {
                Object value = RequestUtils.lookup(pageContext, name, property,
                        null);
                if (value == null) {
                    results = "";
                } else {
                    results = ResponseUtils.filter(value.toString());
                }
            }

            // Render the result to the output writer
            ResponseUtils.write(pageContext, results);
        }
        return (EVAL_BODY_TAG);

    }


    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        write = false;

    }
}
