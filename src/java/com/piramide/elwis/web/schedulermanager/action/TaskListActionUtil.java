package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.ListAction;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: TaskListBaseAction.java 28-ene-2009 18:49:22
 */
public class TaskListActionUtil {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * This method manages the filter behavior for All | Not Closed tasks
     *
     * @param request         The HttpServletRequest
     * @param form            The ActionForm
     * @param listName        The listaName to search For Persistence
     * @param listModuleName  the list moduleName to search for default state
     * @param parameterIdName the param to search for default state
     * @param moduleName      the actual module name
     * @param listAction      the action from this is called
     */
    public void processFilterForTaskList(HttpServletRequest request,
                                         ActionForm form,
                                         String listName,
                                         String listModuleName,
                                         String parameterIdName,
                                         String moduleName,
                                         ListAction listAction) {
        log.debug("Processing the taskShowStatus filter..............................");
        User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);
        Map map = (Map) request.getSession().getAttribute("Fantabulous.Modules");
        Map listParameters = PersistenceManager.persistence().loadStatus(user.getValue("userId").toString(),
                listName, moduleName);
        SearchForm searchForm = (SearchForm) form;
        if (map != null && parameterIdName != null && !request.getParameter(parameterIdName).equals(map.get("/" + listModuleName))) {
            //log.debug("The list state must be restarted (to the default status)...");
            listAction.addFilter("concluded_status", SchedulerConstants.CONCLUDED);
            request.setAttribute("setDefaultStatusInView", "true");
        } else {
            //log.debug("The list state dont need to be restarted......");
            if (request.getParameterMap().containsKey("fromSearchForm")) {
                //log.debug("Coming from the task search form (a submit)");
                if (request.getParameterMap().containsKey("ByLetter(titleAlphabet)")) { //is an alphabet search?
                    //log.debug("Is an alphabet search...");
                    listAction.addFilter("concluded_status", SchedulerConstants.CONCLUDED);
                    request.setAttribute("setDefaultStatusInView", "true");
                } else {
                    //log.debug("Is not al alphabet search....");
                    listAction.addFilter("concluded_status", searchForm.getParameter("concluded_status").toString());
                }
            } else {
                //log.debug("Coming from outside the form...... (a link)");
                if (listParameters.get("ByLetter") != null) {
                    //log.debug("And I have ByLetter in persistence....");
                    listAction.addFilter("concluded_status", SchedulerConstants.CONCLUDED);
                    request.setAttribute("setDefaultStatusInView", "true");
                } else {
                    //log.debug("I dont have ByLetter in persistence");
                    if (listParameters.get("concluded_status") != null) {
                        //log.debug("But I have concluded_status in persistence......");
                        listAction.addFilter("concluded_status", listParameters.get("concluded_status").toString());
                    } else {
                        listAction.addFilter("concluded_status", SchedulerConstants.CONCLUDED);
                        request.setAttribute("setDefaultStatusInView", "true");
                    }
                }

            }
        }
    }
}


/* LEGACY CODE (Only to remember.... sadly remember....)
if(SchedulerConstants.TRUE_VALUE.equals(request.getParameter("simple"))){
            SearchForm searchForm= (SearchForm) form;
            User user = (User) request.getSession(false).getAttribute(Constants.USER_KEY);
            Map map = (Map) request.getSession().getAttribute("Fantabulous.Modules");
            boolean executed=false;
            if (map != null && !request.getParameter(parameterIdName).equals(map.get("/"+listModuleName))) { //defining default value when is showing another contact
                searchForm.setParameter("taskShowStatus", SchedulerConstants.SHOW_NOT_CLOSED);
                executed=true;
            }
            if(!executed || moduleName.equals("scheduler")){
                Map listParameters = PersistenceManager.persistence().loadStatus(user.getValue("userId").toString(),
                            listName, moduleName);


                if(listParameters.containsKey("ByLetter")){
                    String byLetterFilter=listParameters.get("ByLetter").toString();
                    String letra=byLetterFilter.substring(byLetterFilter.lastIndexOf("_"));
                    searchForm.setParameter("ByLetter",letra);
                }
                else if(!request.getParameterMap().containsKey("ByLetter(title)")){
                    if(searchForm.getParameter("taskShowStatus")==null){
                        if(!listParameters.containsKey("taskShowStatus"))
                            searchForm.setParameter("taskShowStatus", SchedulerConstants.SHOW_NOT_CLOSED);
                        else{
                            searchForm.setParameter("taskShowStatus",listParameters.get("taskShowStatus"));
                        }
                        if(listParameters.containsKey("title"))
                            searchForm.setParameter("title",listParameters.get("title"));
                    }
                }
            }
            searchForm.setParameter("concluded_status","3");
       }
*/