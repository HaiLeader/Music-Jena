package vn.brine.haileader.exploratorysearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vn.brine.haileader.exploratorysearch.R;
import vn.brine.haileader.exploratorysearch.models.Movie;

/**
 * Created by HaiLeader on 7/12/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private Context mContext;
    private List<Movie> moviesList;

    public MovieAdapter(Context context, List<Movie> resultAdapters){
        this.mContext = context;
        this.moviesList = resultAdapters;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_row, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_profile));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView image;

        public MovieViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.titleTextView);
            image = (ImageView)view.findViewById(R.id.itemImageView);
        }
    }
}
