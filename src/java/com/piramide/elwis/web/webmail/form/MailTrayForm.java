package com.piramide.elwis.web.webmail.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 * This class helps to the view of the mail tray
 *
 * @author Alvaro
 * @version $Id: MailTrayForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class MailTrayForm extends SearchForm {

    private Map dto;
    private Object mailFilter;
    private Object markAs;
    private Object moveTo;
    private Object[] selectedMails;
    private Object folderId;
    private Object delete;
    private Object toEmptyFolder;
    private Object emptyFolder;
    private Object moveToTrash;
    private Object emptyFolderToTrash;
    private Object moveToButton;
    private String emailIdentifiers = "";


    public Map getDto() {
        return dto;
    }

    public void setDto(Map dto) {
        this.dto = dto;
    }

    public MailTrayForm() {
        this.dto = new HashMap();
        this.mailFilter = null;
        this.markAs = null;
        this.moveTo = null;
        this.moveToButton = null;
        this.selectedMails = new Object[0];
    }

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }

    public Object getMailFilter() {
        return (this.mailFilter);
    }

    public void setMailFilter(Object mailFilter) {
        this.mailFilter = mailFilter;
    }

    public Object getMoveTo() {
        return (this.moveTo);
    }

    public void setMoveTo(Object moveTo) {
        this.moveTo = moveTo;
    }

    public Object getMarkAs() {
        return (this.markAs);
    }

    public void setMarkAs(Object markAs) {
        this.markAs = markAs;
    }

    public Object[] getSelectedMails() {
        return this.selectedMails;
    }

    public void setSelectedMails(Object[] selectedMails) {
        if (selectedMails != null) {
            this.selectedMails = selectedMails;
        } else {
            this.selectedMails = new Object[0];
        }
    }

    public void setFolderId(Object folderId) {
        this.folderId = folderId;
    }

    public Object getFolderId() {
        return (this.folderId);
    }

    public void setDelete(Object delete) {
        this.delete = (null != delete && !"".equals(delete.toString().trim()) ? delete : null);
    }

    public Object getDelete() {
        return (this.delete);
    }

    public void setToEmptyFolder(Object toEmptyFolder) {
        this.toEmptyFolder = toEmptyFolder;
    }

    public Object getToEmptyFolder() {
        return (this.toEmptyFolder);
    }

    public Object getEmptyFolderToTrash() {
        return emptyFolderToTrash;
    }

    public void setEmptyFolderToTrash(Object emptyFolderToTrash) {
        this.emptyFolderToTrash = (null != emptyFolderToTrash && !"".equals(emptyFolderToTrash.toString().trim()) ? emptyFolderToTrash : null);
    }

    public void setMoveToTrash(Object moveToTrash) {
        this.moveToTrash = (null != moveToTrash && !"".equals(moveToTrash.toString().trim()) ? moveToTrash : null);
    }

    public Object getMoveToTrash() {
        return moveToTrash;
    }

    public Object getEmptyFolder() {
        return emptyFolder;
    }

    public void setEmptyFolder(Object emptyFolder) {
        this.emptyFolder = (null != emptyFolder && !"".equals(emptyFolder.toString().trim()) ? emptyFolder : null);
    }

    public Object getMoveToButton() {
        return moveToButton;
    }

    public void setMoveToButton(Object moveToButton) {
        this.moveToButton = moveToButton;
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

