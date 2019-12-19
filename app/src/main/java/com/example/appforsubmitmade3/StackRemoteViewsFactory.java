package com.example.appforsubmitmade3;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.appforsubmitmade3.db.MappingHelper;
import com.example.appforsubmitmade3.db.MyContentProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Film> widgetItem = new ArrayList<>();
    private final Context context;
    private Uri uri;
    private static final String DATABASE_TABLE = "film";

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate() {
        uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, DATABASE_TABLE);
    }

    @Override
    public void onDataSetChanged() {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        widgetItem.addAll(MappingHelper.mapCursorToArrayList(cursor));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetItem.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        try {
            Bitmap bitmap = Glide.with(context).asBitmap().load("https://image.tmdb.org/t/p/w500"+widgetItem.get(position).getPosterPath()).submit().get();
            remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MovieCatalogueWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.imageView, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
