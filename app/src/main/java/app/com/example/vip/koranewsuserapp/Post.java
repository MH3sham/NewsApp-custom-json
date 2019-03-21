package app.com.example.vip.koranewsuserapp;

import android.graphics.Bitmap;

/**
 * Created by Vip on 29-Nov-17.
 * http://www.hendiware.com/%D8%AA%D8%B9%D9%84%D9%85-%D8%A7%D9%84%D9%80-recyclerview-%D9%88%D9%85%D8%A7-%D9%88%D8%B1%D8%A7%D8%A1-%D8%A7%D9%84%D9%80-adapter/
 */
public class Post {
    String id;
    String title;
    String content;
    String urlToImg;
    String time;
    Bitmap imageFav;

    public Post(String id, String title, String content,String urlToImg, String time){
        this.id = id;
        this.title = title;
        this.content = content;
        this.urlToImg = urlToImg;
        this.time = time;
    }

    public Post(String title, String content,Bitmap imageFav, String time){
        this.title = title;
        this.content = content;
        this.imageFav = imageFav;
        this.time = time;
    }

    public Post() {

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrlToImg() {
        return urlToImg;
    }

    public String getTime() {
        return time;
    }

    public Bitmap getImageFav() {
        return imageFav;
    }


}
