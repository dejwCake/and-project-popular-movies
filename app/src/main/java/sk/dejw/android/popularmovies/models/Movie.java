package sk.dejw.android.popularmovies.models;

public class Movie {
    private String title;
    private String posterPath;
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    public Movie(String vTitle, String vPosterPath)
    {
        this.setTitle(vTitle);
        this.setPosterPath(vPosterPath);
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
        return IMAGE_URL.concat(posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
