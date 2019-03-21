package app.com.example.vip.koranewsuserapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ScrollingActivityDetails extends AppCompatActivity {

    TextView title, content, time;
    ImageView imageView;
    FloatingActionButton fab;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = openOrCreateDatabase("koranews",MODE_PRIVATE,null);
        db.execSQL("create table if not exists favnews(title varchar , content varchar , imageFav blob , time varchar)");

        title = (TextView) findViewById(R.id.title_Details);
        content = (TextView) findViewById(R.id.content_Details);
        time = (TextView) findViewById(R.id.time_Details);
        imageView = (ImageView) findViewById(R.id.image_Details);
        Intent intent = getIntent();

        //String mtitle= intent.getStringExtra("title");
        //setTitle(mtitle); // to set title as the activity title

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        time.setText(intent.getStringExtra("time"));

        if (intent.hasExtra("image")){ //case it sent from ViewAll activity so it must has image
            Picasso.with(getApplicationContext()).load(intent.getStringExtra("image")).fit().centerCrop().into(imageView);
        }
        else{ // but in favourite it doesnt sent image as we cant transfer image throw intent (TooLarge) so we got it from database.
            Cursor c = db.rawQuery("select imageFav from favnews where title = '"+title.getText().toString()+"' and time = '"+time.getText().toString()+"'", null);
            if (c.moveToNext()){
                byte[] image = c.getBlob(0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(bitmap);
            }
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        changeFavIconButton(fab); // to change the icon if post is fav or not fav
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //first check if the post is already in favourites and we clicked then remove it and if not then add it!
                String mFavCheckQuery = "select title, time from favnews where title = '"+title.getText().toString()+"' and time = '"+time.getText().toString()+"'";
                Cursor c = db.rawQuery(mFavCheckQuery, null);
                if (c.moveToNext()){
                    db.execSQL("delete from favnews where title = '"+title.getText().toString()+"' and time = '"+time.getText().toString()+"'");
                    changeFavIconButton(fab);
                    Snackbar.make(view, "Removed From Favourites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    //add to favourites
                    insertIntoDatabase(title.getText().toString(), content.getText().toString(), imageView, time.getText().toString());
                    changeFavIconButton(fab);
                    Snackbar.make(view, "Added To Favourites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    private void changeFavIconButton(FloatingActionButton fab) {
        String mFavCheckQuery = "select title, time from favnews where title = '"+title.getText().toString()+"' and time = '"+time.getText().toString()+"'";
        Cursor c = db.rawQuery(mFavCheckQuery, null);
        if (c.moveToNext()){
            fab.setImageResource(R.drawable.star_checked);
        }
        else {
            fab.setImageResource(R.drawable.star_unchecked);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        if (item.getItemId() == R.id.action_favourite) {

            Intent intent = new Intent(ScrollingActivityDetails.this , FavouritesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //method to insert my data into database including encoding Image into ByteArray
    //convert bitmap to byteArray
    public void insertIntoDatabase(String title, String content, ImageView imageView, String time){

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable(); //get image as drawable
        Bitmap bitmap = drawable.getBitmap(); //put image in bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); // encode the image into ByteArray with Max quality 100
        byte[] mImageData = baos.toByteArray(); // add it to the bytearray so that we can save it into database as bolb datatype
        Log.e(">>>>>>>> BEFORE >>>>>>>> ", mImageData.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("imageFav" , mImageData);
        contentValues.put("time", time);
        db.insert("favnews",null, contentValues); //insert

    }

    @Override
    public void onRestart() //to reload activity so that if changes happened and we press back we can see it
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
