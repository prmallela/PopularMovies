package com.prmallela.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.prmallela.android.popularmovies.JsonKeys.OWN_ORIGIANL_TITLE;
import static com.prmallela.android.popularmovies.JsonKeys.OWN_OVERVIEW;
import static com.prmallela.android.popularmovies.JsonKeys.OWN_POSTER_PATH;
import static com.prmallela.android.popularmovies.JsonKeys.OWN_RELEASE_DATE;
import static com.prmallela.android.popularmovies.JsonKeys.OWN_REUSLTS;
import static com.prmallela.android.popularmovies.JsonKeys.OWN_VOTE_AVERAGE;
import static com.prmallela.android.popularmovies.TheMovieDBURL.APIKEY;
import static com.prmallela.android.popularmovies.TheMovieDBURL.BASEURL;
import static com.prmallela.android.popularmovies.TheMovieDBURL.IMAGE_BASEURL;


    public class FetchMovieData extends AsyncTask<String, Void, List<Movie>> {

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

    }
