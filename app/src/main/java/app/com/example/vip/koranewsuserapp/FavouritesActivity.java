package app.com.example.vip.koranewsuserapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String title ,content , time;
    List<Post> postsArrayList;
    RecyclerView recyclerView;
    TextView noDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_posts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noDataText = (TextView) findViewById(R.id.nodataTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        db=openOrCreateDatabase("koranews",MODE_PRIVATE,null);
        //get all my favs to display in Favourites Activity
        Cursor c = db.rawQuery("select * from favnews", null);
        postsArrayList = new ArrayList<>();

        while (c.moveToNext()){

            title = c.getString(0);
            content = c.getString(1);
            time = c.getString(3);

            byte[] image = c.getBlob(2); //third colm (index of 2)
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length); //decode the byteArray came from database as bolb datatype and put it back into bitmap.
            Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", String.valueOf(image.length));
            //imageView.setImageBitmap(bitmap);

            Post post = new Post(title,content,bitmap,time);
            postsArrayList.add(post);
        }
        c.close();


        //first will check if no data then show text says "no data available to preview" or something like that xD.
        //IN THREAD ONLY but now we r not in one >> this only work when deleting attribute android:visibility="gone" and that's bcoz it's in Thread!
        if (postsArrayList.isEmpty()) {
            noDataText.setText("You got no favourites, Start adding your favourites by pressing the favourite button!");
            recyclerView.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            PostAdapterFavourites postAdapterFavourites = new PostAdapterFavourites(postsArrayList, FavouritesActivity.this, FavouritesActivity.this);
            recyclerView.setAdapter(postAdapterFavourites);

            recyclerView.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRestart() //to reload activity so that if changes happened and we press back we can see it
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
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
            finish(); //kill it so if we go back it go to main posts activity not Fav again
            Intent intent = new Intent(FavouritesActivity.this , FavouritesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
