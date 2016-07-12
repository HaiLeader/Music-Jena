package vn.brine.haileader.musicjena.models;


/**
 * Created by HaiLeader on 7/12/2016.
 */
public class Movie {

    private String title;
    private String imageUrl;

    public Movie(String title, String imageUrl){
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImage(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
