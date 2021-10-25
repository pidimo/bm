package com.piramide.elwis.web.webmail.form;

/**
 * AlfaCentauro Team
 * This class helps to the view of the results of a search
 *
 * @author Alvaro
 * @version $Id: SearchResultForm.java 8379 2008-07-03 16:19:52Z ivan $
 */
public class SearchResultForm extends MailTrayForm {

    Object searchText;  //Text to search
    Object searchFilter; //Filter for the search
    Object searchFolder; //Folder to consider for the search


    public SearchResultForm() {
        super();
        searchText = null;
        searchFilter = null;
        searchFolder = null;
    }

    public Object getSearchText() {
        return (this.searchText);
    }

    public void setSearchText(Object searchText) {
        this.searchText = searchText;
    }

    public Object getSearchFilter() {
        return (this.searchFilter);
    }

    public void setSearchFilter(Object searchFilter) {
        this.searchFilter = searchFilter;
    }

    public Object getSearchFolder() {
        return (this.searchFolder);
    }

    public void setSearchFolder(Object searchFolder) {
        this.searchFolder = searchFolder;
    }
}
