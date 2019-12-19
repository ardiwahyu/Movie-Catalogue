package com.example.favoriteapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Film> listFilm = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;
    private Activity activity;

    public Adapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Film> getListFilm(){
        return listFilm;
    }


    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<Film> item) {
        listFilm.clear();
        listFilm.addAll(item);
        notifyDataSetChanged();
    }

    public void addItem(final Film item) {
        this.listFilm.add(item);
        notifyItemInserted(listFilm.size() - 1);
    }

    public void removeItem(int position){
        this.listFilm.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listFilm.size());
    }

    public void clearData() {
        listFilm.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(listFilm.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listFilm.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvGenre, tvDate, tvRate;
        ImageView imgItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            imgItem = itemView.findViewById(R.id.img_item);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvRate = itemView.findViewById(R.id.tv_rate);
        }

        void bind(Film film, int position){
            Glide.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w185" + film.getPosterPath())
                    .apply(new RequestOptions().transform(new RoundedCorners(32)))
                    .placeholder(R.drawable.load).into(imgItem);
            tvJudul.setText(film.getOriginalTitle());
            tvGenre.setText(film.getOneGenre());
            tvGenre.setText(film.getGenre());
            tvDate.setText(film.getReleaseDate());
            tvRate.setText(String.valueOf(film.getVoteAverage()));
            itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(View view, int position) {
                    Log.d("position", listFilm.size() +" and "+ position);
                    onItemClickCallback.onItemClicked(listFilm.get(position), position);
//                    Animation animation = new AlphaAnimation(0.3f,1.0f);
//                    animation.setDuration(10);
//                    view.startAnimation(animation);
                }
            }));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Film data, int position);
    }
}
