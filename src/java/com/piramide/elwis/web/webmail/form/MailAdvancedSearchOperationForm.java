package com.piramide.elwis.web.webmail.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Alvaro Sejas
 * @version 4.2.2
 */
public class MailAdvancedSearchOperationForm extends WebmailDefaultForm {
    Log log = LogFactory.getLog(this.getClass());

    private Object[] selectedMails;
    private Integer moveToFolderId;
    private Object moveToButton;
    private Object moveToTrash;
    private Object emptyFolderToTrash;
    private String emailIdentifiers = "";

    public MailAdvancedSearchOperationForm() {
        this.selectedMails = new Object[0];
    }

    public Object[] getSelectedMails() {
        return selectedMails;
    }

    public void setSelectedMails(Object[] selectedMails) {
        if (selectedMails != null) {
            this.selectedMails = selectedMails;
        } else {
            this.selectedMails = new Object[0];
        }
    }

    public Integer getMoveToFolderId() {
        return moveToFolderId;
    }

    public void setMoveToFolderId(Integer moveToFolderId) {
        this.moveToFolderId = moveToFolderId;
    }

    public Object getMoveToButton() {
        return moveToButton;
    }

    public void setMoveToButton(Object moveToButton) {
        this.moveToButton = moveToButton;
    }

    public void setMoveToTrash(Object moveToTrash) {
        this.moveToTrash = (null != moveToTrash && !"".equals(moveToTrash.toString().trim()) ? moveToTrash : null);
    }

    public Object getMoveToTrash() {
        return moveToTrash;
    }

    public Object getEmptyFolderToTrash() {
        return emptyFolderToTrash;
    }

    public void setEmptyFolderToTrash(Object emptyFolderToTrash) {
        this.emptyFolderToTrash = (null != emptyFolderToTrash && !"".equals(emptyFolderToTrash.toString().trim()) ? emptyFolderToTrash : null);
    }

    public String getEmailIdentifiers() {
        return emailIdentifiers;
    }

    public void setEmailIdentifiers(String emailIdentifiers) {
        this.emailIdentifiers = emailIdentifiers;
    }

    public List getEmailIdentifiersAsList() {
        List result = new ArrayList();
        String[] keys = emailIdentifiers.split("(,)");
        if (null != keys) {
            for (String key : keys) {
                if (!"".equals(key.trim())) {
                    result.add(key);
                }
            }
        }
        return result;
    }

}
