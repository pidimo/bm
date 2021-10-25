package com.piramide.elwis.web.dashboard.component.web.util;

import com.piramide.elwis.web.dashboard.component.util.ResourceReader;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

/**
 * @author : ivan
 */
public class ElUtils {
    public static Object evaluate(String exp,
                                  String returnClassName, JspContext jspContext)
            throws JspException {
        return evaluate(exp, returnClassName,
                jspContext, null);
    }

    public static Object evaluate(String expression,
                                  String returnClassName, JspContext jspContext,
                                  FunctionMapper functionMapper)
            throws JspException {
        try {
            ExpressionEvaluator ev = jspContext.getExpressionEvaluator();
            VariableResolver variableResolver = jspContext.getVariableResolver();

            return ev.evaluate(
                    expression, ResourceReader.getClass(returnClassName),
                    variableResolver, functionMapper);
        } catch (ELException x) {
            throw new JspException(x);
        }
    }
}
