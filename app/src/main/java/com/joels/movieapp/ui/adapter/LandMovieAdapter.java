package com.joels.movieapp.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class LandMovieAdapter extends RecyclerView.Adapter<LandMovieAdapter.MyViewHolder>{

    private Context mContext;
    private List<Movie> movieList = new ArrayList<>();

    final private MovieAdapterOnCLickHandler mClickHandler;

    public interface MovieAdapterOnCLickHandler {
        void onClick(Movie movie);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mThumbnail;
        TextView mName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mThumbnail = itemView.findViewById(R.id.thumbnail);
            mName = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = movieList.get(position);
            mClickHandler.onClick(movie);
        }
    }

    public LandMovieAdapter(Context context, MovieAdapterOnCLickHandler mClickHandler){
        this.mContext = context;
        this.mClickHandler = mClickHandler;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.land_movie_card, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mName.setText(movieList.get(i).title);
        Glide.with(mContext).load(movieList.get(i).posterPath).into(myViewHolder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
