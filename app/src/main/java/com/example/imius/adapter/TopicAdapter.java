package com.example.imius.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.activity.PlaylistActivity;
import com.example.imius.data.DataLocalManager;
import com.example.imius.model.Singer;
import com.example.imius.model.TopicModel;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder>{
    private Context context;

    private List<TopicModel> topicModelList;
    public TopicAdapter(Context context){
        this.context = context;
    }
    public  TopicAdapter(Context context, ArrayList<TopicModel> arrayTopic){
        this.context = context;
        this.topicModelList = arrayTopic;
    }

    public List<TopicModel> getTopicModelList() {
        return topicModelList;
    }

    public void setTopicModelList(List<TopicModel> topicModelList) {
        this.topicModelList = topicModelList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TopicModel topicModel = topicModelList.get(position);

        if(topicModelList == null){
            return;
        }
        holder.tvNameTopic.setText(topicModel.getNameTopic());
        Picasso.get().load(topicModel.getImageTopic()).into(holder.imgTopic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaylistActivity(topicModel);
            }

        });
    }

    private void callPlaylistActivity(TopicModel topicModel) {
        DataLocalManager.setIdTopic(String.valueOf(topicModel.getIdTopic()));
        Intent intent = new Intent(context, PlaylistActivity.class);
        Bundle bundle = new Bundle();

        //      bundle.putString("idSinger", String.valueOf(singer.getIdSinger()));
        bundle.putString("nameTopic", topicModel.getNameTopic());
        bundle.putString("imageTopic", topicModel.getImageTopic());
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return topicModelList != null ? topicModelList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgTopic;
        TextView tvNameTopic;
        public ViewHolder(View itemView){
            super(itemView);
            imgTopic = itemView.findViewById(R.id.item_theme_iv_charts_theme);
            tvNameTopic = itemView.findViewById(R.id.item_theme_tv_theme_name);

        }

    }

}