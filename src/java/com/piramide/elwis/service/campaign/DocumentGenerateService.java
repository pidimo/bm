/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 26, 2004
 * Time: 9:56:49 AM
 * To change this template use File | Settings | File Templates.
 */
package com.piramide.elwis.service.campaign;

import com.piramide.elwis.exception.CreateDocumentException;

import javax.ejb.EJBLocalObject;
import java.util.List;
import java.util.Map;

public interface DocumentGenerateService extends EJBLocalObject {
    public byte[] renderDocument(List values, String[] names, byte[] wordDoc) throws CreateDocumentException;

    public boolean renderDocumentWithMail(String from, Map images, List fields, List values, String[] names,
                                          List mails, String subject, List attachs, Integer userId);
}
