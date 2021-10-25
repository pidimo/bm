package com.piramide.elwis.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: SchedulerPermissionUtil.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 05-05-2005 04:12:26 PM ivan Exp $
 */
public class SchedulerPermissionUtil {
    private static Log log = LogFactory.getLog(com.piramide.elwis.utils.SchedulerPermissionUtil.class);

    public static final int READ = 1;
    public static final int ADD = 2;
    public static final int EDIT = 4;
    public static final int DELETE = 8;
    public static final int ANONYM = 16;

    public static final String PERMISSION_READ = "READ";
    public static final String PERMISSION_ADD = "ADD";
    public static final String PERMISSION_EDIT = "EDIT";
    public static final String PERMISSION_DELETE = "DELETE";
    public static final String PERMISSION_ANONYM = "ANONYM";

    private final static List permissions;

    static {
        permissions = new ArrayList();
        permissions.add(new Integer(ANONYM));
        permissions.add(new Integer(DELETE));
        permissions.add(new Integer(EDIT));
        permissions.add(new Integer(ADD));
        permissions.add(new Integer(READ));
    }


    public static boolean hasSchedulerAccessRight(Byte accessRight, int permission) {
        return accessRight != null && (permission == (accessRight.byteValue() & permission));
    }

    public static boolean hasSchedulerAccessRight(Byte accessRight, String permission) {
        return (convertPermissionToInt(permission) == (accessRight.byteValue() & convertPermissionToInt(permission)));
    }

    public static int convertPermissionToInt(String permission) {
        if (PERMISSION_READ.equals(permission)) {
            return READ;
        } else if (PERMISSION_ADD.equals(permission)) {
            return ADD;
        } else if (PERMISSION_EDIT.equals(permission)) {
            return EDIT;
        } else if (PERMISSION_DELETE.equals(permission)) {
            return DELETE;
        } else if (PERMISSION_ANONYM.equals(permission)) {
            return ANONYM;
        }

        throw new RuntimeException("Permission:  " + permission + "     is not a valid Scheduler permission. Check class: com.piramide.elwis.utils.PermissionUtil to know which are.");

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

    public static List<Integer> extractPermissions(Byte permission) {
        return (permission != null ? extractPermissions(permission.intValue()) : new ArrayList());
    }

    public static List getListStringPermissions() {
        List permissions = new ArrayList();
        permissions.add(SchedulerPermissionUtil.PERMISSION_READ);
        permissions.add(SchedulerPermissionUtil.PERMISSION_ADD);
        permissions.add(SchedulerPermissionUtil.PERMISSION_EDIT);
        permissions.add(SchedulerPermissionUtil.PERMISSION_DELETE);
        permissions.add(SchedulerPermissionUtil.PERMISSION_ANONYM);
        return permissions;
    }


    public static List getListIntPermissions() {
        return permissions;
    }


    public static List checkPermissionAllowed(java.lang.Byte permission) {

        List actualPermissions = new ArrayList();
        actualPermissions = extractPermissions(permission.intValue());

        List result = new ArrayList();
        List permissions = getListStringPermissions();

        for (int i = 0; i < permissions.size(); i++) {
            String s = (String) permissions.get(i);

            if (hasSchedulerAccessRight(permission, s)) {
                Map permissionMap = new HashMap();

                if (actualPermissions.contains(new Integer(convertPermissionToInt(s)))) {
                    permissionMap.put("isChecked", Boolean.valueOf("true"));
                } else {
                    permissionMap.put("isChecked", Boolean.valueOf("false"));
                }

                permissionMap.put("value", new Integer(convertPermissionToInt(s)));
                permissionMap.put("stringValue", s);
                result.add(permissionMap);
            }
        }

        List sortList = new ArrayList();

        for (int i = 0; i < getListStringPermissions().size(); i++) {
            Map blank = new HashMap();
            blank.put("blank", new Boolean(true));
            sortList.add(blank);
        }

        for (int i = 0; i < result.size(); i++) {
            Map map = (Map) result.get(i);

            if (PERMISSION_READ.equals(map.get("stringValue"))) {
                sortList.remove(0);
                sortList.add(0, map);
            } else {

                if (PERMISSION_ADD.equals(map.get("stringValue"))) {
                    sortList.remove(1);
                    sortList.add(1, map);
                } else {
                    if (PERMISSION_EDIT.equals(map.get("stringValue"))) {
                        sortList.remove(2);
                        sortList.add(2, map);
                    } else {
                        if (PERMISSION_DELETE.equals(map.get("stringValue"))) {
                            sortList.remove(3);
                            sortList.add(3, map);
                        } else {

                            if (PERMISSION_ANONYM.equals(map.get("stringValue"))) {
                                sortList.remove(4);
                                sortList.add(4, map);
                            }
                        }
                    }
                }
            }
        }

        return sortList;
    }

    /**
     * verif if this user have a permission especify
     *
     * @param ActualPermission , is the actual permission the user
     * @param verifPermission  , to verif if this permission have the user
     * @return boolean, true or false
     */
    public static boolean havePermission(Byte ActualPermission, String verifPermission) {
        log.debug("Executing metohod havePermission ... ?" + verifPermission + " with:" + ActualPermission);
        boolean res = false;

        List listPermissionAllow = SchedulerPermissionUtil.checkPermissionAllowed(ActualPermission);
        log.debug(". . . permission allow" + listPermissionAllow);
        for (Iterator iterator2 = listPermissionAllow.iterator(); iterator2.hasNext();) {
            Map mapPermss = (Map) iterator2.next();

            if (mapPermss.get("blank") == null) {
                String permission = mapPermss.get("stringValue").toString();
                if (permission.equals(verifPermission)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Verify if this permission contain only ANONYM permission
     *
     * @param persmission
     * @return true or false
     */
    public static boolean hasOnlyAnonymousPermission(Byte persmission) {
        List<Integer> permissionList = SchedulerPermissionUtil.extractPermissions(persmission);
        return (permissionList.size() == 1 && permissionList.get(0).intValue() == SchedulerPermissionUtil.ANONYM);
    }

    public static boolean hasPermissions(Byte permissions) {
        List<Integer> permissionList = extractPermissions(permissions);
        return (permissionList.size() > 0);
    }

}
