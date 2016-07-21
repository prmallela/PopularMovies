package com.prmallela.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MoviesAdapter extends ArrayAdapter<Movie> {

    public MoviesAdapter(Activity context, List<Movie> movieList) {
        super(context, 0, movieList);
    }

    //Image Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            View rootView = LayoutInflater.from(getContext()).inflate(R.layout.movies_imageview, null);
            convertView = rootView;
        }

        ImageView imageview = (ImageView) convertView.findViewById(R.id.movies_imageview);
        Picasso
                .with(getContext())
                .load(movie.getPoster_path())
                //   .fit()
                .into(imageview);
        return convertView;
    }
}


