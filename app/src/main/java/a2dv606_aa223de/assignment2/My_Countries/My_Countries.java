package a2dv606_aa223de.assignment2.My_Countries;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import a2dv606_aa223de.assignment2.R;

import static android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL;

public class My_Countries extends Activity implements CalendarProviderClient {

    private SimpleCursorAdapter adapter;
    private int currentCountryId;
    private String sortOrder = "sort_order";
    ListView listView;
    TextView yView, cView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__countries);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getMyCountriesCalendarId();

         listView = (ListView) findViewById(R.id.listview_contries);
        adapter = new MyCountriesCursorAdapter(this, R.layout.country_row_view, null,
                EVENTS_LIST_PROJECTION, new int[]{R.id.year_id}, 0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER_MANAGER_ID, null, this);
        getPref();

    }

     public void getUIPrefs(){
      SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
      boolean fontSize = prefs.getBoolean("font_size", false);
      String foregroundColor = prefs.getString("key_foreground_color", "#000000");
      String backgroundColor = prefs.getString("key_background_color","#FFFFFF");

      if(fontSize){
          String s =Float.toString(getResources().getDimension(R.dimen.text_size_big));
          cView.setTextSize(getResources().getDimension(R.dimen.text_size_big));
          yView.setTextSize(getResources().getDimension(R.dimen.text_size_big));
      }
         else{
          String s =Float.toString(getResources().getDimension(R.dimen.text_size_normal));
          cView.setTextSize(getResources().getDimension(R.dimen.text_size_normal));
          yView.setTextSize(getResources().getDimension(R.dimen.text_size_normal));
      }

         cView.setTextColor(Color.parseColor(foregroundColor));
         yView.setTextColor(Color.parseColor(foregroundColor));
         listView.setBackgroundColor(Color.parseColor(backgroundColor));

  }

    @Override
    public void onResume() {  // Activity comes in the foreground
        super.onResume();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_countries, menu);
        return true;
    }
    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        boolean fontSize = prefs.getBoolean("font_size", false);
        String foregroundColor = prefs.getString("key_foreground_color", "#000000");
        String backgroundColor = prefs.getString("key_background_color","#FFFFFF");
        SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("font_size", fontSize);
            edit.putString("key_foreground_color",foregroundColor);
            edit.putString("key_background_color",backgroundColor);
            edit.commit();




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String order = "";
        int id = item.getItemId();
        if (id == R.id.action_add_country) {
            Intent intent = new Intent(this, Add_Country.class);
            this.startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.sort_by_country_desc) {
            order = CalendarContract.Events.TITLE + " DESC";
            setPref(order);

        } else if (id == R.id.sort_by_country_ASC) {
            order = CalendarContract.Events.TITLE + " ASC";
            setPref(order);

        } else if (id == R.id.sort_by_year_desc) {
            order = CalendarContract.Events.DTSTART + " DESC";
            setPref(order);

        } else if (id == R.id.sort_by_year_asc) {

            order = CalendarContract.Events.DTSTART + " ASC";
            setPref(order);
        }
        else if(id ==R.id.settings){
            startActivity(new Intent(this, MyCountryPreferenceActivity.class));
            return(true);
        }
        return super.onOptionsItemSelected(item);}

      private void setPref(String order){

          SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
          SharedPreferences.Editor editor = prefs.edit();
          editor.putString("sort", order);
          editor.commit();
          Bundle bundle = new Bundle();
          bundle.putString(sortOrder, order);
          getLoaderManager().restartLoader(LOADER_MANAGER_ID, bundle, this);
      }
    private void getPref(){
        Bundle bundle = new Bundle();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String order = prefs.getString("sort", "");
        if (order != null) {

            bundle.putString(sortOrder, order);
            getLoaderManager().restartLoader(LOADER_MANAGER_ID, bundle, this);}

    }


    @Override
    public void addNewEvent(int year, String country) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, CalendarUtils.getEventStart(year));
        contentValues.put(CalendarContract.Events.DTEND, CalendarUtils.getEventEnd(year));
        contentValues.put(CalendarContract.Events.TITLE, country);
        contentValues.put(CalendarContract.Events.CALENDAR_ID, getMyCountriesCalendarId());
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, CalendarUtils.getTimeZoneId());
        getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);
        ContentResolver contentResolver= getContentResolver();
        contentResolver.insert(EVENTS_LIST_URI, contentValues);
    }


    @Override
    public void updateEvent(int eventId, int year, String country) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.TITLE, country);
        contentValues.put(CalendarContract.Events.DTSTART, CalendarUtils.getEventStart(year));
        contentValues.put(CalendarContract.Events.DTEND, CalendarUtils.getEventEnd(year));
        Uri updateUri = ContentUris.withAppendedId(EVENTS_LIST_URI, eventId);
        ContentResolver contentResolver = getContentResolver();
        contentResolver.update(updateUri, contentValues, null, null);
        getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);

    }

    @Override
    public void deleteEvent(int eventId) {
        Uri uri = ContentUris.withAppendedId(EVENTS_LIST_URI, eventId);
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(uri, null, null);
        getLoaderManager().restartLoader(LOADER_MANAGER_ID, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (args == null)
            return new CursorLoader(this, EVENTS_LIST_URI, EVENTS_LIST_PROJECTION, null, null, null);
        else
            return new CursorLoader(this, EVENTS_LIST_URI, EVENTS_LIST_PROJECTION, null, null,args.getString(sortOrder));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_MANAGER_ID:
                adapter.swapCursor(data);
                break;


        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_MANAGER_ID:
                adapter.swapCursor(null);
                break;

        }

    }

    private static Uri asSyncAdapter(Uri uri, String account, String accountType) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build();
    }

    public long getMyCountriesCalendarId()  {
        long id =-1;

        Cursor cursor =  getContentResolver().query(CALENDARS_LIST_URI, CALENDARS_LIST_PROJECTION, CALENDARS_LIST_SELECTION,
                CALENDARS_LIST_SELECTION_ARGS, null);
        if(!cursor.moveToFirst()){
            Uri calendarUri = asSyncAdapter(CALENDARS_LIST_URI, ACCOUNT_TITLE, CalendarContract.ACCOUNT_TYPE_LOCAL);
                             getContentResolver().insert( calendarUri, createNewCalender());
        }else {
            id =  cursor.getLong(PROJ_CALENDARS_LIST_ID_INDEX);
        }
        return id;
    }
    private ContentValues createNewCalender(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_TITLE);
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        contentValues.put(CalendarContract.Calendars.NAME, CALENDAR_TITLE);
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_TITLE);
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_TITLE);
        contentValues.put(CalendarContract.Calendars.VISIBLE, 1);
        contentValues.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return contentValues;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result){
        String country = "";
        int year = 0;

        if(resultCode == RESULT_OK){
            country = result.getStringExtra("COUNTRY");
            year = result.getIntExtra("YEAR", 0);

            switch (requestCode){
                case 0:
                    addNewEvent(year, country);
                   getPref();
                    break;
                case 1:
                    updateEvent(currentCountryId, year, country);
                  getPref();
                    break;
            }
        }
    }

    class  MyCountriesCursorAdapter extends SimpleCursorAdapter {

        public  MyCountriesCursorAdapter(Context context,int layout, Cursor c,String[] from,int[] to, int flags) {
            super(context,layout,c,from,to, flags);

        }

        /* The bindView method is used to bind all data to a given view
        like setting the text on a TextView*/
        @Override
        public void bindView(final View view, final Context context, final Cursor cursor) {
            super.bindView(view, context, cursor);
            cView = (TextView) view.findViewById(R.id.country_id);
            yView = (TextView) view.findViewById(R.id.year_id);
            int yearValue = CalendarUtils.getEventYear(cursor.getLong(CalendarProviderClient.PROJ_EVENTS_LIST_DTSTART_INDEX));
            String countryValue = cursor.getString(CalendarProviderClient.PROJ_EVENTS_LIST_TITLE_INDEX);
            final int id  = cursor.getInt(CalendarProviderClient.PROJ_EVENTS_LIST_ID_INDEX);
            yView.setText(String.valueOf(yearValue));
            cView.setText(countryValue);
            getUIPrefs();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater
                            = (LayoutInflater)getBaseContext()
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup, null);
                    final PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,true);


                    Button btnCancel = (Button)popupView.findViewById(R.id.cancel_b);
                    btnCancel.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }});
                    Button btnUpdate = (Button)popupView.findViewById(R.id.update_b);
                    btnUpdate.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Update_Country.class);
                            currentCountryId=id;
                            startActivityForResult(intent, 1);
                            popupWindow.dismiss();
                        }});
                    Button btnDelete = (Button)popupView.findViewById(R.id.delete_b);
                    btnDelete.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            currentCountryId =id;
                            deleteEvent(currentCountryId);
                            popupWindow.dismiss();
                        }});

                    popupWindow.showAsDropDown(v,50,-30);


                }
            });

        }
    }
}

