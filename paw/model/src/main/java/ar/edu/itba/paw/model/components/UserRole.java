package ar.edu.itba.paw.model.components;

/**
 * User possible roles.
 */

public enum UserRole {
    ENTREPRENEUR("Entrepreneur", 1),
    INVESTOR("Investor", 2),
    NOTFOUND("Not found", 0);

    private String role;
    private int id;

    UserRole(String role, int id) {
        this.role = role;
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public static UserRole valueOf(int id) {
        for (UserRole role : UserRole.values()) {
            if (role.getId() == id)
                return role;
        }
        return NOTFOUND;
    }

    public static UserRole getEnum(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.getRole().equals(value))
                return role;
        }
        return NOTFOUND;
    }
}
