package com.piramide.elwis.web.common;

import com.piramide.elwis.cmd.admin.SystemFunctionReadCodeCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.URLParameterProcessor;
import com.piramide.elwis.web.fantabulous.WhiteSpaceToWildcardSearchParameterMapping;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.web.mapping.URLParameterReWriteMapping;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.struts.util.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Custom request processor for all user request.
 * This class is called for each and every request.
 *
 * @author Fernando Montaño
 * @version $Id: CustomRequestProcessor.java 12654 2017-03-24 23:46:39Z miguel $
 */

public class CustomRequestProcessor extends RequestProcessor {

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * Preprocess every request and check if user session expired.
	 * If user is in contact module, check if the active address not has been deleted by other user, if
	 * was deleted, then return to search list and show a information Error.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return true if it's process
	 */

	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {

		log.debug("executing processPreprocess...");
		boolean continueProcessing = true;
		boolean isfromLoginPage = request.getServletPath().startsWith("/Logon.do");
		boolean isfromExternalLoginPage = request.getServletPath().startsWith("/ExternalLogon.do");
		boolean isUpload = StringUtils.contains(request.getServletPath(), "Upload.do");
		boolean isTest = request.getServletPath().startsWith("/FantabulousTest.do");
		boolean isSimpleService = request.getServletPath().startsWith("/SimpleService.do");
		boolean isAttachImage = request.getServletPath().startsWith("/webmail/downloadImage.do");
		boolean isFromMobileLogin = request.getServletPath().startsWith("/mobile/Logon.do") || request.getServletPath().startsWith("/bmapp/LogonBMApp.do") || request.getServletPath().startsWith("/bmapp/LogonBMApp/WVAPP.do");
		boolean isCreateTrialCompany = request.getServletPath().startsWith("/Company/Forward/CreateTrial.do");
		boolean isSaveTrialCompany = request.getServletPath().startsWith("/Company/CreateTrial.do");
		boolean isPlannedPasswordUpdate = StringUtils.contains(request.getServletPath(), "/User/Planned/PasswordUpdate.do");
		boolean isDownloadCompanyLogoBmapp = StringUtils.contains(request.getServletPath(), "/bmapp/Logon/Company/DownloadLogoImage.do");
		boolean isDemoAccount = StringUtils.contains(request.getServletPath(), "/DemoAccount/Front") || request.getServletPath().startsWith("/DemoAccount/Company/Create.do");

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(Constants.USER_KEY);

		if (user == null && !isfromLoginPage && !isfromExternalLoginPage && !isUpload && !isTest && !isSimpleService &&
				!isAttachImage && !isFromMobileLogin && !isCreateTrialCompany && !isSaveTrialCompany && !isPlannedPasswordUpdate
				&& !isDownloadCompanyLogoBmapp && !isDemoAccount) {
			log.debug("Session has expired...");
			continueProcessing = false;
			try {


				if (request.getRequestURI() != null &&   //checking if the session die in mobile module.
						request.getRequestURI().indexOf(request.getContextPath() + "/mobile/") > -1) {
					response.sendRedirect(request.getContextPath() + "/mobile/index.jsp?expired=1");
					return continueProcessing;
				}

				if (request.getRequestURI() != null &&   //checking if the session die in bm app module.
						request.getRequestURI().indexOf(request.getContextPath() + "/bmapp/") > -1) {
					response.sendRedirect(request.getContextPath() + "/bmapp/SessionExpiredJson.jsp?expired=1");
					return continueProcessing;
				}

				if ("true".equals(request.getHeader("isAjaxRequest"))) {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					response.sendRedirect(request.getContextPath() + "/index.jsp?expired=1");
				}


			} catch (IOException e) {
				log.error("Error redirecting", e);
			}
		} else if (user != null) {//check if the companyId of the request is the same of the user session.
			if (request.getParameter(Constants.REQUEST_COMPANY_ID) != null &&
					!user.getValue(Constants.COMPANYID).toString()
							.equals(request.getParameter(Constants.REQUEST_COMPANY_ID))) {
				log.warn("The companyId requested is not equal of the current session");
				try {
					processInvalidCompanyRequest(request);
					request.getRequestDispatcher("/ErrorPage.jsp").forward(request, response);
				} catch (Exception e) {
					log.error("Error showing error page for invalid company request", e);
				}
				return false;
			}
		}

		return continueProcessing;
	}


	protected ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response,
												 Action action, ActionForm form, ActionMapping mapping)
			throws IOException, ServletException {

		if (mapping instanceof WhiteSpaceToWildcardSearchParameterMapping) {
			((WhiteSpaceToWildcardSearchParameterMapping) mapping).preProcessParameters(form);
		}

		ActionForward forward = super.processActionPerform(request, response, action, form, mapping);

		if (mapping instanceof WhiteSpaceToWildcardSearchParameterMapping) {
			((WhiteSpaceToWildcardSearchParameterMapping) mapping).posProcessParameters(form);
		}

		if (forward != null) {

			//log.debug("forward= " + forward);

			StringBuffer path = new StringBuffer(response.encodeURL(forward.getPath()));


			URLParameterProcessor.addModuleParameters(path, request, request.getSession().getServletContext(), true, true);
			if (mapping instanceof URLParameterReWriteMapping) {
//                log.debug("SubModule Parameters add..:" + mapping);
				URLParameterProcessor.addParameterToUrl(path, ((URLParameterReWriteMapping) mapping).getParamReWriteList(), request, false);
				URLParameterProcessor.addParameterToUrl(path, ((URLParameterReWriteMapping) mapping).getParamReWriteTextList(), request, true);
			}

			forward = new ActionForward(forward.getName(), new String(path), forward.getRedirect(), forward.getContextRelative());

			//log.debug("The new path: " + forward);
		}
		return forward;
	}

	/**
	 * This method check using the value defined in roles configuration for each action if user has a restriction
	 * on that functionality an permission. If there is a restriction for both conbination user is redirected
	 * to error page (Security access fail).
	 *
	 * @param request  the request
	 * @param response the response
	 * @param mapping  the Action instance mapping
	 * @return true if user has access to execute the action, false otherwise (it has restrictions)
	 * @throws IOException      an exception
	 * @throws ServletException server web exception
	 */

	protected boolean processRoles(HttpServletRequest request,
								   HttpServletResponse response,
								   ActionMapping mapping)
			throws IOException, ServletException {

		//the cancel button has not be evaluated.
		if (request.getParameter(org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY) != null) {
			return true;
		}

		log.debug("Process Security Roles and access rights...");

		String roles[] = mapping.getRoleNames();
		if ((roles == null) || (roles.length < 1)) {
			return (true);
		} else {
			String token = roles[0];
			String functionality = token.substring(0, token.indexOf("."));
			String permissionRequired = token.substring(token.indexOf(".") + 1, token.length());
			log.debug("functionality = " + functionality);
			log.debug("permission required = " + permissionRequired);

			User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

			if (user.getSecurityAccessRights().containsKey(functionality)) {
				Byte accessRight = (Byte) user.getSecurityAccessRights().get(functionality);
				if (!PermissionUtil.hasAccessRight(accessRight, permissionRequired)) {
					processInvalidAccessRight(functionality, permissionRequired, request);
					request.getRequestDispatcher("/ErrorPage.jsp").forward(request, response);
					return false;
				}
			} else {
				processInvalidAccessRight(functionality, request);
				request.getRequestDispatcher("/ErrorPage.jsp").forward(request, response);
				return false;
			}
		}

		return (true);  //enable the access.


	}

	/**
	 * Process invalid request for the company.
	 *
	 * @param request request
	 */
	private void processInvalidCompanyRequest(HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.invalid.request.companyId"));

		// Remove any error messages attribute if none are required
		if ((errors == null) || errors.isEmpty()) {
			request.removeAttribute(Globals.ERROR_KEY);
			return;
		}
		// Save the error messages we need
		request.setAttribute(Globals.ERROR_KEY, errors);
	}

	/**
	 * Process invalid attachSize.
	 *
	 * @param request request
	 */
	private void processInvalidAttachSize(HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.Error_maxLengthExceeded"));

		// Remove any error messages attribute if none are required
		if ((errors == null) || errors.isEmpty()) {
			request.removeAttribute(Globals.ERROR_KEY);
			return;
		}
		// Save the error messages we need
		request.setAttribute(Globals.ERROR_KEY, errors);
	}


	/**
	 * Process invalida access error messages.
	 *
	 * @param functionality functioality which is trying to enter
	 * @param permission    permission required.
	 * @param request       request
	 */
	private void processInvalidAccessRight(String functionality, String permission, HttpServletRequest request) {
		SystemFunctionReadCodeCmd functionCmd = new SystemFunctionReadCodeCmd();
		ActionErrors errors = new ActionErrors();
		functionCmd.putParam("functionCode", functionality);
		try {

			ResultDTO resultDTO = BusinessDelegate.i.execute(functionCmd, request);
			if (!resultDTO.isFailure()) {
				if (permission != null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.invalid_access2",
							JSPHelper.getMessage(request, permission),
							JSPHelper.getMessage(request, resultDTO.getAsString("nameKey")),
							JSPHelper.getMessage(request, resultDTO.getAsString("moduleNameKey"))));
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.invalid_access1",
							JSPHelper.getMessage(request, resultDTO.getAsString("nameKey")),
							JSPHelper.getMessage(request, resultDTO.getAsString("moduleNameKey"))));
				}
			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.invalid_access"));
			}

		} catch (AppLevelException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.invalid_access"));
		}

		// Remove any error messages attribute if none are required
		if ((errors == null) || errors.isEmpty()) {
			request.removeAttribute(Globals.ERROR_KEY);
			return;
		}

		// Save the error messages we need
		request.setAttribute(Globals.ERROR_KEY, errors);
	}

	private void processInvalidAccessRight(String functionality, HttpServletRequest request) {
		processInvalidAccessRight(functionality, null, request);
	}

	@Override
	protected ActionForm processActionForm(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) {
		log.debug("start: ProcessActionForm");
		return super.processActionForm(request, response, mapping);


	}

	/**
	 * <p>Process an <code>HttpServletRequest</code> and create the
	 * corresponding <code>HttpServletResponse</code>.</p>
	 *
	 * @param request  The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @throws IOException      if an input/output error occurs
	 * @throws ServletException if a processing exception occurs
	 */
	public void process(HttpServletRequest request,
						HttpServletResponse response)
			throws IOException, ServletException {

		// Wrap multipart requests with a special wrapper
		request = processMultipart(request);

		// Identify the path component we will use to select a mapping
		String path = processPath(request, response);
		if (path == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("Processing a '" + request.getMethod() +
					"' for path '" + path + "'");
		}

		// Select a Locale for the current user if requested
		processLocale(request, response);

		// Set the content type and no-caching headers if requested
		processContent(request, response);
		processNoCache(request, response);

		// General purpose preprocessing hook
		if (!processPreprocess(request, response)) {
			return;
		}

		// Identify the mapping for this request
		ActionMapping mapping = processMapping(request, response, path);
		if (mapping == null) {
			return;
		}
		// Process any ActionForm bean related to this request
		ActionForm form = processActionForm(request, response, mapping);

		processPopulate(request, response, form, mapping);
		if (!processValidate(request, response, form, mapping)) {
			return;
		}
		// Check for any role required to perform this action
		if (!processRoles(request, response, mapping)) {
			return;
		}

		// Process a forward or include specified by this mapping
		if (!processForward(request, response, mapping)) {
			return;
		}
		if (!processInclude(request, response, mapping)) {
			return;
		}

		// Create or acquire the Action instance to process this request
		Action action = processActionCreate(request, response, mapping);
		if (action == null) {
			return;
		}

		// Call the Action instance itself
		ActionForward forward =
				processActionPerform(request, response,
						action, form, mapping);
		response.setCharacterEncoding(Constants.CHARSET_ENCODING);
		// Process the returned ActionForward instance
		processForwardConfig(request, response, forward);

	}

	/**
	 * Return an <code>Action</code> instance that will be used to process
	 * the current request, creating a new one.
	 *
	 * Override to create new instance of action by each request.
	 *
	 * @param request  The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param mapping  The mapping we are using
	 * @throws IOException if an input/output error occurs
	 */
	@Override
	protected Action processActionCreate(HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) throws IOException {
		// Acquire the Action instance we will be using (if there is one)
		String className = mapping.getType();
		if (log.isDebugEnabled()) {
			log.debug(" Looking for Action instance for class " + className);
		}

		Action instance = null;

		// Create and return a new Action instance
		if (log.isTraceEnabled()) {
			log.trace("  Creating new Action instance");
		}

		try {
			instance = (Action) RequestUtils.applicationInstance(className);
			// TODO Maybe we should propagate this exception instead of returning
			// null.
		} catch (Exception e) {
			log.error(
					getInternal().getMessage("actionCreate", mapping.getPath()),
					e);

			response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					getInternal().getMessage("actionCreate", mapping.getPath()));

			return (null);
		}

		instance.setServlet(this.servlet);

		return (instance);
	}
}