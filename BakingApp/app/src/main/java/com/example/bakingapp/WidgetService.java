package com.example.bakingapp;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    // THIS WHOLE CLASS HAS BEEN TAKEN FROM AN ONLINE VIDEO SOURCE TO IMPLEMENT LISTVIEW WIDGET

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory{
        private Context context;
        private int widgetId;
        private String[] x = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};

        WidgetItemFactory(Context context, Intent intent){
            this.context = context;
            this.widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {
            // CONNECT TO DATASOURCE - no heavy operations
        }

        @Override
        public void onDataSetChanged() {
            //
        }

        @Override
        public void onDestroy() {
            // CLOSE CONNECTION TO DATASOURCE
        }

        @Override
        public int getCount() {
            return x.length;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_listview_item);
            views.setTextViewText(R.id.listview_widget_item, x[i]);

            // THIS INTENT PORTION HAS BEEN TAKEN FROM AN ONLINE SOURCE
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("recipe_number", String.valueOf(i));
            views.setOnClickFillInIntent(R.id.listview_widget_item, fillInIntent);

            return views;
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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
