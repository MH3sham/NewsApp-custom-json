package app.com.example.vip.koranewsuserapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// to support RecyclerView when it's Empty to set Text says no data available >> https://stackoverflow.com/a/28352183/7577004
public class ViewAllPosts extends AppCompatActivity {
    List<Post> postsArrayList;
    TextView noDataText;
    RecyclerView recyclerView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_posts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noDataText = (TextView) findViewById(R.id.nodataTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //initialize AndroidNetworking
        AndroidNetworking.initialize(getApplicationContext());

        //method that check if no internet then display AlertDialg says "No internet connection!" else run the thread and get my data.
        checkIfNoInternetDisplayDialogElseLoadMyData();


    }
    private void getDataAndroidFastNetwork() {
        AndroidNetworking.get("http://devyooda.000webhostapp.com/Koranewsjson.php")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {  //get json Object >> kol l json lly hyzharlak
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.e("READDDD", response.toString());

                        try{
                            JSONArray postsArray = response.getJSONArray("news");  // access array news lly gwaha kolo b2a
                            postsArrayList = new ArrayList<>();
                            for (int i = 0; i < postsArray.length(); i++) {
                                JSONObject currentObject = postsArray.getJSONObject(i);
                                String id = currentObject.getString("id");
                                String title = currentObject.getString("title");
                                String content = currentObject.getString("content");
                                String urlToImage = currentObject.getString("image_path");
                                String time = currentObject.getString("time"); // 2017-12-14 22:50:02

                                String mTime = mTimeConverterStringMonth(time); //to get month name and preview date only > 2017 Dec 14
                                Log.e(">>>>>>>>> TIME >>>>> ", mTime);

                                Post post = new Post(id, title, content, urlToImage, mTime);
                                postsArrayList.add(post);
                            }
                            GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2); // this will preview 2 cards side by side
                            recyclerView.setLayoutManager(mLayoutManager);

                            Collections.reverse(postsArrayList); //to preview the last post added in the database as the first one in the recyclerView.
                            PostAdapter postAdapter = new PostAdapter(postsArrayList, ViewAllPosts.this, ViewAllPosts.this);
                            recyclerView.setAdapter(postAdapter);

                        }
                        catch (Exception e){
                            e.getStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    private String mTimeConverterStringMonth(String time) {
        String monthString = "";
        String[] partsMainTime = time.split(" "); // 2017-12-14 22:50:02
        String dateOnly = partsMainTime[0]; // 2017-12-14
        String[] parts2MainTime = dateOnly.split("-");
        String monthInt = parts2MainTime[1]; //12
        switch (monthInt) {
            case "1":  monthString = "Jan";
                break;
            case "2":  monthString = "Feb";
                break;
            case "3":  monthString = "Mar";
                break;
            case "4":  monthString = "Apr";
                break;
            case "5":  monthString = "May";
                break;
            case "6":  monthString = "Jun";
                break;
            case "7":  monthString = "Jul";
                break;
            case "8":  monthString = "Aug";
                break;
            case "9":  monthString = "Sep";
                break;
            case "10": monthString = "Oct";
                break;
            case "11": monthString = "Nov";
                break;
            case "12": monthString = "Dec";
                break;
            default: monthString = "Invalid month";
                break;
        }
        String mTime = parts2MainTime[0] + " " + monthString + " " + parts2MainTime[2];
        return mTime;
    }

    private void checkIfNoInternetDisplayDialogElseLoadMyData() {
        //method that check if no internet then display AlertDialg says "No internet connection!" else run the thread and get my data.
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            recyclerView.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
            new AlertDialog.Builder(ViewAllPosts.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.internet_error))
                    .setIcon(R.drawable.exclamation_icon)
                    .setPositiveButton("OK", null).show();
        }else{
            noDataText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            getDataAndroidFastNetwork();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        if (item.getItemId() == R.id.action_favourite) {
            Intent intent = new Intent(ViewAllPosts.this , FavouritesActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.action_refresh) {
            View itemRefresh = toolbar.findViewById(R.id.action_refresh);
            itemRefresh.animate().rotationBy(360f).setDuration(1000); //add rotation animation to refresh bar button
            checkIfNoInternetDisplayDialogElseLoadMyData();
        }
        return super.onOptionsItemSelected(item);
    }


}
