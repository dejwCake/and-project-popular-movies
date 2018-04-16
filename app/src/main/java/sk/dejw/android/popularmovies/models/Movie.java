package sk.dejw.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    private Integer id;
    private String title;
    private String posterPath;
    private String plot;
    private Double rating;
    private Date releaseDate;

    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static final String SIZE_W92 = "w92/";
    public static final String SIZE_W154 = "w154/";
    public static final String SIZE_W185 = "w185/";
    public static final String SIZE_W342 = "w342/";
    public static final String SIZE_W500 = "w500/";
    public static final String SIZE_W780 = "w780/";
    public static final String SIZE_ORIGINAL = "original/";

    public Movie(Integer vId, String vTitle, String vPosterPath, String vOverview, Double vVoteAverage, Date vReleaseDate)
    {
        id = vId;
        title = vTitle;
        posterPath = vPosterPath;
        plot = vOverview;
        rating = vVoteAverage;
        releaseDate = vReleaseDate;
    }

    public Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.plot = in.readString();
        this.rating = in.readDouble();
        this.releaseDate = new Date(in.readLong());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getFullPosterPath(String size) {
        return IMAGE_URL.concat(size).concat(posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(plot);
        parcel.writeDouble(rating);
        parcel.writeLong(releaseDate.getTime());
    }

    static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
