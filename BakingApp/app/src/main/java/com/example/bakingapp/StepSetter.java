package com.example.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class StepSetter implements Parcelable {
    private String short_description;
    private String description;
    private String videoURL;
    private String id;

    protected StepSetter(Parcel in) {
        short_description = in.readString();
        description = in.readString();
        videoURL = in.readString();
        id = in.readString();
    }

    public static final Creator<StepSetter> CREATOR = new Creator<StepSetter>() {
        @Override
        public StepSetter createFromParcel(Parcel in) {
            return new StepSetter(in);
        }

        @Override
        public StepSetter[] newArray(int size) {
            return new StepSetter[size];
        }
    };

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StepSetter() {
    }

    public StepSetter(String short_description, String description, String videoURL, String id) {
        this.short_description = short_description;
        this.description = description;
        this.videoURL = videoURL;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(short_description);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(id);
    }
}
