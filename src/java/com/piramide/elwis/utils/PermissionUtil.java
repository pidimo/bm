package com.piramide.elwis.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class to manage persmision constants and utility methods.
 * 00000 bits for the byte.
 *
 * @author Fernando Montaño
 * @version $Id: PermissionUtil.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class PermissionUtil {
    public static final int VIEW = 1;
    public static final int CREATE = 2;
    public static final int UPDATE = 4;
    public static final int DELETE = 8;
    public static final int EXECUTE = 16;

    public static final String PERMISSION_VIEW = "VIEW";
    public static final String PERMISSION_CREATE = "CREATE";
    public static final String PERMISSION_UPDATE = "UPDATE";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_EXECUTE = "EXECUTE";


    /**
     * Checks if the permission required has the access right for the given permission
     *
     * @param accessRight the functionality access right.
     * @param permission  the permission to check if is allowed.
     * @return .
     */
    public static boolean hasAccessRight(Byte accessRight, int permission) {
        return (permission == (accessRight.byteValue() & permission));
    }

    public static boolean hasAccessRight(Byte accessRight, String permission) {
        return (convertPermissionToInt(permission) == (accessRight.byteValue() & convertPermissionToInt(permission)));
    }

    public static int convertPermissionToInt(String permission) {
        if (PERMISSION_VIEW.equals(permission)) {
            return VIEW;
        } else if (PERMISSION_CREATE.equals(permission)) {
            return CREATE;
        } else if (PERMISSION_UPDATE.equals(permission)) {
            return UPDATE;
        } else if (PERMISSION_DELETE.equals(permission)) {
            return DELETE;
        } else if (PERMISSION_EXECUTE.equals(permission)) {
            return EXECUTE;
        }

        throw new RuntimeException("Permission:  " + permission + "     is not a valid system permission. Check class: com.piramide.elwis.utils.PermissionUtil to know which are.");

    }

    /**
     * This class extracts all permissions where "number" is valid<br>
     * eg.<br>
     * number = 15  then permissions allowed : VIEW, CREATE, UPDATE, DELETE<br>
     * permissions 1 2 4 8<br>
     * number   permission     remainder<br>
     * 15     -       8       = 6 <br>
     * 7      -       4       = 3 <br>
     * 3      -       2       = 1 <br>
     * 1      -       1       = 0
     *
     * @param number int that represents the sumatory of all permissions
     * @return list that contains the permissions
     */
    public static List extractPermissions(int number) {
        List permissions = getListIntPermissions();
        List result = new ArrayList();

        for (int i = 0; i < permissions.size(); i++) {
            Integer permission = (Integer) permissions.get(i);
            if ((number - permission.intValue()) > 0) {
                result.add(permission);
                number = number - permission.intValue();
            }
            if ((number - permission.intValue()) == 0) {
                result.add(permission);
                break;
            }
        }
        return result;

    }

    public static List getListStringPermissions() {
        List permissions = new ArrayList();
        permissions.add(PermissionUtil.PERMISSION_VIEW);
        permissions.add(PermissionUtil.PERMISSION_CREATE);
        permissions.add(PermissionUtil.PERMISSION_UPDATE);
        permissions.add(PermissionUtil.PERMISSION_DELETE);
        permissions.add(PermissionUtil.PERMISSION_EXECUTE);
        return permissions;
    }

    public static List getListIntPermissions() {
        List permissions = new ArrayList();
        permissions.add(new Integer(EXECUTE));
        permissions.add(new Integer(DELETE));
        permissions.add(new Integer(UPDATE));
        permissions.add(new Integer(CREATE));
        permissions.add(new Integer(VIEW));
        return permissions;
    }
}
