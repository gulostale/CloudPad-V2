package com.jatar_000.cloudpad.notes.android.activity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RemoteViews;

import com.jatar_000.cloudpad.notes.R;
import com.jatar_000.cloudpad.notes.android.widget.SingleNoteWidget;
import com.jatar_000.cloudpad.notes.model.Item;
import com.jatar_000.cloudpad.notes.model.ItemAdapter;
import com.jatar_000.cloudpad.notes.model.Note;
import com.jatar_000.cloudpad.notes.persistence.NoteSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration Activity to select a single note which should be displayed in the SingleNoteWidget
 * Created by Jatar on 24.05.16.
 */
public class SelectSingleNoteActivity extends AppCompatActivity implements ItemAdapter.NoteClickListener {

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private NoteSQLiteOpenHelper db = null;
    private RecyclerView listView = null;
    private ItemAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_select_single_note);
        // Display Data
        db = new NoteSQLiteOpenHelper(this);
        db.synchronizeWithServer();
        setListView(db.getNotes());


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    private void setListView(List<Note> noteList) {
        List<Item> itemList = new ArrayList<>();
        itemList.addAll(noteList);
        adapter = new ItemAdapter(itemList);
        listView = (RecyclerView) findViewById(R.id.select_single_note_list_view);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter.setNoteClickListener(this);
    }

    @Override
    public void onNoteClick(int position, View v) {
        final Context context = SelectSingleNoteActivity.this;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_single_note);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        SingleNoteWidget.updateAppWidget((Note) adapter.getItem(position), context, appWidgetManager, appWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public boolean onNoteLongClick(int position, View v) {
        return false;
    }
}
