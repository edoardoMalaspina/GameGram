package it.unipi.gamegram;

import it.unipi.gamegram.Entities.Review;

public class ReviewSingleton {

    private static ReviewSingleton reviewS = null;
    private static Review review;

    private ReviewSingleton(Review review){
        this.review = review;
    }
    public static ReviewSingleton getInstance(Review review){
        if(review == null) {
            reviewS = new ReviewSingleton(review);
        }
        return reviewS;
    }

    public static Review getReview() {
        return review;
    }

    public static void setNull(){
        reviewS = null;
    }
}
