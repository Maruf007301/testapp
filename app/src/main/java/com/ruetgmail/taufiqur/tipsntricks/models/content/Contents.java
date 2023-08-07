package com.ruetgmail.taufiqur.tipsntricks.models.content;

import android.os.Parcel;
import android.os.Parcelable;

public class Contents implements Parcelable {
    String title;
    String subTitle;
    String imageUrl;
    String details;
    boolean isFavorite;

    public Contents(String title, String subTitle, String imageUrl, String details, boolean isFavorite) {
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.details = details;
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetails() {
        return details;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(details);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }

    protected Contents(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        details = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static Creator<Contents> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Contents> CREATOR = new Creator<Contents>() {
        @Override
        public Contents createFromParcel(Parcel source) {
            return new Contents(source);
        }

        @Override
        public Contents[] newArray(int size) {
            return new Contents[size];
        }
    };
}