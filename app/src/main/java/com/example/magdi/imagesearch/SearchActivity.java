package com.example.magdi.imagesearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    public static final String SETTINGS_KEY = "settings";
    static String query = null;
    SearchView searchView;
    GridView gvResults;
    ArrayList<imageResultModel> imageResults;
    imageResultsAdapter aimageresult;
    Settings setting;
    String oquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpview();
        setting = new Settings();
        //create data source
        imageResults = new ArrayList<imageResultModel>();
        //attached to the source
        aimageresult = new imageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aimageresult);


        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
// Triggered only when new data needs to be appended to the list
// Add whatever code is needed to append new items to your AdapterView

                customLoadMoreDataFromApi(page);
                Log.d("QUERY_DONE", String.valueOf(page));
// or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

    }


    public void setUpview() {

        gvResults = (GridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                imageResultModel res = imageResults.get(position);
                i.putExtra("result", res);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        setupSearchView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchActivity.query = query;
                imageResults.clear();
                aimageresult.clear();
                searchWithOffset(query, 0);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void searchWithOffset(String query, int offset) {


        AsyncHttpClient client = new AsyncHttpClient();

        //String url = "https://ajax.googleapis.com/ajax/services/search/images?&v=1.0&q=" + Uri.encode(query) + "&rsz=8";
        oquery = (oquery == null ? "" : oquery);
        String url = "https://ajax.googleapis.com/ajax/services/search/images?" +
                "start=" + Integer.toString(offset * 8) + "&v=1.0&q=" + Uri.encode(query) + "&rsz=8" + oquery;
        Log.d("QUERY_DONE", query);

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray imageJsonResults = null;

                try {

                    imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();
                    //add == nodify
                    aimageresult.addAll(imageResultModel.fromJSONArray(imageJsonResults));
                    aimageresult.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DEBUG", imageResults.toString());
            }
        });

        Toast.makeText(getApplicationContext(), "Query is:" + "" + query, Toast.LENGTH_SHORT).show();


    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public void customLoadMoreDataFromApi(int offset) {
        searchWithOffset(SearchActivity.query, offset);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Intent productIntent = new Intent(this, SettingActivity.class);
            //Start Product Activity
            startActivity(productIntent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void onSettings(MenuItem mi) {
        Intent i = new Intent(this, SettingActivity.class);
        i.putExtra(SETTINGS_KEY, setting);
        startActivityForResult(i, 41);

    }

    private void setupSearchView(MenuItem searchItem) {
        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
        searchView.setOnQueryTextListener(this);
    }

    private boolean isAlwaysExpanded() {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 41 && resultCode == RESULT_OK) {
            String color = data.getStringExtra("color");
            String size = data.getStringExtra("size");
            String type = data.getStringExtra("type");
            String siteFilter = data.getStringExtra("site");

            if (size != null) {
                oquery += ("&imgsz=" + size);
            }
            if (type != null) {
                oquery += ("&imgtype=" + type);
            }
            if (color != null) {
                oquery += ("&imgcolor=" + color);
            }
            if (siteFilter != null && siteFilter.toString().length() > 0) {
                oquery += ("&as_sitesearch=" + siteFilter);
            }


            // setting = (Settings) data.getSerializableExtra(SETTINGS_KEY);
            Log.d("QUERY_RECEIVED", oquery);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
