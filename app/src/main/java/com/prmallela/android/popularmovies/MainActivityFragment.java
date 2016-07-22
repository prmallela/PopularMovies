package com.prmallela.android.popularmovies;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.prmallela.android.popularmovies.Extras.connected;
import static com.prmallela.android.popularmovies.JsonKeys.*;
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
            FetchMovieData movieData = new FetchMovieData();
            movieData.execute(NOW_PLAYING);
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
                FetchMovieData movieData = new FetchMovieData();
                movieData.execute(POPULAR);
            }
            return true;
        }

        if (id == R.id.menu_now_playing) {
            if (connected(getContext())) {
                FetchMovieData movieData = new FetchMovieData();
                movieData.execute(NOW_PLAYING);
            }
            return true;
        }

        if (id == R.id.menu_sort_rating) {
            if (connected(getContext())) {
                FetchMovieData movieData = new FetchMovieData();
                movieData.execute(TOP_RATED);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {

        //Parsing Json String to Json object to Movie to Movie List
        private List<Movie> getMoviesDataFromJson(String movieJsonStr)
                throws JSONException {
            List<Movie> movieList = new ArrayList<>();
            //movieList.clear();

            JSONObject moviesData = new JSONObject(movieJsonStr);
            JSONArray movieArray = moviesData.getJSONArray(OWN_REUSLTS);

            for (int i = 0; i < movieArray.length(); i++) {
                Movie movie = new Movie(Parcel.obtain());
                JSONObject movieData = movieArray.getJSONObject(i);
                movie.setPoster_path(IMAGE_BASEURL.concat(movieData.getString(OWN_POSTER_PATH)));
                movie.setOriginal_title(movieData.getString(OWN_ORIGIANL_TITLE));
                movie.setOverview(movieData.getString(OWN_OVERVIEW));
                movie.setRelease_date(movieData.getString(OWN_RELEASE_DATE));
                movie.setVote_average(movieData.getString(OWN_VOTE_AVERAGE));
                movieList.add(movie);
            }
            return movieList;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String theMoviesdata = null;
            try {
                URL url = new URL(BASEURL.concat(params[0].concat(APIKEY)));
                //Creating request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                theMoviesdata = buffer.toString();
            } catch (IOException e) {
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                return getMoviesDataFromJson(theMoviesdata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<Movie> resultData) {
            if (resultData != null) {
                MoviesAdapter moviesAdapter = new MoviesAdapter(getActivity(), resultData);
                gridView.setAdapter(moviesAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Movie movie = resultData.get(i);
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("SMovie", movie);
                        startActivity(intent);

                    }
                });
            }
        }
    }

}
