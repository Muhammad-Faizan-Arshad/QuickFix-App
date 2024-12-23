package Backup.QuickFix.model;

public class Mechanic {
    private String mechanicID;
    private String username;
    private Long age;
    private String experience;
    private String city;
    private String mobileNumber;
    private String about;
    private Float rating;
    private String fullname;
    private String email;


    public Mechanic() {}

    public Mechanic(String mechanicID, String fullname, String username, String email, String role) {
        this.mechanicID = mechanicID;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.age = null;
        this.experience = "";
        this.city = "";
        this.mobileNumber = "";
        this.about = "";
        this.rating = 0.0f;
    }

    public Mechanic(String mechanicID, String mobileNumber, Long age, String experience, String city, String fullname, String about) {
        this.mechanicID = mechanicID;
        this.mobileNumber = mobileNumber;
        this.age = age;
        this.experience = experience;
        this.city = city;
        this.about = about;
        this.rating = 0.0f;
        this.fullname = fullname;
        this.username = "";
        this.email = "";
    }

    // Getters and setters
    public String getMechanicID() {
        return mechanicID;
    }

    public void setMechanicID(String mechanicID) {
        this.mechanicID = mechanicID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
