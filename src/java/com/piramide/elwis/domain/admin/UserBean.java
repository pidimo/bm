package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User Entity Bean
 *
 * @author Fernando Montaño
 * @version $Id: UserBean.java 12552 2016-05-23 21:48:26Z miguel $
 */

public abstract class UserBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setUserId(PKGenerator.i.nextKey(AdminConstants.TABLE_USER));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getUserLogin();

    public abstract void setUserLogin(String login);

    public abstract Integer getMaxRecentList();

    public abstract void setMaxRecentList(Integer maxRecentList);

    public abstract String getUserPassword();

    public abstract void setUserPassword(String password);

    public abstract Integer getRowsPerPage();

    public abstract void setRowsPerPage(Integer rowsPerPage);

    public abstract Integer getTimeout();

    public abstract void setTimeout(Integer timeout);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getFavoriteLanguage();

    public abstract void setFavoriteLanguage(String favoriteLanguage);

    public abstract Boolean getHasMailAccount();

    public abstract void setHasMailAccount(Boolean hasMailAccount);

    public abstract Boolean getSeePrivateData();

    public abstract void setSeePrivateData(Boolean seePrivateData);

    public abstract Boolean getIsDefaultUser();

    public abstract void setIsDefaultUser(Boolean isDefaultUser);

    public abstract Company getCompany();

    public abstract void setCompany(Company company);

    public abstract String getTimeZone();

    public abstract void setTimeZone(String timeZone);

    public abstract Integer getCalendarDefaultView();

    public abstract void setCalendarDefaultView(Integer calendarDefaultView);

    public abstract Integer getInitialDayOfWork();

    public abstract void setInitialDayOfWork(Integer initialDayOfWork);

    public abstract Integer getFinalDayOfWork();

    public abstract void setFinalDayOfWork(Integer finishDayOfWork);

    public abstract Integer getDayFragmentation();

    public abstract void setDayFragmentation(Integer dayFracmentation);

    public abstract Integer getHolidayCountryId();

    public abstract void setHolidayCountryId(Integer holidayCountryId);

    public abstract Integer getType();

    public abstract void setType(Integer type);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract String getNotificationAppointmentEmail();

    public abstract void setNotificationAppointmentEmail(String notificationAppointmentEmail);

    public abstract String getNotificationSchedulerTaskEmail();

    public abstract void setNotificationSchedulerTaskEmail(String notificationSchedulerTaskEmail);

    public abstract String getNotificationSupportCaseEmail();

    public abstract void setNotificationSupportCaseEmail(String notificationSupportCaseEmail);

    public abstract String getNotificationQuestionEmail();

    public abstract void setNotificationQuestionEmail(String notificationQuestionEmail);

    public abstract String getAccessIp();

    public abstract void setAccessIp(String accessIp);

    /**
     * return the list of emails configured to remind an appointment
     *
     * @return list of emails
     */
    public List getAppointmentNotificationEmails() {
        List result = new LinkedList();
        if (getNotificationAppointmentEmail() != null) {
            for (StringTokenizer emails = new StringTokenizer(getNotificationAppointmentEmail(), ",");
                 emails.hasMoreTokens(); ) {
                result.add(emails.nextToken().trim());
            }
        }
        return result;
    }

    /**
     * List of emails for notifications in tasks
     *
     * @return the list of emails
     */
    public List getSchedulerTaskNotificationEmails() {
        List result = new LinkedList();
        if (getNotificationSchedulerTaskEmail() != null) {
            for (StringTokenizer emails = new StringTokenizer(getNotificationSchedulerTaskEmail(), ",");
                 emails.hasMoreTokens(); ) {
                result.add(emails.nextToken().trim());
            }
        }
        return result;
    }

    /**
     * List of emails for notifications in support cases
     *
     * @return the list of emails
     */
    public List getSupportCaseNotificationEmails() {
        List result = new LinkedList();
        if (getNotificationSupportCaseEmail() != null) {
            for (StringTokenizer emails = new StringTokenizer(getNotificationSupportCaseEmail(), ",");
                 emails.hasMoreTokens(); ) {
                result.add(emails.nextToken().trim());
            }
        }
        return result;
    }

    /**
     * return the list of emails configured to remind an question
     *
     * @return list of emails
     */
    public List getQuestionEmails() {
        List result = new LinkedList();
        if (getNotificationQuestionEmail() != null) {
            for (StringTokenizer emails = new StringTokenizer(getNotificationQuestionEmail(), ",");
                 emails.hasMoreTokens(); ) {
                result.add(emails.nextToken().trim());
            }
        }
        return result;
    }

    public abstract Collection getUserRole();

    public abstract void setUserRole(Collection userRole);

    public abstract Collection ejbSelectActiveApplicationUserIds() throws FinderException;

    public Collection ejbHomeSelectActiveApplicationUserIds() throws FinderException {
        return ejbSelectActiveApplicationUserIds();
    }

    public abstract Integer ejbSelectCountCompanyUsersMobileAccessEnabledWithoutMe(Integer companyId, Integer currentUserId) throws FinderException;

    public Integer ejbHomeSelectCountCompanyUsersMobileAccessEnabledWithoutMe(Integer companyId, Integer currentUserId) throws FinderException {
        return ejbSelectCountCompanyUsersMobileAccessEnabledWithoutMe(companyId, currentUserId);
    }

    public abstract Integer ejbSelectCountCompanyUsersMobileAccessEnabled(Integer companyId) throws FinderException;

    public Integer ejbHomeSelectCountCompanyUsersMobileAccessEnabled(Integer companyId) throws FinderException {
        return ejbSelectCountCompanyUsersMobileAccessEnabled(companyId);
    }

    public abstract Integer ejbSelectCountCompanyUsersWVAppAccessEnabledWithoutMe(Integer companyId, Integer currentUserId) throws FinderException;

    public Integer ejbHomeSelectCountCompanyUsersWVAppAccessEnabledWithoutMe(Integer companyId, Integer currentUserId) throws FinderException {
        return ejbSelectCountCompanyUsersWVAppAccessEnabledWithoutMe(companyId, currentUserId);
    }

    public abstract Integer ejbSelectCountCompanyUsersWVAppAccessEnabled(Integer companyId) throws FinderException;

    public Integer ejbHomeSelectCountCompanyUsersWVAppAccessEnabled(Integer companyId) throws FinderException {
        return ejbSelectCountCompanyUsersWVAppAccessEnabled(companyId);
    }

    public abstract String getCsvDelimiter();

    public abstract void setCsvDelimiter(String csvDelimiter);

    public abstract String getReportCharset();

    public abstract void setReportCharset(String reportCharset);

    public abstract Collection getDynamicSearchs();

    public abstract void setDynamicSearchs(Collection dynamicSearchs);

    public abstract Boolean getMobileActive();

    public abstract void setMobileActive(Boolean mobileActive);

    public abstract Boolean getVisibleMobileApp();

    public abstract void setVisibleMobileApp(Boolean visibleMobileApp);

    public abstract Integer getMobileOrganizationId();

    public abstract void setMobileOrganizationId(Integer mobileOrganizationId);

    public abstract Boolean getEnableMobileWVApp();

    public abstract void setEnableMobileWVApp(Boolean enableMobileWVApp);

    public abstract Boolean getIsFavoriteWVApp();

    public abstract void setIsFavoriteWVApp(Boolean isFavoriteWVApp);
}
