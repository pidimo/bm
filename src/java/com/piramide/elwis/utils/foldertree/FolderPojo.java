package com.piramide.elwis.utils.foldertree;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: FolderPojo.java 13-may-2009 21:03:36
 */
public class FolderPojo {
    Integer folderId;
    String folderName;
    Integer unReadMails;
    Integer allMails;
    Integer parentFolderId;

    public FolderPojo(Integer folderId, String folderName, Integer unReadMails, Integer allMails, Integer parentFolderId) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.unReadMails = unReadMails;
        this.allMails = allMails;
        this.parentFolderId = parentFolderId;
    }

    public FolderPojo() {
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getUnReadMails() {
        return unReadMails;
    }

    public void setUnReadMails(Integer unReadMails) {
        this.unReadMails = unReadMails;
    }

    public Integer getAllMails() {
        return allMails;
    }

    public void setAllMails(Integer allMails) {
        this.allMails = allMails;
    }

    public Integer getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Integer parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
}
