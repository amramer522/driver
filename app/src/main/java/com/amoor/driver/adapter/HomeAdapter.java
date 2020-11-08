package com.amoor.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoor.driver.R;
import com.amoor.driver.data.model.tweet.Tweet;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.hold> {
    private Context context;
    private List<Tweet> tweetList = new ArrayList<>();

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public hold onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new hold(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull hold hold, int i)
    {
        hold.admin_name.setText(tweetList.get(i).getName());
        hold.tweet_content.setText(tweetList.get(i).getTweet());
        hold.tweet_time.setText(tweetList.get(i).getDate());
        Glide.with(context).load("http://gtex.ddns.net/busbooking-system/data/include/uploads/" + tweetList.get(i).getImage()).into(hold.admin_photo);

    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public void setData(List<Tweet> tweetList) {
        this.tweetList = tweetList;
    }

    class hold extends RecyclerView.ViewHolder
    {
        TextView admin_name;
        TextView tweet_content;
        TextView tweet_time;
        CircleImageView admin_photo;


         hold(@NonNull View itemView)
        {
            super(itemView);
            admin_name = itemView.findViewById(R.id.Item_Home_Tv_User_Name);
            tweet_content = itemView.findViewById(R.id.Item_Home_Tv_Content_Text);
            tweet_time = itemView.findViewById(R.id.Item_Home_Tv_Time);
            admin_photo = itemView.findViewById(R.id.Item_Home_Civ_User_Image);
        }
    }
}
