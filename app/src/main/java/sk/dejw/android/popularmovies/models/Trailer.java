package sk.dejw.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Trailer implements Parcelable {
    private String id;
    private String name;
    private String key;

    public static final String YOU_TUBE_URL = "https://www.youtube.com/watch?v=";

    public Trailer(String vId, String vName, String vKey)
    {
        id = vId;
        name = vName;
        key = vKey;
    }

    public Trailer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.key = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getYouTubePath() {
        return YOU_TUBE_URL.concat(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(key);
    }

    static final Creator<Trailer> CREATOR
            = new Creator<Trailer>() {

        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
