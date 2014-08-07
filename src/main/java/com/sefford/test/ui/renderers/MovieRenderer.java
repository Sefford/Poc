package com.sefford.test.ui.renderers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sefford.brender.interfaces.Renderer;
import com.sefford.test.R;
import com.sefford.test.core.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Renderer that handles a Movie layout for lists
 */
public class MovieRenderer implements Renderer<Movie> {

    private ImageView background;
    private TextView score;
    private TextView info;
    private TextView name;

    @Override
    public void mapViews(View view) {
        background = (ImageView) view.findViewById(R.id.iv_background);
        score = (TextView) view.findViewById(R.id.tv_score);
        info = (TextView) view.findViewById(R.id.tv_info);
        name = (TextView) view.findViewById(R.id.tv_name);
    }

    @Override
    public void hookUpListeners(View view, Movie movie) {
        // Do nothing
    }

    @Override
    public void render(Context context, Movie movie, int position, boolean first, boolean last) {
        Picasso.with(context).load(movie.getPosterPath())
                .into(background);
        score.setText(Double.toString(movie.getAverage()));
        info.setText(movie.getRelease());
        name.setText(movie.getTitle());
    }

    @Override
    public int getId() {
        return R.layout.listitem_release;
    }
}

