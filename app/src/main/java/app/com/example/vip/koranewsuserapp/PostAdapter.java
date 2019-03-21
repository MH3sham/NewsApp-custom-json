package app.com.example.vip.koranewsuserapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vip on 29-Nov-17.
 * http://www.hendiware.com/%D8%A7%D9%84%D9%80-recyclerview-%D9%88%D9%85%D8%A7-%D9%88%D8%B1%D8%A7%D8%A1-%D8%A7%D9%84%D9%80-adapters-%D9%88-%D8%A7%D9%84%D9%80-models-%D8%A7%D9%84%D8%AC%D8%B2%D8%A1-%D8%A7%D9%84%D8%AB%D8%A7%D9%86/
 */
public class PostAdapter extends RecyclerView.Adapter <PostAdapter.PostViewHolder> {
    List<Post> mPostList;
    Context context;
    ViewAllPosts mainActivity; //for intent to move from mainactivity to Details
    SQLiteDatabase db;

    Post mPost = new Post();

    public PostAdapter(List<Post> mPostList, Context context, ViewAllPosts mainActivity){
        this.mPostList = mPostList;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypost,parent,false);
        PostViewHolder postViewHolder = new PostViewHolder(row);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = mPostList.get(position);
        holder.cardView.setTag(position);

        holder.title.setText(post.title);
        holder.content.setText(post.content);
        holder.time.setText(post.time);
        Picasso.with(context).load(post.urlToImg).fit().centerCrop().into(holder.imageView);
        //holder.اسم الفاريبول من كلاس الهولدر اللي تحت.setText(post.اسم الفاريبول في كلاس البوست نفسه)
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,content,time;
        ImageView imageView, favStarImageView;
        CardView cardView;
        public PostViewHolder(View itemView) {
            super(itemView);
            //id = (TextView) itemView.findViewById(R.id.idText);
            title = (TextView) itemView.findViewById(R.id.titleTV);
            content = (TextView) itemView.findViewById(R.id.contentTV);
            time = (TextView) itemView.findViewById(R.id.timeTV);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            cardView = (CardView) itemView.findViewById(R.id.mycardview);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();
            mPost = mPostList.get(position);
            Intent intent = new Intent(mainActivity, ScrollingActivityDetails.class);

            intent.putExtra("title", mPost.getTitle());
            intent.putExtra("content", mPost.getContent());
            intent.putExtra("time", mPost.getTime());
            intent.putExtra("image", mPost.getUrlToImg());

            //https://guides.codepath.com/android/shared-element-activity-transition
            // make imageFav view Transition Animation
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mainActivity, imageView, "myImagetrasition");

            mainActivity.startActivity(intent, options.toBundle());

        }
    }
}
