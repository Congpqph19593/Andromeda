package com.nhom2.andromeda.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom2.andromeda.R;
import com.nhom2.andromeda.adapter.MovieAdapter;
import com.nhom2.andromeda.databinding.FragmentHomeBinding;
import com.nhom2.andromeda.model.MovieItem;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<MovieItem> movieItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MovieAdapter(movieItems, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        movieItems.add(new MovieItem(R.drawable.movie1, "Movie 1", "0", "0"));
        movieItems.add(new MovieItem(R.drawable.movie2, "Movie 2", "1", "0"));
        movieItems.add(new MovieItem(R.drawable.movie3, "Movie 3", "2", "0"));
        movieItems.add(new MovieItem(R.drawable.movie4, "Movie 4", "3", "0"));
        movieItems.add(new MovieItem(R.drawable.movie5, "Movie 5", "4", "0"));

        return root;
    }

}