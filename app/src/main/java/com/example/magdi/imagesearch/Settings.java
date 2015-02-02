package com.example.magdi.imagesearch;

import java.io.Serializable;

/**
 * Created by magdi on 1/28/15.
 */
public class Settings implements Serializable {
    private static final long serialVersionUID = 2081909167593883030L;
    public String siteFilter;
    public String color;
    public String size;
    public String type;

    public Settings(String color, String size, String type, String siteFilter) {
        super();
        this.siteFilter = siteFilter;
        this.color = color;
        this.size = size;
        this.type = type;
    }

    public Settings() {
    }

    public String getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQueryString() {
        String result = "";
        if (this.size != null) {
            result += ("&imgsz=" + this.size);
        }
        if (this.type != null) {
            result += ("&imgtype=" + this.type);
        }
        if (this.color != null) {
            result += ("&imgcolor=" + this.color);
        }
        if (this.siteFilter != null && this.siteFilter.toString().length() > 0) {
            result += ("&as_sitesearch=" + this.siteFilter);
        }
        return result;
    }
}



