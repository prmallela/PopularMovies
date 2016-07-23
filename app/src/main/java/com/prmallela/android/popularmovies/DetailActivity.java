package com.prmallela.android.popularmovies;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.prmallela.android.popularmovies.Extras.getYear;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Getting Selected Movie by user from MainActivityFragment
        Movie movie = getIntent().getParcelableExtra("SMovie");

        ImageView imageView = (ImageView) findViewById(R.id.dimageview);
        Picasso
                .with(this)
                .load(movie.getPoster_path())
                //   .fit()
                .into(imageView);

        TextView title = (TextView) findViewById(R.id.dtitle);
        title.setText(movie.getOriginal_title());

        TextView overview = (TextView) findViewById(R.id.doverview);
        overview.setText(movie.getOverview());

        TextView releaseDate = (TextView) findViewById(R.id.drelease);

        releaseDate.setText(getYear(movie.getRelease_date()));

        TextView rating = (TextView) findViewById(R.id.drating);
        rating.setText(movie.getVote_average());
    }

}
