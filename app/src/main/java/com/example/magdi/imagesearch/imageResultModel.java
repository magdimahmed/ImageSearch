package com.example.magdi.imagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class imageResultModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fullUrl;
    private String thumbUrl;
    private String title;

    public imageResultModel(JSONObject json) {
        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            this.fullUrl = null;
            this.thumbUrl = null;
            this.title = null;
        }
    }

    public static ArrayList<imageResultModel> fromJSONArray(
            JSONArray imageJsonResults) {
        ArrayList<imageResultModel> results = new ArrayList<imageResultModel>();
        for (int x = 0; x < imageJsonResults.length(); x++) {
            try {
                results.add(new imageResultModel(imageJsonResults.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String toString() {
        return thumbUrl;
    }

    public String getTitle() {
        return title;
    }

}

