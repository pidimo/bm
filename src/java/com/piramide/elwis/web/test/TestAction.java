package com.piramide.elwis.web.test;

import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.SysLevelException;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ernesto
 * @version TestAction.java, v 2.0 Aug 24, 2004 5:09:51 PM
 */
public class TestAction extends DefaultAction {
    protected final Log log = LogFactory.getLog(getClass());


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("-------------- TESTING COMMANDS !!!!!!");


        String commandName = "com.piramide.elwis.cmd." + (String) request.getParameter("command") + "Cmd";
        String[] params = request.getParameterValues("param");
        String[] values = request.getParameterValues("value");
        ParamDTO paramDTO = new ParamDTO();
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                String par = params[i];
                /* par = "dto("+par+")";*/
                log.debug("PARAMETER IN TEST: " + par + " Value=" + values[i]);
                paramDTO.put(par, values[i]);
            }
        }
        if (commandName != null) {
            try {
                EJBCommand cmd = instantiateEJBCommand(commandName);
                log.debug("-------------- TESTING COMMANDS from Busisnesss DElegate !!!!!!");
                cmd.putParam(paramDTO);
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                resultDTO.putAll(paramDTO);
                if (resultDTO.isFailure()) {
                    return mapping.findForward("fail");
                }
                resultDTO.put("result", "ok");
                request.setAttribute(KEY_REQDTO, resultDTO);
            } catch (Exception e) {
                request.setAttribute(KEY_REQDTO, new HashMap(paramDTO));
                return mapping.findForward("fail");
            }

        }


        return mapping.findForward("success");
    }

    private EJBCommand instantiateEJBCommand(String className) {

        //create EJBCommand instance
        try {
            return (EJBCommand) Class.forName(className).newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SysLevelException(
                    "TEST: ---------------------- > EJBCommand class not found: " + className);
        } catch (InstantiationException e) {
            throw new SysLevelException(e);
        } catch (IllegalAccessException e) {
            throw new SysLevelException(e);
        }
    }

    private static Map getOrCreateReqScopeDTO(HttpServletRequest req) {

        Map reqDTO = (Map) req.getAttribute(KEY_REQDTO);
        if (reqDTO == null) {
            reqDTO = new HashMap();
            req.setAttribute(KEY_REQDTO, reqDTO);
        }
        return reqDTO;
    }


}
