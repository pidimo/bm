package com.piramide.elwis.web.webmail.util;

import com.piramide.elwis.cmd.webmailmanager.util.foldertree.FolderPojo;
import com.piramide.elwis.cmd.webmailmanager.util.foldertree.FolderTreeModel;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.utils.LinkEncoderUtil;
import com.piramide.elwis.web.utils.XmlDOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: FolderTreeUtil.java 20-may-2009 15:38:14
 */
public class FolderTreeUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private FolderTreeModel folderTreeModel;

    public FolderTreeUtil(FolderTreeModel folderTreeModel) {
        this.folderTreeModel = folderTreeModel;
    }

    public String getTreeAsUList(PageContext pageContext, Integer folderId) {
        String res = "";
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) folderTreeModel.getRoot();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            this.log.error("Error in creating an xmlDocument.... " + e.getMessage());
        }
        if (docBuilder != null) {
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("ul");
            rootElement.setAttribute("id", "webmailFolderTree");
            rootElement.setAttribute("class", "filetree treeview");
            doc.appendChild(rootElement);
            if (root.getChildCount() > 0) {
                List<Element> childsList = generateElements(doc, root.children(), pageContext, folderId);
                for (Element element : childsList) {
                    rootElement.appendChild(element);
                }
            }
            res = new XmlDOMUtil().DOMToString(doc);
            //res = res.substring(res.indexOf("?>") + 2);
        }
        return res;
    }

    private List<Element> generateElements(Document doc, Enumeration childrens,
                                           PageContext pageContext, Integer selectedFolderId) {
        List<Element> res = new ArrayList<Element>();
        while (childrens.hasMoreElements()) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) childrens.nextElement();
            FolderPojo folder = (FolderPojo) treeNode.getUserObject();

            Element listItem = doc.createElement("li");
            listItem.setAttribute("id", "folder_" + folder.getFolderId());
            Element span = getSpanElement(doc, folder, pageContext, treeNode, selectedFolderId);

            if (treeNode.getChildCount() > 0) {//persistense when childs
                if (folder.getIsOpen()) {
                    if (!childrens.hasMoreElements()) {
                        listItem.setAttribute("class", "collapsable lastCollapsable");
                    } else {
                        listItem.setAttribute("class", "collapsable");
                    }
                } else {
                    if (!childrens.hasMoreElements()) {
                        listItem.setAttribute("class", "expandable lastExpandable");
                    } else {
                        listItem.setAttribute("class", "expandable");
                    }
                }
                Element div = getDivForPersistence(folder, doc, !childrens.hasMoreElements());
                listItem.appendChild(div);
            } else {
                if (!childrens.hasMoreElements()) //persistence when no childrens
                {
                    listItem.setAttribute("class", "last");
                }
            }

            listItem.appendChild(span);
            if (treeNode.getChildCount() > 0) {
                Element ul = processChilds(doc, treeNode, pageContext, selectedFolderId);
                if (folder.getIsOpen()) //persistence
                {
                    ul.setAttribute("style", "display: block;");
                } else {
                    ul.setAttribute("style", "display: none;");
                }
                listItem.appendChild(ul);
            }

            addDragAndDropCssClass(listItem);

            res.add(listItem);
        }
        return (res);
    }

    /**
     * Add css class to interactuate with drag & drop functionality
     * @param listItem element
     */
    private void addDragAndDropCssClass(Element listItem) {
        String classAttr = listItem.getAttribute("class");
        if (classAttr != null) {
            classAttr = classAttr + " folderDropCls";
        } else {
            classAttr = "folderDropCls";
        }
        listItem.setAttribute("class", classAttr);
    }

    private boolean haveUnreadMails(DefaultMutableTreeNode parentNode) {
        boolean res = false;
        Enumeration nodesEnum = parentNode.depthFirstEnumeration();
        while (nodesEnum.hasMoreElements() && !res) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodesEnum.nextElement();
            if (node.getUserObject() != null) {
                FolderPojo folderPojo = (FolderPojo) node.getUserObject();
                res = folderPojo.getUnReadMails() > 0;
            }
        }
        return (res);
    }

    private Element getDivForPersistence(FolderPojo folder, Document doc, boolean isLast) {
        Element div = doc.createElement("div");

        if (folder.getIsOpen()) {
            if (isLast) {
                div.setAttribute("class", "hitarea collapsable-hitarea lastCollapsable-hitarea");
            } else {
                div.setAttribute("class", "hitarea collapsable-hitarea");
            }
        } else {
            if (isLast) {
                div.setAttribute("class", "hitarea expandable-hitarea lastExpandable-hitarea");
            } else {
                div.setAttribute("class", "hitarea expandable-hitarea");
            }

        }
        Comment comment = doc.createComment("required comment... :(");
        div.appendChild(comment);
        return (div);
    }

    private Element getSpanElement(Document doc, FolderPojo folder, PageContext pageContext, DefaultMutableTreeNode treeNode,
                                   Integer selectedFolderId) {
        //span
        Element span = doc.createElement("span");
        if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_INBOX)) {
            span.setAttribute("class", "inbox");
        } else if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_OUTBOX)) {
            span.setAttribute("class", "outbox");
        } else if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_SENDITEMS)) {
            span.setAttribute("class", "sent");
        } else if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_DRAFTITEMS)) {
            span.setAttribute("class", "draft");
        } else if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_TRASH)) {
            span.setAttribute("class", "trash");
        } else if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_DEFAULT)) {
            span.setAttribute("class", "folder");
        }

        //linkText
        String linkContent = folder.getFolderName();
        if (folder.getUnReadMails() > 0) {
            linkContent = linkContent + " (" + folder.getUnReadMails() + ")";
        }
        Text linkTextNode = doc.createTextNode(linkContent);
        //link
        Element link = doc.createElement("a");
        String encodedLink = LinkEncoderUtil.encodeLink(true,
                "/Mail/MailTray.do?folderId=" + folder.getFolderId() + "&index=0&orderParam(1)=SENTDATETIME-false",
                pageContext);
        link.setAttribute("href", encodedLink);

        String linkStyle = "";
        if (haveUnreadMails(treeNode)) {
            linkStyle = "font-weight: bold;";
        }

        if (selectedFolderId != null && folder.getFolderId().equals(selectedFolderId)) {
            linkStyle += "text-decoration: underline;";
        }
        if (linkStyle.length() > 0) {
            link.setAttribute("style", linkStyle);
        }
        link.appendChild(linkTextNode);

        span.appendChild(link);
        if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_TRASH)) {
            span.appendChild(createEmptyFolderLink(doc, pageContext, folder));
        }
        return (span);
    }

    private Element processChilds(Document doc, DefaultMutableTreeNode treeNode, PageContext pageContext, Integer selectedFolderId) {
        Element ul = doc.createElement("ul");
        List<Element> childElements = generateElements(doc, treeNode.children(), pageContext, selectedFolderId);
        for (Element childElement : childElements) {
            ul.appendChild(childElement);
        }
        return (ul);
    }

    private Element createEmptyFolderLink(Document doc, PageContext pageContext, FolderPojo folder) {
        Element link = doc.createElement("a");
        String encodedLink = LinkEncoderUtil.encodeLink(true,
                "/Mail/MailTray.do?folderId=${selectedFolderId}&emptyFolder=true&toEmptyFolder=" + folder.getFolderId() + "&mailFilter=${emailReadFilter}",
                pageContext);
        link.setAttribute("href", encodedLink);
        String linkStyle = "font-size: 9px;";
        if (linkStyle.length() > 0) {
            link.setAttribute("style", linkStyle);
        }
        Text linkTextElement = doc.createTextNode("[" + JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Webmail.folder.empty") + "]");
        link.appendChild(linkTextElement);
        return (link);
    }
}
