package kz.almas.comixread.classes;

import android.widget.ImageView;

public class ComicRecommendations {
    public String key, posterBase64, title;

    public int id;

    public ComicRecommendations() {

    }

    public ComicRecommendations(String key, String posterBase64, String title, int id) {
        this.key = key;
        this.posterBase64 = posterBase64;
        this.title = title;
        this.id = id;
    }
}
