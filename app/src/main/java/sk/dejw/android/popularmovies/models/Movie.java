package sk.dejw.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    private String title;
    private String posterPath;
    private String plot;
    private Double rating;
    private Date releaseDate;

    public static final String IMAGE_URL_SMALL = "http://image.tmdb.org/t/p/w185/";
    public static final String IMAGE_URL_BIG = "http://image.tmdb.org/t/p/w342/";

    public Movie(String vTitle, String vPosterPath, String vOverview, Double vVoteAverage, Date vReleaseDate)
    {
        title = vTitle;
        posterPath = vPosterPath;
        plot = vOverview;
        rating = vVoteAverage;
        releaseDate = vReleaseDate;
    }

    public Movie(Parcel in) {
        this.title = in.readString();
        this.posterPath = in.readString();
        this.plot = in.readString();
        this.rating = in.readDouble();
        this.releaseDate = new Date(in.readLong());
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

    public String getFullPosterPath() {
        return IMAGE_URL_SMALL.concat(posterPath);
    }

    public String getBigFullPosterPath() {
        return IMAGE_URL_BIG.concat(posterPath);
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
