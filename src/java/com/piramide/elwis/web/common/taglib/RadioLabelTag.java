package com.piramide.elwis.web.common.taglib;

import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.jsp.JspException;

/**
 * Tag for input fields of type "radio".
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 * @author David Graham
 * @author modification ALejandro Ruiz
 * @version $Revision: 9703 $ $Date: 2009-09-12 11:46:08 -0400 (Sat, 12 Sep 2009) $
 */
public class RadioLabelTag extends BaseHandlerTag {


    // ----------------------------------------------------- Instance Variables
    /**
     * The message resources for this package.
     */
    protected static MessageResources messages =
            MessageResources.getMessageResources(Constants.Package + ".LocalStrings");


    /**
     * The name of the bean containing our underlying property.
     */
    protected String name = Constants.BEAN_KEY;

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * The property name for this field.
     */
    protected String property = null;


    /**
     * The body content of this tag (if any).
     */
    protected String text = null;


    /**
     * The server value for this option.
     */
    protected String value = null;


    /**
     * Name of the bean (in some scope) that will return the
     * value of the radio tag.
     * <p/>
     * If an iterator is used to render a series of radio tags,
     * this field may be used to specify the name of the bean
     * exposed by the iterator. In this case, the value attribute is
     * used as the name of a property on the <code>idName</code> bean
     * that returns the value of the radio tag in this iteration.
     */
    protected String idName = null;


    // ------------------------------------------------------------- Properties


    /**
     * Return the property name.
     */
    public String getProperty() {

        return (this.property);

    }

    /**
     * Set the property name.
     *
     * @param property The new property name
     */
    public void setProperty(String property) {

        this.property = property;

    }

    /**
     * Return the server value.
     */
    public String getValue() {

        return (this.value);

    }

    /**
     * Set the server value.
     *
     * @param value The new server value
     */
    public void setValue(String value) {

        this.value = value;

    }

    /**
     * Return the idName.
     *
     * @since Struts 1.1
     */
    public String getIdName() {

        return (this.idName);

    }

    /**
     * Set the idName.
     *
     * @param idName The new idName
     * @since Struts 1.1
     */
    public void setIdName(String idName) {

        this.idName = idName;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Generate the required input tag.
     * [Indexed property since Struts 1.1]
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        //For add label support, move this lines to doAfterBody Method
        /*String radioTag = renderRadioElement(serverValue(), currentValue());

        ResponseUtils.write(pageContext, radioTag);*/

        this.text = null;
        return (EVAL_BODY_TAG);
    }

    /**
     * Return the String to be used in the radio tag's <code>value</code> attribute
     * that gets sent to the server on form submission.
     *
     * @throws JspException
     */
    private String serverValue() throws JspException {

        // Not using indexed radio buttons
        if (this.idName == null) {
            return this.value;
        }

        String serverValue = this.lookupProperty(this.idName, this.value);

        return (serverValue == null) ? "" : serverValue;
    }

    /**
     * Acquire the current value of the bean specified by the <code>name</code>
     * attribute and the property specified by the <code>property</code> attribute.
     * This radio button with this value will be checked.
     *
     * @throws JspException
     */
    private String currentValue() throws JspException {
        String current = this.lookupProperty(this.name, this.property);

        return (current == null) ? "" : current;
    }

    /**
     * Renders an HTML &lt;input type="radio"&gt; element.
     *
     * @param serverValue  The data to be used in the tag's <code>value</code>
     *                     attribute and sent to the server when the form is submitted.
     * @param checkedValue If the serverValue equals this value the radio button
     *                     will be checked.
     * @return A radio input element.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String renderRadioElement(String serverValue, String checkedValue)
            throws JspException {


        StringBuffer results = new StringBuffer("<input type=\"radio\"");
        results.append(" name=\"");
        // @since Struts 1.1
        if (indexed) {
            prepareIndex(results, name);
        }
        results.append(this.property);
        results.append("\"");
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }
        results.append(" value=\"");
        results.append(serverValue);
        results.append("\"");
        if (serverValue.equals(checkedValue)) {
            results.append(" checked=\"checked\"");
        }
        results.append(prepareEventHandlers());
        results.append(prepareStyles());
        results.append(getElementClose());
        return results.toString();
    }

    /**
     * Save the associated label from the body content.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (this.bodyContent != null) {
            String value = this.bodyContent.getString().trim();
            if (value.length() > 0) {
                this.text = value;
            }
        }

        return (SKIP_BODY);
    }

    /**
     * Optionally render the associated label from the body content.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        // Render any description for this radio button
        StringBuffer label = null;
        if (this.text != null) {
            label = new StringBuffer();
            if (getStyleId() == null) {
                String id = getProperty().replace('(', '_').replaceAll("\\)", "");
                setStyleId(id);
            }

            label.append("<label for=\"")
                    .append(getStyleId())
                    .append("\">")
                    .append(text)
                    .append("</label>\n");
        }

        String radioTag = renderRadioElement(serverValue(), currentValue());

        ResponseUtils.write(pageContext, radioTag);
        if (label != null) {
            ResponseUtils.write(pageContext, label.toString());
        }


        return (EVAL_PAGE);

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();
        name = Constants.BEAN_KEY;
        property = null;
        text = null;
        value = null;

    }

}
