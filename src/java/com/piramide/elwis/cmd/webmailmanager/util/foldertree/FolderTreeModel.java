package com.piramide.elwis.cmd.webmailmanager.util.foldertree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.*;

public class FolderTreeModel extends DefaultTreeModel {
    Log log = LogFactory.getLog(getClass());

    public FolderTreeModel(TreeNode treeNode) {
        super(treeNode);
        this.root = new DefaultMutableTreeNode(null);
    }

    public void fillTree(List<HashMap> folderList) {
        for (Iterator hashMapIterator = folderList.iterator(); hashMapIterator.hasNext();) {
            HashMap folderMap = (HashMap) hashMapIterator.next();
            Integer unReadMails = 0, parentFolderId = null;
            if (folderMap.get("unReadMails") != null && folderMap.get("unReadMails").toString().length() > 0) {
                unReadMails = new Integer(folderMap.get("unReadMails").toString());
            }
            if (folderMap.get("parentFolderId") != null && folderMap.get("parentFolderId").toString().length() > 0) {
                parentFolderId = new Integer(folderMap.get("parentFolderId").toString());
            }
            FolderPojo folder = new FolderPojo(new Integer(folderMap.get("folderId").toString()),
                    (String) folderMap.get("folderName"),
                    unReadMails,
                    parentFolderId,
                    new Integer(folderMap.get("folderType").toString()));
            if (folderMap.get("isOpen") != null && folderMap.get("isOpen").equals("1")) {
                folder.setIsOpen(true);
            } else {
                folder.setIsOpen(false);
            }

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(folder);

            List myMissedChilds = findMissedChildrens(node);
            if (folder.getParentFolderId() != null) {
                FolderPojo parentFolder = new FolderPojo();
                parentFolder.setFolderId(folder.getParentFolderId());
                DefaultMutableTreeNode parentNode = findNodeInTree(new DefaultMutableTreeNode(parentFolder));
                if (parentNode != null) {
                    parentNode.add(node);
                    if (myMissedChilds.size() > 0) {
                        addMissedChilds(myMissedChilds, node);
                    }
                } else {
                    if (myMissedChilds.size() > 0) {
                        addMissedChilds(myMissedChilds, node);
                    }
                    ((DefaultMutableTreeNode) this.root).add(node);
                }
            } else {
                ((DefaultMutableTreeNode) this.root).add(node);
                if (myMissedChilds.size() > 0) {
                    addMissedChilds(myMissedChilds, node);
                }
            }
        }
    }

    public DefaultMutableTreeNode findNodeInTree(DefaultMutableTreeNode treeNode) {
        DefaultMutableTreeNode res = null;
        Enumeration childrens = ((DefaultMutableTreeNode) this.root).breadthFirstEnumeration();
        while ((childrens.hasMoreElements()) && (res == null)) {
            DefaultMutableTreeNode actualNode = (DefaultMutableTreeNode) childrens.nextElement();
            if ((treeNode.getUserObject() != null) && (actualNode.getUserObject() != null)) {
                FolderPojo treeNodeFolder = (FolderPojo) treeNode.getUserObject();
                FolderPojo actualNodeFolder = (FolderPojo) actualNode.getUserObject();
                if (treeNodeFolder.getFolderId().equals(actualNodeFolder.getFolderId())) {
                    res = actualNode;
                }
            }
        }
        return res;
    }

    private List<DefaultMutableTreeNode> findMissedChildrens(DefaultMutableTreeNode parentTreeNode) {
        List res = new ArrayList();
        Enumeration childrens = this.root.children();
        while (childrens.hasMoreElements()) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) childrens.nextElement();
            FolderPojo parentTreeNodeFolder = (FolderPojo) parentTreeNode.getUserObject();
            FolderPojo treeNodeFolder = (FolderPojo) treeNode.getUserObject();
            if (parentTreeNodeFolder.getFolderId().equals(treeNodeFolder.getParentFolderId())) {
                res.add(treeNode);
            }
        }
        return res;
    }

    private void addMissedChilds(List<DefaultMutableTreeNode> myMissedChilds, DefaultMutableTreeNode parentNode) {
        for (DefaultMutableTreeNode childTreeNode : myMissedChilds) {
            parentNode.add(childTreeNode);
        }
    }

    public List<FolderPojo> getDescendantFoldersList(DefaultMutableTreeNode parentNode) {
        List foldersList = new ArrayList();
        Enumeration nodesEnum = parentNode.depthFirstEnumeration();
        while (nodesEnum.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodesEnum.nextElement();
            if (node.getUserObject() != null) {
                FolderPojo folderPojo = (FolderPojo) node.getUserObject();
                foldersList.add(folderPojo);
            }
        }
        return foldersList;
    }

    public List<FolderPojo> sortList(List<FolderPojo> folders) {
        List res = new ArrayList();
        SortedMap<String, FolderPojo> tempMap = new TreeMap<String, FolderPojo>();
        for (FolderPojo folderPojo : folders) {
            tempMap.put(folderPojo.getFolderName().toLowerCase(), folderPojo);
        }

        for (String s : tempMap.keySet()) {
            res.add(tempMap.get(s));
        }

        return res;
    }
}