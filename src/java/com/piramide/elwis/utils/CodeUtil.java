package com.piramide.elwis.utils;


/**
 * Util to determine the code of an address. Manging codes as byte.
 *
 * @author Ernesto
 * @version $Id: CodeUtil.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class CodeUtil {
    public static final int default_ = 1;
    public static final int employee = 2;
    public static final int customer = 4;
    public static final int company = 8;
    public static final int supplier = 16;


    static public boolean isDefault(String code) throws NumberFormatException {
        Byte var = new Byte(code);
        return default_ == (var.byteValue() & default_);
    }

    static public boolean isEmployee(String code) throws NumberFormatException {
        Byte var = new Byte(code);
        return employee == (var.byteValue() & employee);
    }

    static public boolean isCustomer(String code) throws NumberFormatException {
        Byte var = new Byte(code);
        return customer == (var.byteValue() & customer);
    }

    static public boolean isCompany(String code) throws NumberFormatException {
        Byte var = new Byte(code);
        return company == (var.byteValue() & company);
    }

    static public boolean isSupplier(String code) throws NumberFormatException {
        Byte var = new Byte(code);
        return supplier == (var.byteValue() & supplier);
    }

    static public boolean isDefault(byte code) throws NumberFormatException {
        Byte var = new Byte(code);
        return default_ == (var.byteValue() & default_);
    }

    static public boolean isEmployee(byte code) throws NumberFormatException {
        Byte var = new Byte(code);
        return employee == (var.byteValue() & employee);
    }

    static public boolean isCustomer(byte code) throws NumberFormatException {
        Byte var = new Byte(code);
        return customer == (var.byteValue() & customer);
    }

    static public boolean isCompany(byte code) throws NumberFormatException {
        Byte var = new Byte(code);
        return company == (var.byteValue() & company);
    }

    static public boolean isSupplier(byte code) throws NumberFormatException {
        Byte var = new Byte(code);
        return supplier == (var.byteValue() & supplier);
    }

    public static String addCode(Byte code, int codeToAdd) {
        return String.valueOf(code.intValue() + codeToAdd);
    }

    public static String addCode(int code, int codeToAdd) {
        return addCode(new Byte(String.valueOf(code)), codeToAdd);
    }

    public static byte addCode(byte code, int codeToAdd) {
        code += codeToAdd;
        return code;
    }

    public static byte removeCode(byte code, int codeToRomove) {
        code -= codeToRomove;
        return code;
    }
}
