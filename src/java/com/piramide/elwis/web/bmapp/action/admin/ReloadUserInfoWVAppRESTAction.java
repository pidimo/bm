package com.piramide.elwis.web.bmapp.action.admin;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.1
 */
public class ReloadUserInfoWVAppRESTAction extends ReloadUserInfoRESTAction {

    @Override
    protected boolean isReloadInfoFromWVApp() {
        return true;
    }
}
