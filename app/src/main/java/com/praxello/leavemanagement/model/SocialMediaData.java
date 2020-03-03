package com.praxello.leavemanagement.model;

public class SocialMediaData {
    int socialMediaImage;
    String socialMediaName;

    public SocialMediaData(int socialMediaImage, String socialMediaName) {
        this.socialMediaImage = socialMediaImage;
        this.socialMediaName = socialMediaName;
    }

    public int getSocialMediaImage() {
        return socialMediaImage;
    }

    public void setSocialMediaImage(int socialMediaImage) {
        this.socialMediaImage = socialMediaImage;
    }

    public String getSocialMediaName() {
        return socialMediaName;
    }

    public void setSocialMediaName(String socialMediaName) {
        this.socialMediaName = socialMediaName;
    }
}
