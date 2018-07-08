package com.example.xyzreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.squareup.picasso.Picasso;

public class ArticlesAdapter  extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mLocalContext;

    public ArticlesAdapter(Cursor cursor, Context localContext) {
        mCursor = cursor;
        mLocalContext =localContext;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mLocalContext).inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mLocalContext, vh.thumbnailView, mLocalContext.getString(R.string.share_el));
                mLocalContext.startActivity(new Intent(Intent.ACTION_VIEW,
                        ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))), options.toBundle());
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        String subTitleText = DateUtils.getRelativeTimeSpanString(
                mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString()
                + " by "
                + mCursor.getString(ArticleLoader.Query.AUTHOR);

        holder.subtitleView.setText(subTitleText);

        final DynamicHeightNetworkImageView thumbnailView = holder.thumbnailView;
        final String thumbnailUrl = mCursor.getString(ArticleLoader.Query.THUMB_URL);
        final float aspectRatio = mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO);
        Picasso.with(mLocalContext).load(thumbnailUrl).into(thumbnailView);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public DynamicHeightNetworkImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }
}
