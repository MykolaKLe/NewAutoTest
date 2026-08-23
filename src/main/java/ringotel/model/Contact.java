package ringotel.model;

public class Contact {

    private String fullName;
    private String jobTitle;
    private String company;
    private String phone;
    private String email;
    private String notes;

    public String getFullName() {
        return fullName;
    }

    public Contact setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Contact setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public Contact setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public Contact setNotes(String notes) {
        this.notes = notes;
        return this;
    }
}