package com.piramide.elwis.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectUserPermissionUtil {
    public static enum Permission {
        NEW(1, "NEW"),
        VIEW(2, "VIEW"),
        CONFIRM(4, "CONFIRM"),
        ADMIN(8, "ADMIN");

        private int value;
        private String name;

        Permission(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static LinkedList<Permission> getPermissions() {
            LinkedList<Permission> result = new LinkedList<Permission>();
            result.add(ADMIN);
            result.add(CONFIRM);
            result.add(VIEW);
            result.add(NEW);
            return result;
        }

        public static Permission getPermission(int value) {
            List<Permission> permissions = getPermissions();
            for (Permission permission : permissions) {
                if (permission.getValue() == value) {
                    return permission;
                }
            }

            return null;
        }

        public static Permission getPermission(String name) {
            List<Permission> permissions = getPermissions();
            for (Permission permission : permissions) {
                if (permission.getName().equals(name)) {
                    return permission;
                }
            }

            return null;
        }
    }

    public static List<Permission> extractPermissions(int number) {
        List<Permission> result = new ArrayList<Permission>();

        for (Permission permission : Permission.getPermissions()) {
            if (number - permission.getValue() > 0) {
                result.add(permission);
                number = number - permission.getValue();
            }
            if (number - permission.getValue() == 0) {
                result.add(permission);
                break;
            }
        }

        return result;

    }

    public static boolean hasPermission(Byte value, int permission) {
        return value != null && (permission == (value.byteValue() & permission));
    }

    public static boolean hasPermission(Byte value, Permission permission) {
        return hasPermission(value, permission.getValue());
    }

    public static List<Permission> getEnabledPermissions(Byte value) {
        List<Permission> result = new ArrayList<Permission>();

        List<Permission> permissions = Permission.getPermissions();
        for (Permission permission : permissions) {
            if (hasPermission(value, permission)) {
                result.add(permission);
            }
        }

        return result;
    }

    public static int getAllPermissions() {
        return Permission.ADMIN.value +
                Permission.CONFIRM.getValue() +
                Permission.VIEW.getValue() + Permission.NEW.getValue();
    }
}
