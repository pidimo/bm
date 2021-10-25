package com.piramide.elwis.cmd.webmailmanager.util.foldertree;

public class FolderPojo {
    private Integer folderId;
    private String folderName;
    private Integer unReadMails;
    private Integer parentFolderId;
    private Boolean isOpen;
    private Integer folderType;

    public FolderPojo(Integer folderId, String folderName, Integer unReadMails, Integer parentFolderId, Integer folderType) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.unReadMails = unReadMails;
        this.parentFolderId = parentFolderId;
        this.folderType = folderType;
    }

    public FolderPojo() {
    }

    public Integer getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getUnReadMails() {
        return this.unReadMails;
    }

    public void setUnReadMails(Integer unReadMails) {
        this.unReadMails = unReadMails;
    }

    public Integer getParentFolderId() {
        return this.parentFolderId;
    }

    public void setParentFolderId(Integer parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public Boolean getIsOpen() {
        return this.isOpen;
    }

    public void setIsOpen(Boolean open) {
        this.isOpen = open;
    }

    public Integer getFolderType() {
        return this.folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }
}