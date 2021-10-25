package com.piramide.elwis.web.projects.util;

import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public interface WorkFlowDataUpdater {
    public void update(DefaultForm defaultForm, HttpServletRequest request);
}
