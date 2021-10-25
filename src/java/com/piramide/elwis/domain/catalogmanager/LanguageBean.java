package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;

/**
 * This Class represents the Language Entity Bean
 *
 * @author Ivan
 * @version $Id: LanguageBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class LanguageBean implements EntityBean {
    private Log log = LogFactory.getLog(LanguageBean.class);
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setLanguageId(PKGenerator.i.nextKey(CatalogConstants.TABLE_LANGUAGE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
        /* Below lines only were a test of timer ejb service does not make sense to uncomment.
        Calendar time = Calendar.getInstance();  // the current time.
        time.add(Calendar.MINUTE, 5); // add 5 minutes to current time
        Date date = time.getTime();
        TimerService timerService = entityContext.getTimerService();
        timerService.createTimer(date, null);*/
    }


    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
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

    public abstract Integer getLanguageId();

    public abstract void setLanguageId(Integer languageId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getLanguageName();

    public abstract void setLanguageName(String nameId);

    public abstract String getLanguageIso();

    public abstract void setLanguageIso(String code);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean is_default);

    /* public void ejbTimeout(Timer timer) {
         log.debug("EJB Timeout called");
         cancelTimer();
     }

     private void cancelTimer() {
         TimerService timerService = entityContext.getTimerService();
         Iterator timers = timerService.getTimers().iterator();
         if (timers.hasNext()) {
             Timer timer = (Timer) timers.next();
             timer.cancel();
         }
     }*/
}
