package com.opengovtbd.model;

public class Officer extends User {

    private String officerId;
    private String governmentEmail;
    private String department;
    private String designation;

    public Officer(String fullName, String officerId, String governmentEmail,
                    String department, String designation, String password) {
        super(fullName, password);
        this.officerId = officerId;
        this.governmentEmail = governmentEmail;
        this.department = department;
        this.designation = designation;
    }

    @Override
    public String getDashboardUrl() { return "/officer/dashboard"; }

    @Override
    public String getDisplayRole() { return "Government Officer"; }

    @Override
    public Role getRole() { return Role.OFFICER; }

    @Override
    public String getLoginIdentifier() { return officerId; }

    public String getOfficerId() { return officerId; }
    public String getGovernmentEmail() { return governmentEmail; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}
