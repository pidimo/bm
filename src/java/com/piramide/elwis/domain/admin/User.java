package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;
import java.util.List;

/**
 * User entiy local interface
 *
 * @author Fernando Montaño
 * @version $Id: User.java 12547 2016-05-04 23:25:43Z miguel $
 */

public interface User extends EJBLocalObject {

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getUserLogin();

    void setUserLogin(String login);

    Integer getMaxRecentList();

    void setMaxRecentList(Integer maxRecentList);

    String getUserPassword();

    void setUserPassword(String password);

    Integer getRowsPerPage();

    void setRowsPerPage(Integer rowsPerPage);

    Integer getTimeout();

    void setTimeout(Integer timeout);

    Integer getVersion();

    void setVersion(Integer version);

    String getFavoriteLanguage();

    void setFavoriteLanguage(String favoriteLanguageId);

    Boolean getHasMailAccount();

    void setHasMailAccount(Boolean hasMailAccount);

    Boolean getSeePrivateData();

    void setSeePrivateData(Boolean seePrivateData);

    Boolean getIsDefaultUser();

    void setIsDefaultUser(Boolean isDefaultUser);

    Company getCompany();

    void setCompany(Company company);

    String getTimeZone();

    void setTimeZone(String timeZone);

    Integer getCalendarDefaultView();

    void setCalendarDefaultView(Integer calendarDefaultView);

    Integer getInitialDayOfWork();

    void setInitialDayOfWork(Integer initialDayOfWork);

    Integer getFinalDayOfWork();

    void setFinalDayOfWork(Integer finishDayOfWork);

    Integer getDayFragmentation();

    void setDayFragmentation(Integer dayFracmentation);

    Integer getHolidayCountryId();

    void setHolidayCountryId(Integer holidayCountryId);

    Integer getType();

    void setType(Integer type);

    Boolean getActive();

    void setActive(Boolean active);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    String getNotificationAppointmentEmail();

    void setNotificationAppointmentEmail(String notificationAppointmentEmail);

    String getNotificationSchedulerTaskEmail();

    void setNotificationSchedulerTaskEmail(String notificationSchedulerTaskEmail);

    String getNotificationSupportCaseEmail();

    void setNotificationSupportCaseEmail(String notificationSupportCaseEmail);

    String getNotificationQuestionEmail();

    void setNotificationQuestionEmail(String notificationQuestionEmail);

    List getAppointmentNotificationEmails();

    List getSchedulerTaskNotificationEmails();

    List getSupportCaseNotificationEmails();

    List getQuestionEmails();

    String getAccessIp();

    void setAccessIp(String accessIp);

    Collection getUserRole();

    void setUserRole(Collection userRole);

    String getCsvDelimiter();

    void setCsvDelimiter(String csvDelimiter);

    String getReportCharset();

    void setReportCharset(String reportCharset);

    Collection getDynamicSearchs();

    void setDynamicSearchs(Collection dynamicSearchs);

    Boolean getMobileActive();

    void setMobileActive(Boolean mobileActive);

    Boolean getVisibleMobileApp();

    void setVisibleMobileApp(Boolean visibleMobileApp);

    Integer getMobileOrganizationId();

    void setMobileOrganizationId(Integer mobileOrganizationId);

    Boolean getEnableMobileWVApp();

    void setEnableMobileWVApp(Boolean enableMobileWVApp);

    Boolean getIsFavoriteWVApp();

    void setIsFavoriteWVApp(Boolean isFavoriteWVApp);
}
