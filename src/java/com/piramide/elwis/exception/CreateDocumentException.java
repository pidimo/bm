package com.piramide.elwis.exception;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 9, 2004
 * Time: 9:46:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateDocumentException extends Exception {
    private String arg1;

    public CreateDocumentException() {
        super();
    }

    public CreateDocumentException(String message) {
        super(message);
        arg1 = null;
    }

    public CreateDocumentException(String message, String arg1) {
        super(message);
        this.arg1 = arg1;
    }

    public boolean hasArg() {
        return arg1 != null;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String toString() {
        return super.toString() + "- ARGUMENT:" + arg1;
        //To change body of overridden methods use File | Settings | File Templates.
    }
}
