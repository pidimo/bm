/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.piramide.elwis.service.exception.webmail.EmailNotFoundException;

import javax.ejb.EJBLocalObject;
import java.util.List;

public interface EmailSourceService extends EJBLocalObject {
    public void storeEmailSource(Integer mailId,
                                 Integer userMailId) throws ConnectionException,
            EmailNotFoundException;

    void rebuildEmailBodyAttachInBackgroundBatchProcess(List<Integer> mailIdList);
}
