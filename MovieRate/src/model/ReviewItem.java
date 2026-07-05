package model;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents a single movie review created by a user.
 */
public class ReviewItem {
    private static int nextId = 1;

    private int id;
    private String username;
    private String movieTitle;
    private String rating;
    private String review;
    private String posterFileName;

    public ReviewItem() {
        this.id = nextId++;
    }

    public ReviewItem(String username, String movieTitle, String rating, String review, String posterFileName) {
        this();
        this.username = username;
        this.movieTitle = movieTitle;
        this.rating = rating;
        this.review = review;
        this.posterFileName = posterFileName;
    }

    // --------------------------------------------------------
    // Getters & Setters
    // --------------------------------------------------------
    public int getId()                             { return id; }
    public void setId(int id)                      { this.id = id; }

    public String getUsername()                    { return username; }
    public void setUsername(String username)       { this.username = username; }

    public String getMovieTitle()                  { return movieTitle; }
    public void setMovieTitle(String movieTitle)   { this.movieTitle = movieTitle; }

    public String getRating()                      { return rating; }
    public void setRating(String rating)           { this.rating = rating; }

    public String getReview()                      { return review; }
    public void setReview(String review)           { this.review = review; }

    public String getPosterFileName()              { return posterFileName; }
    public void setPosterFileName(String f)        { this.posterFileName = f; }

    // --------------------------------------------------------
    // Poster resolution
    // --------------------------------------------------------

    /**
     * Resolves the poster File for this review item.
     */
    public File resolvePosterFile() {
        File f = resolvePosterFileFromName(posterFileName);
        if (f != null) return f;

        String resolved = resolvePosterFileNameFromTitle(movieTitle);
        if (resolved == null || resolved.isBlank()) return null;

        return resolvePosterFileFromName(resolved);
    }

    /**
     * Resolves and returns the poster file name that actually exists on disk.
     */
    public String resolvePosterFileName() {
        File f = resolvePosterFileFromName(posterFileName);
        if (f != null) return f.getName();
        return resolvePosterFileNameFromTitle(movieTitle);
    }

    /**
     * Given a movie title, finds a matching poster file name by fuzzy-matching
     * against all files in the poster directories.
     */
    public static String resolvePosterFileNameFromTitle(String movieTitle) {
        if (movieTitle == null || movieTitle.isBlank()) return "";

        String normalizedTitle = normalizeName(movieTitle);
        
        List<File> dirs = getPosterBaseDirectories();
        System.out.println("[ReviewItem] resolvePoster for '" + movieTitle + "' checking " + dirs.size() + " dirs:");
        for (File d : dirs) {
            System.out.println("  dir=" + d.getAbsolutePath() + " exists=" + d.exists());
        }

        for (File posterDir : getPosterBaseDirectories()) {
            if (!posterDir.exists() || !posterDir.isDirectory()) continue;

            File[] files = posterDir.listFiles((dir, name) -> {
                String lower = name.toLowerCase(Locale.ROOT);
                return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
            });

            if (files == null) continue;

            for (File file : files) {
                String stem = normalizeName(stripExtension(file.getName()));
                // exact stem match
                if (stem.equals(normalizedTitle)) return file.getName();
                // title contains stem  (e.g. "avengers endgame" contains "avengers")
                if (normalizedTitle.contains(stem) || stem.contains(normalizedTitle)) return file.getName();
            }
        }

        return "";
    }

    private File resolvePosterFileFromName(String fileName) {
        if (fileName == null || fileName.isBlank()) return null;
        for (File dir : getPosterBaseDirectories()) {
            File candidate = new File(dir, fileName);
            if (candidate.exists() && candidate.isFile()) return candidate;
        }
        return null;
    }

    /**
     * Returns the list of candidate poster directories to search, from most-specific
     * (running from project root) to least-specific (running from a sub-folder or IDE).
     */
    public static List<File> getPosterBaseDirectories() {
        List<File> dirs = new ArrayList<>();

        // 1. Relative to working directory (paling umum saat run dari project root)
        addDir(dirs, new File("assets/poster"));
        addDir(dirs, new File("MovieRate/assets/poster"));

        // 2. Relative to JAR / class location
        try {
            File jarDir = new File(
                ReviewItem.class.getProtectionDomain()
                               .getCodeSource()
                               .getLocation()
                               .toURI()
            ).getParentFile();

            addDir(dirs, new File(jarDir, "assets/poster"));
            addDir(dirs, new File(jarDir, "../assets/poster"));
            addDir(dirs, new File(jarDir, "../../assets/poster"));
            addDir(dirs, new File(jarDir, "../MovieRate/assets/poster"));
        } catch (URISyntaxException | SecurityException ignored) {}

        // 3. Absolute fallback: cari dari semua drive root ke atas dari working dir
        File cwd = new File(System.getProperty("user.dir"));
        for (File dir = cwd; dir != null; dir = dir.getParentFile()) {
            addDir(dirs, new File(dir, "assets/poster"));
            addDir(dirs, new File(dir, "MovieRate/assets/poster"));
        }

        return dirs;
    }

    private static void addDir(List<File> dirs, File dir) {
        try {
            File canonical = dir.getCanonicalFile();
            if (!dirs.contains(canonical)) dirs.add(canonical);
        } catch (Exception ignored) {
            if (!dirs.contains(dir)) dirs.add(dir);
        }
    }

    private static String normalizeName(String value) {
        if (value == null) return "";
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "").trim();
    }

    private static String stripExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(0, dot) : fileName;
    }
}
