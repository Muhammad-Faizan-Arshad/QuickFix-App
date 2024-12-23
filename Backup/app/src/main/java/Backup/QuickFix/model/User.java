package Backup.QuickFix.model;

public class User {
    private String name;
    private String username;
    private String email;
    private String role;


    public User() {
    }

    public User(String name, String username, String email, String role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email; // Add getter for email
    }

    public void setEmail(String email) {
        this.email = email; // Add setter for email
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
