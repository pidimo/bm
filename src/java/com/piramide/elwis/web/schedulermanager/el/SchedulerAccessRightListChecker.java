package com.piramide.elwis.web.schedulermanager.el;


import com.piramide.elwis.dto.schedulermanager.SchedulerAccessDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SchedulerPermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class SchedulerAccessRightListChecker {
    private Log log = LogFactory.getLog(this.getClass());

    private Map<String, List<SchedulerAccessDTO>> cache = new HashMap<String, List<SchedulerAccessDTO>>();

    public static SchedulerAccessRightListChecker i = new SchedulerAccessRightListChecker();

    private SchedulerAccessRightListChecker() {
    }

    public Long registerUserToCheckAccessRights(HttpServletRequest request) {
        Long code = System.nanoTime();
        getSchedulerAccessFromCache(request, code);
        return code;
    }

    public boolean isAddAppointmentPermissionEnabled(HttpServletRequest request, Long code) {
        List<SchedulerAccessDTO> accesses = getSchedulerAccessFromCache(request, code);
        log.debug("-> accesses: " + accesses);
        for (SchedulerAccessDTO dto : accesses) {
            Byte publicPermission = (Byte) dto.get("permission");
            Byte privatePermission = (Byte) dto.get("privatePermission");
            if (SchedulerPermissionUtil.hasSchedulerAccessRight(publicPermission, SchedulerPermissionUtil.ADD) ||
                    SchedulerPermissionUtil.hasSchedulerAccessRight(privatePermission, SchedulerPermissionUtil.ADD)) {
                return true;
            }
        }

        return false;
    }

    public Map<String, Byte> getPermissions(Integer appointmentUserId, HttpServletRequest request, Long code) {
        List<SchedulerAccessDTO> accesses = getSchedulerAccessFromCache(request, code);

        return SchedulerAccessRightUtil.i.getPermissions(appointmentUserId, getUserId(request), accesses);
    }

    public void clearCache(HttpServletRequest request, Long code) {
        String cacheKey = buildCacheKey(request, code);
        log.debug("Remove from cache key: " + cacheKey);
        cache.remove(cacheKey);
        log.debug("cache: " + cache);
    }

    private List<SchedulerAccessDTO> getSchedulerAccessFromCache(HttpServletRequest request, Long code) {
        String cacheKey = buildCacheKey(request, code);

        List<SchedulerAccessDTO> cacheElement = cache.get(cacheKey);
        if (null == cacheElement) {
            cacheElement = SchedulerAccessRightUtil.i.getSchedulerAccesses(getUserId(request), request);
            cache.put(cacheKey, cacheElement);

            log.debug("put in cache : " + cacheElement);
        }

        return cacheElement;
    }

    private String buildCacheKey(HttpServletRequest request, Long code) {
        Integer companyId = getCompanyId(request);
        Integer userId = getUserId(request);

        return companyId + "_" + userId + "_" + code;
    }

    private Integer getUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue(Constants.USERID);
    }

    private Integer getCompanyId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return (Integer) user.getValue(Constants.COMPANYID);
    }
}
