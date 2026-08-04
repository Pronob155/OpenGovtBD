package com.opengovtbd.model;

/** A super admin with full system oversight. */
public class Admin extends User {

    private String email;

    public Admin(String fullName, String email, String password) {
        super(fullName, password);
        this.email = email;
    }

    @Override
    public String getDashboardUrl() { return "/admin/dashboard"; }

    @Override
    public String getDisplayRole() { return "Super Admin"; }

    @Override
    public Role getRole() { return Role.ADMIN; }

    @Override
    public String getLoginIdentifier() { return email; }

    public String getEmail() { return email; }
}
