package app.com.example.vip.koranewsuserapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vip on 18-Dec-17.
 */
public class PostAdapterFavourites extends RecyclerView.Adapter <PostAdapterFavourites.PostViewHolder> {

    List<Post> mPostList;
    Context context;
    FavouritesActivity favouritesActivity; //for intent to move from mainactivity to Details
    Post mPost = new Post();

    public PostAdapterFavourites(List<Post> mPostList, Context context, FavouritesActivity favouritesActivity){
        this.mPostList = mPostList;
        this.context = context;
        this.favouritesActivity = favouritesActivity;
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
        holder.imageView.setImageBitmap(post.imageFav);
        //Picasso.with(context).load(getImageUri(favouritesActivity , post.imageFav)).fit().centerCrop().into(holder.imageView);
        //holder.اسم الفاريبول من كلاس الهولدر اللي تحت.setText(post.اسم الفاريبول في كلاس البوست نفسه)
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,content,time;
        ImageView imageView;
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
            Intent intent = new Intent(favouritesActivity, ScrollingActivityDetails.class);

            intent.putExtra("title", mPost.getTitle());
            intent.putExtra("content", mPost.getContent());
            intent.putExtra("time", mPost.getTime());
            //intent.putExtra("image", mPost.getImageFav()); //cant send it with intent it's TooLarge

            //https://guides.codepath.com/android/shared-element-activity-transition
            // make imageFav view Transition Animation
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(favouritesActivity, imageView, "myImagetrasition");

            favouritesActivity.startActivity(intent, options.toBundle());

        }


    }
}
