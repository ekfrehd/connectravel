package org.ezone.room.constant;

public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    SELLER("ROLE_SELLER");

    private String role;
    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
