package com.nhom2.andromeda.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom2.andromeda.R;
import com.nhom2.andromeda.database.FavDB;
import com.nhom2.andromeda.model.MovieItem;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<MovieItem> movieItems;
    private Context context;
    private FavDB favDB;

    public MovieAdapter(ArrayList<MovieItem> movieItems, Context context) {
        this.movieItems = movieItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favDB = new FavDB(context);
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        final MovieItem movieItem = movieItems.get(position);
        
        readCursorData(movieItem, holder);
        holder.imageView.setImageResource(movieItem.getImageResource());
        holder.titleTextView.setText(movieItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        Button favBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            favBtn = itemView.findViewById(R.id.favBtn);

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MovieItem movieItem = movieItems.get(position);

                    if (movieItem.getFavStatus().equals("0")) {
                        movieItem.setFavStatus("1");
                        favDB.insertIntoDatabase(movieItem.getTitle(), movieItem.getImageResource(),
                                movieItem.getKey_id(), movieItem.getFavStatus());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_red);
                    } else {
                        movieItem.setFavStatus("0");
                        favDB.remove_fav(movieItem.getKey_id());
                        favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow);
                    }
                }
            });
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorData(MovieItem movieItem, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data(movieItem.getKey_id());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                movieItem.setFavStatus(item_fav_status);

                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_red);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_shadow);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }
}
