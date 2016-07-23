package com.prmallela.android.popularmovies;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.prmallela.android.popularmovies.Extras.connected;
import static com.prmallela.android.popularmovies.TheMovieDBURL.*;


public class MainActivityFragment extends Fragment {

    private GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_view);

        if (connected(getContext())) {
            fetchMovieData(NOW_PLAYING);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sort_popularity) {
            if (connected(getContext())) {
                fetchMovieData(POPULAR);
            }
            return true;
        }

        if (id == R.id.menu_now_playing) {
            if (connected(getContext())) {
                fetchMovieData(NOW_PLAYING);
            }
            return true;
        }

        if (id == R.id.menu_sort_rating) {
            if (connected(getContext())) {
                fetchMovieData(TOP_RATED);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchMovieData(String movieCat) {
        FetchMovieData movieData;
        movieData = new FetchMovieData();
        movieData.execute(movieCat);
        try {
            setAdapter(movieData.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setAdapter(final List<Movie> res) {
        MoviesAdapter moviesAdapter = new MoviesAdapter(getActivity(), res);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = res.get(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("SMovie", movie);
                startActivity(intent);

            }
        });
    }
}
