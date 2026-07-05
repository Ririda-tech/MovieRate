package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a movie entry in the catalogue.
 * Follows single-responsibility: holds data only.
 */
public class Movie {
    private final String title;
    private final String genre;
    private final double rating;
    private final String director;
    private final String duration;
    private final String synopsis;
    private final String posterFileName;

    public Movie(String title, String genre, double rating,
                 String director, String duration, String synopsis,
                 String posterFileName) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.director = director;
        this.duration = duration;
        this.synopsis = synopsis;
        this.posterFileName = posterFileName;
    }

    public String getTitle()        { return title; }
    public String getGenre()        { return genre; }
    public double getRating()       { return rating; }
    public String getDirector()     { return director; }
    public String getDuration()     { return duration; }
    public String getSynopsis()     { return synopsis; }
    public String getPosterFileName() { return posterFileName; }

    // --------------------------------------------------------
    // Catalogue — central list of all movies in the app
    // --------------------------------------------------------
    public static List<Movie> getCatalogue() {
        List<Movie> list = new ArrayList<>();


        list.add(new Movie(
            "Avengers",
            "Action",
            4.8,
            "Anthony & Joe Russo",
            "181 min",
            "After the devastating events of Infinity War, the Avengers assemble once more to reverse Thanos's actions and restore balance to the universe.",
            "avengers.jpg"
        ));

        list.add(new Movie(
            "Parasite",
            "Drama",
            4.7,
            "Bong Joon-ho",
            "132 min",
            "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
            "parasite.jpg"
        ));

        list.add(new Movie(
            "Dune",
            "Adventure",
            4.8,
            "Denis Villeneuve",
            "155 min",
            "Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding, must travel to the most dangerous planet in the universe to ensure the future of his family and his people.",
            "dune.jpg"
        ));

        list.add(new Movie(
            "Batman",
            "Action",
            4.6,
            "Matt Reeves",
            "176 min",
            "When a sadistic serial killer begins murdering key political figures in Gotham, Batman is forced to investigate the city's hidden corruption.",
            "batman.jpg"
        ));

        list.add(new Movie(
            "Oppenheimer",
            "Drama",
            4.9,
            "Christopher Nolan",
            "180 min",
            "The story of J. Robert Oppenheimer's role in the development of the atomic bomb during World War II.",
            "oppenheimer.jpg"
        ));

        return list;
    }
}
