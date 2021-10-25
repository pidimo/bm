package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.MailAccountCmd;
import com.piramide.elwis.dto.webmailmanager.MailAccountDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PageSearchUtil;
import com.piramide.elwis.utils.PagingPair;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.persistence.PersistenceConstants;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.result.FieldHash;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: ReadMailAction.java 10480 2014-08-14 18:52:29Z miguel $
 * @deprecated mail state has been updated as bit code
 */
public class ReadMailAction extends WebmailDefaultAction {
    private Log log = LogFactory.getLog(this.getClass());
    private static final String SEPARATOR = ",";
    private static final String MODULE = "webmail";
    private static final String MODULE_WEB = "/webmail";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userMailId = (Integer) user.getValue("userId");
        Integer companyId = (Integer) user.getValue("companyId");

        DefaultForm f = (DefaultForm) form;
        try {
            setMailPagination(request, f);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        super.execute(mapping, form, request, response);

        boolean isHtml = false;

        //scrollMail(request, mapping, f);
        Object redirectType = f.getDto("redirectType");
        Object isDraft = f.getDto("isDraft");

        String to = JSPHelper.getName_Emails((ArrayList) f.getDto("mailTos"));
        String cc = JSPHelper.getName_Emails((ArrayList) f.getDto("mailTosCC"));
        String bcc = JSPHelper.getName_Emails((ArrayList) f.getDto("mailTosBCC"));
        String from = JSPHelper.getName_Email((HashMap) f.getDto("mailFrom"));

        if (redirectType != null && isDraft == null) {
            buildReplyOrForwardHeader(userMailId, f, companyId, request);

        } else {
            f.setDto("to", to);
            f.setDto("cc", cc);
            f.setDto("bcc", bcc);
            f.setDto("from", from);
            if (isDraft != null && isDraft.equals("true")) {    //FOR A DRAFT
                f.setDto("mailFrom", null != f.getDto("mailAccountId") ? f.getDto("mailAccountId") : "");
                //f.setDto("redirect", WebMailConstants.MAIL_STATE_FORWARD);
                String bodyType = f.getDto("bodyType").toString();

                MailFormHelper helper = new MailFormHelper();
                helper.readUserConfiguration(userMailId, request, f);
                helper.setMailAccountDefaultSetting(f.getDto("mailAccountId"), request, f);

                if (WebMailConstants.BODY_TYPE_HTML.equals(bodyType)) {
                    request.setAttribute("dto(html)", "true");
                } else {
                    request.setAttribute("dto(html)", "false");
                }
            }
        }

        return (mapping.findForward("Success"));
    }

    private void buildReplyOrForwardHeader(Integer userMailId,
                                           DefaultForm defaultForm,
                                           Integer companyId,
                                           HttpServletRequest request) {
        DefaultForm form = new DefaultForm();
        MailFormHelper emailHelper = new MailFormHelper();
        emailHelper.readUserConfiguration(userMailId, request, form);
        emailHelper.setMailAccountDefaultSetting(defaultForm.getDto("mailAccountId"), request, form);

        Boolean useHtmlEditor = (Boolean) form.getDto("editMode");
        Boolean replyOrForwardMessagesInTheSameFormat = (Boolean) form.getDto("replyMode");

        boolean isHtml = false;
        String redirectType = (String) defaultForm.getDto("redirectType");
        String bodyType = String.valueOf(defaultForm.getDto("bodyType"));
        String body = (String) defaultForm.getDto("body");

        if (replyOrForwardMessagesInTheSameFormat) {
            isHtml = WebMailConstants.BODY_TYPE_HTML.equals(bodyType);
        } else {
            isHtml = WebMailConstants.BODY_TYPE_HTML.equals(bodyType) || useHtmlEditor;
        }

        if (isHtml) {
            body = processHTMLBody(body, redirectType, bodyType, defaultForm, request);
        } else {
            body = processTEXTBody(body, redirectType, defaultForm, request);
        }

        defaultForm.setDto("body", body);

        String from = JSPHelper.getName_Email((HashMap) defaultForm.getDto("mailFrom"));
        String ReStr = JSPHelper.getMessage(request, "Webmail.compose.replyShort");
        String FwStr = JSPHelper.getMessage(request, "Webmail.compose.forwardShort");
        String email = (String) defaultForm.getDto("mailAccount");

        searchAccountByEmail(WebMailConstants.MailOperations.REPLY.getOperation(),
                email, userMailId, companyId, request, defaultForm);

        if (redirectType.equals("FORWARD")) {
            defaultForm.setDto("subject", FwStr + ": " + defaultForm.getDto("subject"));
            defaultForm.setDto("to", "");
            defaultForm.setDto("cc", "");
            //defaultForm.setDto("redirect", WebMailConstants.MAIL_STATE_FORWARD);
        } else if (redirectType.equals("REPLY")) {
            defaultForm.setDto("subject", ReStr + ": " + defaultForm.getDto("subject"));
            //defaultForm.setDto("redirect", WebMailConstants.MAIL_STATE_ANSWERED);
            defaultForm.setDto("to", from);
            defaultForm.setDto("cc", "");
        } else if (redirectType.equals("REPLYALL")) {
            HashMap fromMap = (HashMap) defaultForm.getDto("senderFrom");
            defaultForm.setDto("subject", ReStr + ": " + defaultForm.getDto("subject"));
            //defaultForm.setDto("redirect", WebMailConstants.MAIL_STATE_ANSWERED);
            defaultForm.setDto("to",
                    replyAllRecipients((ArrayList) defaultForm.getDto("mailTos"),
                            new ArrayList(), fromMap, defaultForm.getDto("userEmail"))
            );
            defaultForm.setDto("cc",
                    replyAllRecipients((ArrayList) defaultForm.getDto("mailTosCC"), new ArrayList(), new HashMap(), defaultForm.getDto("userEmail")));
        }
    }

    private String processTEXTBody(String body,
                                   String redirectType,
                                   DefaultForm defaultForm,
                                   HttpServletRequest request) {
        if (redirectType.equals("REPLY") || redirectType.equals("REPLYALL")) {
            body = ">" + body.replaceAll("\n", "\n>");
        }

        String from = JSPHelper.getName_Email((HashMap) defaultForm.getDto("mailFrom"));
        String to = JSPHelper.getName_Emails((ArrayList) defaultForm.getDto("mailTos"));
        String cc = JSPHelper.getName_Emails((ArrayList) defaultForm.getDto("mailTosCC"));
        String subject = (String) defaultForm.getDto("subject");
        String sentDate = String.valueOf(defaultForm.getDto("sentDate"));

        String redirectHeader = JSPHelper.createRedirectHeader(request, from, to, cc, subject, sentDate, false);

        return redirectHeader + body;
    }

    private String processHTMLBody(String body,
                                   String redirectType,
                                   String bodyType,
                                   DefaultForm form,
                                   HttpServletRequest request) {

        if (WebMailConstants.BODY_TYPE_TEXT.equals(bodyType)) {
            Pattern pattern = Pattern.compile("(\\n\\r|\\n)");
            Matcher matcher = pattern.matcher(body);
            body = matcher.replaceAll("<br>");
        }

        if (redirectType.equals("REPLY") || redirectType.equals("REPLYALL")) {
            body = "<blockquote style=\"PADDING-LEFT: 5px; MARGIN-LEFT: 5px; BORDER-LEFT: #003366 2px solid\">" +
                    body +
                    "</blockquote>";
        }

        String from = JSPHelper.getName_Email((HashMap) form.getDto("mailFrom"));
        String to = JSPHelper.getName_Emails((ArrayList) form.getDto("mailTos"));
        String cc = JSPHelper.getName_Emails((ArrayList) form.getDto("mailTosCC"));
        String subject = (String) form.getDto("subject");
        String sentDate = String.valueOf(form.getDto("sentDate"));

        String redirectHeader = JSPHelper.createRedirectHeader(request, from, to, cc, subject, sentDate, true);

        return redirectHeader + body;
    }

    private void searchAccountByEmail(String operation,
                                      String email,
                                      Integer userMailId,
                                      Integer companyId,
                                      HttpServletRequest request, DefaultForm form) {
        MailFormHelper helper = new MailFormHelper();

        helper.readUserConfiguration(userMailId, request, form);

        MailAccountCmd accountCmd = new MailAccountCmd();
        accountCmd.setOp("searchAccountByEmail");
        accountCmd.putParam("userMailId", userMailId);
        accountCmd.putParam("companyId", companyId);
        accountCmd.putParam("email", email);
        accountCmd.putParam("operation", operation);

        ResultDTO accountResult = null;

        try {
            accountResult = BusinessDelegate.i.execute(accountCmd, request);
        } catch (AppLevelException e) {
            log.debug("Cannot execute MailAccountCmd, ");
        }
        MailAccountDTO accountDTO = (MailAccountDTO) accountResult.get("mailAccount");
        if (null != accountDTO) {
            form.setDto("mailFrom", accountDTO.get("mailAccountId"));
            form.setDto("mailAccountId", accountDTO.get("mailAccountId"));

            helper.setMailAccountDefaultSetting(accountDTO.get("mailAccountId"), request, form);

            if (null == form.getDto("signature")) {
                helper.setDefaultSignature(Integer.valueOf(accountDTO.get("mailAccountId").toString()),
                        userMailId,
                        form,
                        request);
            }
        } else {
            form.setDto("mailFrom", "");
        }
    }

    private String replyAllRecipients(ArrayList a1, ArrayList a2, HashMap hm, Object userEmail) {
        ArrayList recipients = new ArrayList();
        String res = "";
        Iterator i = a1.iterator();
        while (i.hasNext()) {
            HashMap hm_i = (HashMap) i.next();
            if (!recipients.contains(hm_i)) {
                recipients.add(hm_i);
            }
        }
        i = a2.iterator();
        while (i.hasNext()) {
            HashMap hm_i = (HashMap) i.next();
            if (!recipients.contains(hm_i)) {
                recipients.add(hm_i);
            }
        }
        if (!recipients.contains(hm)) {
            recipients.add(hm);
        }
        //Remove me of the list
        if (userEmail != null && recipients.size() > 0) {
            ArrayList newRecipients = new ArrayList();
            for (int k = 0; k < recipients.size(); k++) {
                HashMap hm_k = new HashMap();
                hm_k = (HashMap) recipients.get(k);
                if (hm_k.get("email") != null && !hm_k.get("email").toString().equals(userEmail.toString())) {
                    newRecipients.add(hm_k);
                }
            }
            recipients = newRecipients;
        }

        res = JSPHelper.getName_Emails(recipients);

        return (res);
    }

    /**
     * Gets and set the attributes needed for the pagination
     *
     * @param request
     * @param form
     * @throws Exception
     */
    private void setMailPagination(HttpServletRequest request, DefaultForm form) throws Exception {
        String currentMailId = (String) form.getDto("mailId");
        Integer mailIndex = request.getParameter("mailIndex") != null ? Integer.parseInt(request.getParameter("mailIndex")) : null;

        PageSearchUtil pageSearchUtil = new PageSearchUtil();
        if (currentMailId != null && mailIndex != null) {
            int pageSize = pageSearchUtil.getGroupSize(mailIndex);
            int pageNumber = pageSearchUtil.getGroup(pageSize, mailIndex);

            log.debug("==>Search from database:");
            ListStructure listStructure;
            String listName = "true".equals(request.getParameter("mailSearch")) ? "searchMailList" : "mailTrayList";
            if ((listStructure = FantabulousUtil.getInstance().getList(listName, MODULE_WEB, getServlet().getServletContext())) == null) {
                return;
            }

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            String userId = user.getValue("userId").toString();
            Map parametersLoaded = PersistenceManager.persistence().loadStatus(userId, listStructure.getListName(), MODULE);
            parametersLoaded.put("userMailId", userId);
            log.debug("Parameters:" + parametersLoaded);
            Parameters parameters = new Parameters();
            if (parametersLoaded.size() > 0) {
                // Extract order parameter
                Map<Integer, OrderParam> orderParameters = new TreeMap<Integer, OrderParam>();
                if (parametersLoaded.containsKey(PersistenceConstants.PARAM_ORDER)) {
                    orderParameters = FantabulousUtil.getInstance().getOrderParams((String) parametersLoaded.remove(PersistenceConstants.PARAM_ORDER));
                }

                // The rest is only search parameters...

                // Execute List with search params and filters..
                parameters.getPageParam().setPageSize(pageSize);
                // Set page for list
                parameters.getPageParam().setPageNumber(pageNumber);
                // Order parameters
                if (!orderParameters.isEmpty()) {
                    parameters.setOrderParameters(new ArrayList(orderParameters.values()));
                }
                // Set search parameters...
                parameters.addSearchParameters(FantabulousUtil.getInstance().getParameters(parametersLoaded));
                //parameters.getFieldsThatBeginWithWord().addAll(getBeginWithParameters(searchForm.getBeginsWithParams()));
            }
            Collection listFiedls = Controller.getPage(listStructure, listStructure.getListName(), parameters);
            PagingPair<Integer> pair = findPagingParams(new ArrayList(listFiedls), Integer.parseInt(currentMailId), pageNumber, pageSize);

            if (pair.getPrevious() != null) {
                request.setAttribute("previousMailId", pair.getPrevious());
            }
            if (pair.getNext() != null) {
                request.setAttribute("nextMailId", pair.getNext());
            }
            if (pair.getIndex() != Integer.MIN_VALUE) {
                request.setAttribute("previousMailIndex", pair.getIndex() - 1);
                request.setAttribute("nextMailIndex", pair.getIndex() + 1);
            }
        }
    }

    /**
     * Find the previous and next mailids in the list
     *
     * @param fields
     * @param currentMailId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    private PagingPair<Integer> findPagingParams(List fields, int currentMailId, int pageNumber, int pageSize) {
        PagingPair<Integer> res = new PagingPair<Integer>();
        res.setIndex(Integer.MIN_VALUE);
        Integer previous = Integer.MIN_VALUE;
        int i = 0;
        while (i < fields.size()) {
            FieldHash fs = (FieldHash) fields.get(i);
            if (Integer.parseInt(fs.get("MAILID").toString()) == currentMailId) {
                if (previous > Integer.MIN_VALUE) {
                    res.setPrevious(previous);
                }
                if (i < fields.size() - 1) {
                    res.setNext(new Integer(String.valueOf(((FieldHash) fields.get(i + 1)).get("MAILID"))));
                }
                res.setIndex(((pageNumber - 1) * pageSize) + i);
                i = fields.size();
            } else {
                previous = new Integer(String.valueOf(fs.get("MAILID")));
            }
            i++;
        }
        return (res);
    }
}
