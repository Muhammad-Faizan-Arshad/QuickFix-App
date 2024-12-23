package Backup.QuickFix.model;

public class Review {
    private String userID;
    private float rating;
    private String reviewText;


    public Review() {
    }

    public Review(String userID, float rating, String reviewText) {
        this.userID = userID;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
