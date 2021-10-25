package com.piramide.elwis.web.common.accessrightdatalevel;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.fantabulous.AccessDataLevelFantabulousCompleter;
import com.piramide.elwis.web.common.accessrightdatalevel.fantabulous.AddressAccessRightFantabulousCompleter;
import com.piramide.elwis.web.common.accessrightdatalevel.fantabulous.ArticleAccessRightFantabulousCompleter;
import com.piramide.elwis.web.common.accessrightdatalevel.util.AccessRightDataLevelUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.structure.ListStructureCloneUtil;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Util to check access right on data level
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightDataLevelSecurity {
    private final Log log = LogFactory.getLog(this.getClass());

    public static AccessRightDataLevelSecurity i = new AccessRightDataLevelSecurity();

    public ListStructure processAccessRightByList(ListStructure sourceListStructure, Integer userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration dataLevelAccessConfig) {
        log.debug("Process access right data level by list.. .. .. " + sourceListStructure.getListName() + " , DataLevelAccessConfiguration: " + dataLevelAccessConfig.getConstant());
        ListStructure securityListStructure;

        boolean isSecurityProcess = AccessRightDataLevelUtil.existList(sourceListStructure, dataLevelAccessConfig.getConstant());
        if (isSecurityProcess) {
            securityListStructure = ListStructureCloneUtil.clone(sourceListStructure);

            AccessDataLevelFantabulousCompleter fantabulousCompleter = null;
            if (AccessRightDataLevelConstants.DataLevelAccessConfiguration.ADDRESS_ACCESS.equals(dataLevelAccessConfig)) {
                fantabulousCompleter = new AddressAccessRightFantabulousCompleter();
            } else if (AccessRightDataLevelConstants.DataLevelAccessConfiguration.ARTICLE_ACCESS.equals(dataLevelAccessConfig)) {
                fantabulousCompleter = new ArticleAccessRightFantabulousCompleter();
            }

            if (fantabulousCompleter != null) {
                securityListStructure = fantabulousCompleter.completeByList(userId, securityListStructure);
            }
        } else {
            securityListStructure = sourceListStructure;
        }
        return securityListStructure;
    }

    public ListStructure processAccessRightByList(ListStructure sourceListStructure, HttpServletRequest request, AccessRightDataLevelConstants.DataLevelAccessConfiguration dataLevelAccessConfig) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());
        return processAccessRightByList(sourceListStructure, userId, dataLevelAccessConfig);
    }

    public ListStructure processAccessRightByTable(ListStructure sourceListStructure, Integer userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration dataLevelAccessConfig) {
        log.debug("Process access right data level by table.. .. .. " + sourceListStructure.getListName() +", DataLevelAccessConfiguration: " + dataLevelAccessConfig.getConstant());
        ListStructure securityListStructure;
        securityListStructure = ListStructureCloneUtil.clone(sourceListStructure);

        AccessDataLevelFantabulousCompleter fantabulousCompleter = null;
        if (AccessRightDataLevelConstants.DataLevelAccessConfiguration.ADDRESS_ACCESS.equals(dataLevelAccessConfig)) {
            fantabulousCompleter = new AddressAccessRightFantabulousCompleter();
        } else if (AccessRightDataLevelConstants.DataLevelAccessConfiguration.ARTICLE_ACCESS.equals(dataLevelAccessConfig)) {
            fantabulousCompleter = new ArticleAccessRightFantabulousCompleter();
        }

        if (fantabulousCompleter != null) {
            securityListStructure = fantabulousCompleter.completeByTable(userId, securityListStructure);
        }
        return securityListStructure;
    }

    public ListStructure processAccessRightByTable(ListStructure sourceListStructure, HttpServletRequest request, AccessRightDataLevelConstants.DataLevelAccessConfiguration dataLevelAccessConfig) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());
        return processAccessRightByTable(sourceListStructure, userId, dataLevelAccessConfig);
    }

    public ListStructure processAllAccessRightByList(ListStructure sourceListStructure, Integer userId) {
        ListStructure securityListStructure = ListStructureCloneUtil.clone(sourceListStructure);

        for (int j = 0; j < AccessRightDataLevelConstants.DataLevelAccessConfiguration.values().length; j++) {
            AccessRightDataLevelConstants.DataLevelAccessConfiguration dataLevelAccessConfiguration = AccessRightDataLevelConstants.DataLevelAccessConfiguration.values()[j];
            securityListStructure = AccessRightDataLevelSecurity.i.processAccessRightByList(securityListStructure, userId, dataLevelAccessConfiguration);
        }
        return securityListStructure;
    }

    public ListStructure processAllAccessRightByTable(ListStructure sourceListStructure, Integer userId) {
        ListStructure securityListStructure = ListStructureCloneUtil.clone(sourceListStructure);

        for (int j = 0; j < AccessRightDataLevelConstants.DataLevelAccessConfiguration.values().length; j++) {
            AccessRightDataLevelConstants.DataLevelAccessConfiguration dataLevelAccessConfiguration = AccessRightDataLevelConstants.DataLevelAccessConfiguration.values()[j];
            securityListStructure = AccessRightDataLevelSecurity.i.processAccessRightByTable(securityListStructure, userId, dataLevelAccessConfiguration);
        }
        return securityListStructure;
    }

    public ListStructure processAddressAccessRightByList(ListStructure sourceListStructure, Integer userId) {
        return processAccessRightByList(sourceListStructure, userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration.ADDRESS_ACCESS);
    }

    public ListStructure processAddressAccessRightByList(ListStructure sourceListStructure, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());
        return processAddressAccessRightByList(sourceListStructure, userId);
    }

    public ListStructure processAddressAccessRightByTable(ListStructure sourceListStructure, Integer userId) {
        return processAccessRightByTable(sourceListStructure, userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration.ADDRESS_ACCESS);
    }

    public ListStructure processAddressAccessRightByTable(ListStructure sourceListStructure, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());
        return processAddressAccessRightByTable(sourceListStructure, userId);
    }

    public boolean userWithAccessToAddressOnDataLevel(Integer addressId, HttpServletRequest request) {
        log.debug("Check user access to addressId:" + addressId);

        boolean userWithAccess = true;

        User user = RequestUtils.getUser(request);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        if (addressId != null) {

            String listName = "addressSingleAccessList";
            FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), "/contacts");

            ListStructure list = null;
            try {
                list = fantabulousManager.getList(listName);
                //add access right security because this is for invoice all list result
                list = AccessRightDataLevelSecurity.i.processAddressAccessRightByList(list, userId);
            } catch (ListStructureNotFoundException e) {
                log.debug("->Read List " + listName + " In Fantabulous structure Fail");
            }

            if (list != null) {

                Parameters parameters = new Parameters();
                parameters.addSearchParameter("addressId", addressId.toString());
                parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());

                Collection result = Controller.getList(list, parameters);

                if (result.isEmpty()) {
                    userWithAccess = false;
                }
            }
        }
        return userWithAccess;
    }

    public boolean userWithAccessToArticleOnDataLevel(Integer articleId, HttpServletRequest request) {
        log.debug("Check user access to articleId:" + articleId);

        boolean userWithAccess = true;

        User user = RequestUtils.getUser(request);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        if (articleId != null) {

            String listName = "articleSingleAccessList";
            FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), "/support");

            ListStructure list = null;
            try {
                list = fantabulousManager.getList(listName);
                //add access right security
                list = AccessRightDataLevelSecurity.i.processAccessRightByList(list, userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration.ARTICLE_ACCESS);
            } catch (ListStructureNotFoundException e) {
                log.debug("->Read List " + listName + " In Fantabulous structure Fail");
            }

            if (list != null) {

                Parameters parameters = new Parameters();
                parameters.addSearchParameter("articleId", articleId.toString());
                parameters.addSearchParameter(Constants.COMPANYID, user.getValue(Constants.COMPANYID).toString());

                Collection result = Controller.getList(list, parameters);

                if (result.isEmpty()) {
                    userWithAccess = false;
                }
            }
        }
        return userWithAccess;
    }

}
