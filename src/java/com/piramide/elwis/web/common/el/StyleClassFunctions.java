package com.piramide.elwis.web.common.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JSP 2.0 Functions.
 * <p/>
 * Style class of bootstrap to jsp
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class StyleClassFunctions {
    private static Log log = LogFactory.getLog(StyleClassFunctions.class);

    public static String getFormPanelClasses() {
        return "panel panel-default panel-body formPanelBody";
    }

    public static String getFormLabelClasses() {
        return "control-label col-xs-12 col-sm-3";
    }

    public static String getFormContainClasses(Boolean view) {
        if (view != null && view) {
            return getFormContainViewClasses();
        }
        return "parentElementInputSearch col-xs-11 col-sm-8";
    }

    public static String getFormContainViewClasses() {
        return getFormContainClasses(null) + " form-control-static";
    }

    public static String getFormButtonWrapperClasses() {
        return "wrapperButton input-group";
    }

    public static String getFormSelectClasses() {
        return "form-control ns-search-select";
    }

    public static String getFormButtonClasses() {
        return "btn btn-default";
    }

    public static String getGeneralFantabulousTableClasses() {
        return "table table-striped table-hover table-bordered displayTable table_same_row";
    }

    public static String getFantabulousTableClases() {
        return getGeneralFantabulousTableClasses() + " mobileTableDefault";
    }

    public static String getFantabulousTableLargeClases() {
        return getGeneralFantabulousTableClasses() + " mobileTableLarge";
    }

    public static String getFormContainForPopUpClasses(Boolean view) {
        if (view != null && view) {
            return "col-xs-9 col-sm-6 form-control-static";
        }
        return "col-xs-9 col-sm-6";
    }

    public static String getFormGroupTwoSearchInput() {
        return "form-group col-sm-5";
    }

    public static String getFormGroupTwoSearchButton() {
        return "form-group col-sm-2";
    }

    public static String getFormLabelOneSearchInput() {
        return "control-label col-xs-12 col-sm-2";
    }

    public static String getFormOneSearchInput() {
        return "col-xs-12 col-sm-4";
    }

    public static String getFormSmallLabelClasses() {
        return "control-label col-xs-12 col-sm-2";
    }

    public static String getFormBigContainClasses(Boolean view) {
        if (view != null && view) {
            return getFormBigContainViewClasses();
        }
        return "parentElementInputSearch col-xs-11 col-sm-9";
    }

    public static String getFormBigContainViewClasses() {
        return getFormBigContainClasses(null) + " form-control-static";
    }

    public static String getFormGroupClasses() {
        return "form-group";
    }

    public static String getFormButtonCancelClasses() {
        return "btn btn-primary cancel";
    }

    public static String getFormInputClasses() {
        return "form-control";
    }

    public static String getFormClasses() {
        return "col-xs-12 col-sm-10 col-md-8 col-sm-offset-1 col-md-offset-2";
    }

    public static String getFormColorPickerClasses(Boolean isDelete) {
        if (null != isDelete && isDelete)
            return getFormInputClasses();
        return getFormInputClasses() + " colorPicker";
    }

    public static String getAlphabetWrapperClasses() {
        return "alphabet-wrapper";
    }

    public static String getSearchWrapperClasses() {
        return getFormGroupClasses();
    }

    public static String getListWrapperClasses() {
        return "list-container";
    }

    public static String getFormLabelSearchPopUp() {
        return "control-label col-xs-2 label-left";
    }

    public static String getFormInputSearchPopUP() {
        return "col-xs-10";
    }

    /**
     * label classes in forms with TWO COLUMNS
     *
     * @return string
     */

    public static String getFormLabelClassesTwoColumns() {
        return "control-label col-xs-12 col-sm-4 col-md-3";
    }

    /**
     * contain classes in forms with TWO COLUMNS
     *
     * @return string
     */

    public static String getFormContainClassesTwoColumns(Boolean view) {
        if (null != view && view)
            return getFormContainViewClassesTwoColumns();
        return "parentElementInputSearch col-xs-12 col-sm-7 col-md-8";
    }

    public static String getFormContainViewClassesTwoColumns() {
        return getFormContainClassesTwoColumns(null) + " form-control-static";
    }

    public static String getFormContainClassesTwoColumnsLarge(Boolean view) {
        if (null != view && view)
            return getFormContainViewClassesTwoColumnsLarge();
        return "parentElementInputSearch col-xs-12 col-sm-7 col-md-9";
    }

    public static String getFormContainViewClassesTwoColumnsLarge() {
        return getFormContainClassesTwoColumnsLarge(null) + " form-control-static";
    }

    /**
     * wrapper classes in forms with TWO COLUMNS
     *
     * @return string
     */

    public static String getFormWrapperTwoColumns() {
        return "col-xs-12";
    }

    /**
     * form group classes in forms with TWO COLUMNS
     *
     * @return string
     */

    public static String getFormGroupClassesTwoColumns() {
        return "form-group col-xs-12 col-sm-12 col-md-6";
    }

    /**
     * label classes in forms with RENDER CATEGORY
     *
     * @return string
     */

    public static String getFormLabelRenderCategory() {
        return "control-label col-xs-12 col-sm-4 col-md-4";
    }

    /**
     * contain classes in forms with RENDER CATEGORY
     *
     * @return string
     */

    public static String getFormContainRenderCategory(Boolean view) {
        if (null != view && view)
            return getFormContainViewRenderCategory();
        return "parentElementInputSearch col-xs-11 col-sm-7 col-md-6";
    }

    public static String getFormContainViewRenderCategory() {
        return getFormContainRenderCategory(null) + " form-control-static";
    }

    /*
    form one column large
     */
    public static String getFormClassesLarge() {
        return "col-lg-10 col-lg-offset-1";
    }

    /*
    forms with buttons inline
    */
    public static String getFormButtonInlineClasses() {

        return getFormButtonClasses() + " pull-left marginRight marginButton wrapperButton";
    }

    /*
    forms with cancel button inline
    */
    public static String getFormButtonCancelInlineClasses() {

        return getFormButtonCancelClasses() + " pull-left marginRight marginButton wrapperButton";
    }

    /**
     * row classes in forms with TWO COLUMNS
     *
     * @return string
     */

    public static String getRowClassesTwoColumns() {
        return "row col-xs-12 col-sm-12 col-md-6";
    }

    /**
     * icons classes
     *
     * @return string
     */

    public static String getClassGlyphTrash() {
        return "glyphicon glyphicon-trash trashClassColor centerClass";
    }

    public static String getClassGlyphEdit() {
        return "glyphicon glyphicon-edit editClassColor centerClass";
    }

    public static String getClassGlyphPerson() {
        return "fa fa-user personClassColor centerClass";
    }

    public static String getClassGlyphPrivatePerson() {
        return "fa fa-user privatePersonClassColor centerClass";
    }

    public static String getClassGlyphOrganization() {
        return "fa fa-users organizationClassColor centerClass";
    }

    public static String getClassGlyphDownload() {
        return "glyphicon glyphicon-download-alt downloadClassColor centerClass";
    }

    public static String getClassGlyphGenerate() {
        return "glyphicon glyphicon-download-alt generateClassColor centerClass";
    }

    public static String getClassGlyphOk() {
        return "glyphicon glyphicon-ok okClassColor centerClass";
    }

    public static String getClassGlyphListAlt() {
        return "glyphicon glyphicon-list-alt listaltClassColor centerClass";
    }

    public static String getClassGlyphOpen() {
        return "glyphicon glyphicon-open openClassColor centerClass";
    }

    public static String getClassGlyphImport() {
        return "glyphicon glyphicon-import importClassColor centerClass";
    }

    public static String getClassGlyphRefresh() {
        return "glyphicon glyphicon-refresh refreshClassColor centerClass";
    }

    public static String getClassGlyphSave() {
        return "glyphicon glyphicon-save saveClassColor centerClass";
    }

    public static String getClassGlyphKey() {
        return "fa fa-key KeyClassColor centerClass";
    }

    public static String getClassGlyphRemoveSign() {
        return "glyphicon glyphicon-remove-sign RemoveSignClassColor centerClass";
    }

    public static String getClassGlyphMerge() {
        return "glyphicon glyphicon glyphicon-transfer mergeClassColor centerClass";
    }

    public static String getClassGlyphOpenLink(){
        return "glyphicon glyphicon-globe openLinkClassColor centerClass";
    }

    public static String getClassGlyphViewArticleAnswer(){
        return "fa fa-lightbulb-o viewArticleAnswerClassColor centerClass";
    }

    public static String getClassGlyphContactGroupEdit(){
        return "fa fa-users contactGroupEditClassColor centerClass";
    }

    public  static String getClassGlyphSalesProcess(){
        return "glyphicon glyphicon-usd salesProcessClassColor centerClass";
    }

    public  static String getClassGlyphSalesProcessTwo(){
        return "glyphicon glyphicon-usd salesProcessTwoClassColor centerClass";
    }

    public static String getClassGlyphPhoneTwo(){
        return "glyphicon glyphicon-phone-alt phoneTwoClassColor centerClass";
    }

    public static String getClassGlyphReply(){
        return "fa fa-reply replyClassColor centerClass";
    }

    public static String getClassGlyphForward(){
        return "fa fa-share forwardClassColor centerClass";
    }

    public static String getClassGlyphPaperClip(){
        return "glyphicon glyphicon-paperclip paperClipClassColor centerClass";
    }

    public static String getClassGlyphPrioHigh(){
        return "fa fa-exclamation prioHighClassColor centerClass";
    }

    public static String getClassGlyphOpenFolder(){
        return "glyphicon glyphicon-folder-open openFolderClassColor centerClass";
    }

    public static String getClassGlyphKeyLock(){
        return "fa fa-lock keyLockClassColor centerClass";
    }

    public static String getTableClasesIntoForm() {
        return "table borderless addBorderBottom table_same_row";
    }

}
