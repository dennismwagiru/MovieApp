package com.joels.movieapp.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joels.movieapp.MovieApp;
import com.joels.movieapp.R;
import com.joels.movieapp.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<Movie> movieList = new ArrayList<>();
    private List<Movie> filtered = new ArrayList<>();

    final private MovieAdapterOnCLickHandler mClickHandler;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filtered = movieList;
                } else {
                    List<Movie> filteredList = new ArrayList<>();
                    for (Movie movie : movieList) {
                        if ((movie.title + movie.originalTitle + movie.tagLine).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(movie);
                        }
                    }
                    filtered = filteredList;
                    Log.e("Filtering", charString + String.valueOf(filtered));
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Movie>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MovieAdapterOnCLickHandler {
        void onClick(Movie movie);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mThumbnail;
        TextView mName;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mThumbnail = itemView.findViewById(R.id.thumbnail);
            mName = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie movie = filtered.get(position);
            mClickHandler.onClick(movie);
        }
    }

    public MovieAdapter(Context context, MovieAdapterOnCLickHandler mClickHandler, List<Movie> movies){
        this.mContext = context;
        this.mClickHandler = mClickHandler;
        this.movieList = movies;
        this.filtered = movies;
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
        myViewHolder.mName.setText(filtered.get(i).title);
        final String path = MovieApp.getImdbImagePath() + filtered.get(i).posterPath;
        Glide.with(mContext).load(path).into(myViewHolder.mThumbnail);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }


}
