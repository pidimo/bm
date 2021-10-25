package com.piramide.elwis.utils.foldertree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: FolderTreeModel.java 13-may-2009 20:59:32
 */
public class FolderTreeModel extends DefaultTreeModel {
    Log log = LogFactory.getLog(this.getClass());

    public FolderTreeModel(TreeNode treeNode) {
        super(treeNode);
        root = new DefaultMutableTreeNode(null);
    }

    /**
     * Fill the treeModel with folders
     *
     * @param folderList List of folders
     */
    public void fillTree(List<HashMap> folderList) {
        for (Iterator<HashMap> hashMapIterator = folderList.iterator(); hashMapIterator.hasNext();) {
            HashMap folderMap = hashMapIterator.next();
            FolderPojo folder = new FolderPojo((Integer) folderMap.get("folderId"),
                    (String) folderMap.get("folderName"),
                    (Integer) folderMap.get("unReadMails"),
                    (Integer) folderMap.get("allMails"),
                    (Integer) folderMap.get("parentFolderId"));

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(folder);

            List<DefaultMutableTreeNode> myMissedChilds = findMissedChildrens(node); //find my missed childrens
            if (folder.getParentFolderId() != null) {
                FolderPojo parentFolder = new FolderPojo();
                parentFolder.setFolderId(folder.getParentFolderId());
                DefaultMutableTreeNode parentNode = findNodeInTree(new DefaultMutableTreeNode(parentFolder));
                if (parentNode != null) { //if my dad is in tree
                    parentNode.add(node); //hello dad
                    if (myMissedChilds.size() > 0) //Have I missed childrens in the tree?
                    {
                        addMissedChilds(myMissedChilds, node);  // Oh, hello boys!
                    }
                } else {
                    if (myMissedChilds.size() > 0) //Have I missed childrens in the tree?
                    {
                        addMissedChilds(myMissedChilds, node);  // Oh, hello boys!
                    }
                    ((DefaultMutableTreeNode) root).add(node); //root is my uncle, by now I'm fine with he (but I will find my dad)
                }
            } else {
                ((DefaultMutableTreeNode) root).add(node); //Hi dad (my dad is root)
                if (myMissedChilds.size() > 0) //Have I missed childrens in the tree?
                {
                    addMissedChilds(myMissedChilds, node);  // Oh, hello boys!
                }
            }
        }
        showTree();
    }

    /**
     * Find a node in all the tree
     *
     * @param treeNode treeNode
     * @return The treeNode
     */
    public DefaultMutableTreeNode findNodeInTree(DefaultMutableTreeNode treeNode) {
        DefaultMutableTreeNode res = null;
        Enumeration childrens = ((DefaultMutableTreeNode) root).breadthFirstEnumeration();
        while (childrens.hasMoreElements() && res == null) {
            DefaultMutableTreeNode actualNode = (DefaultMutableTreeNode) childrens.nextElement();
            if (treeNode.getUserObject() != null && actualNode.getUserObject() != null) {
                FolderPojo treeNodeFolder = (FolderPojo) treeNode.getUserObject();
                FolderPojo actualNodeFolder = (FolderPojo) actualNode.getUserObject();
                if (treeNodeFolder.getFolderId().equals(actualNodeFolder.getFolderId())) {
                    res = actualNode;
                }
            }
        }
        return (res);
    }

    private List<DefaultMutableTreeNode> findMissedChildrens(DefaultMutableTreeNode parentTreeNode) {
        List<DefaultMutableTreeNode> res = new ArrayList<DefaultMutableTreeNode>();
        Enumeration childrens = root.children();
        while (childrens.hasMoreElements()) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) childrens.nextElement();
            FolderPojo parentTreeNodeFolder = (FolderPojo) parentTreeNode.getUserObject();
            FolderPojo treeNodeFolder = (FolderPojo) treeNode.getUserObject();
            if (parentTreeNodeFolder.getFolderId().equals(treeNodeFolder.getParentFolderId())) {
                res.add(treeNode);
            }
        }
        return (res);
    }

    private void addMissedChilds(List<DefaultMutableTreeNode> myMissedChilds, DefaultMutableTreeNode parentNode) {
        for (DefaultMutableTreeNode childTreeNode : myMissedChilds) {
            parentNode.add(childTreeNode);
        }
    }

    public void showTree() {
        printValues((DefaultMutableTreeNode) root);
    }

    private void printValues(DefaultMutableTreeNode node) {
        if (node.getUserObject() != null) {
            FolderPojo folderPojo = (FolderPojo) node.getUserObject();
            FolderPojo parentPojo = null;
            if (node.getParent() != null && ((DefaultMutableTreeNode) node.getParent()).getUserObject() != null) {
                parentPojo = (FolderPojo) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
            }
        }
        Enumeration childrens = node.children();
        while (childrens.hasMoreElements()) {
            printValues((DefaultMutableTreeNode) childrens.nextElement());
        }
    }

    /**
     * Gets all the folders that derives from this node
     *
     * @param parentNode Parent node
     * @return List of FolderPojo
     */
    public List<FolderPojo> getDescendantFoldersList(DefaultMutableTreeNode parentNode) {
        List<FolderPojo> foldersList = new ArrayList<FolderPojo>();
        Enumeration nodesEnum = parentNode.depthFirstEnumeration();
        while (nodesEnum.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodesEnum.nextElement();
            if (node.getUserObject() != null) {
                FolderPojo folderPojo = (FolderPojo) node.getUserObject();
                foldersList.add(folderPojo);
            }
        }
        return (foldersList);
    }

    /**
     * Sort the list of folders, by folderName
     *
     * @param folders folders
     * @return Folders list sorted by name
     */
    public List<FolderPojo> sortList(List<FolderPojo> folders) {
        List<FolderPojo> res = new ArrayList<FolderPojo>();
        SortedMap<String, FolderPojo> tempMap = new TreeMap<String, FolderPojo>();
        for (FolderPojo folderPojo : folders) {
            tempMap.put(folderPojo.getFolderName().toLowerCase(), folderPojo);
        }

        for (String s : tempMap.keySet()) {
            res.add(tempMap.get(s));
        }
        return (res);
    }
}
