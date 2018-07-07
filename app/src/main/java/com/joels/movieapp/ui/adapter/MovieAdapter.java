package com.joels.movieapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>{

    private Context mContext;
    private List<Movie> movieList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {

        ImageView mThumbnail;
        TextView mName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mThumbnail = itemView.findViewById(R.id.thumbnail);
            mName = itemView.findViewById(R.id.name);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }

    public MovieAdapter(Context context, List<Movie> movies){
        this.mContext = context;
        this.movieList = movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mName.setText(R.string.dummy_name);
        Glide.with(mContext).load("https://dg.imgix.net/at-home-in-wakanda-kdfkrhff-en/landscape/at-home-in-wakanda-kdfkrhff-d90bdb41656350831da4f01ab4eaa01f.jpg?ts=1518914815&;ixlib=rails-2.1.4&;w=700&;h=394&;dpr=2&;ch=Width%2CDPR&;auto=format%2Ccompress&;fit=min").into(myViewHolder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
