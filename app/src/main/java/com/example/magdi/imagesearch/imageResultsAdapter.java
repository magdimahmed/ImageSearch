package com.example.magdi.imagesearch;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.magdi.imagesearch.R.layout.item_image_results;

/**
 * Created by magdi on 1/28/15.
 */
public class imageResultsAdapter extends ArrayAdapter<imageResultModel> {

    public imageResultsAdapter(Context context, List<imageResultModel> images) {
        super(context, item_image_results, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        imageResultModel imageInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(item_image_results, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.imageView);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.textView);
        ivImage.setImageResource(0);
        tvTitle.setText(Html.fromHtml(imageInfo.getTitle()));
        Picasso.with(getContext()).load(imageInfo.getThumbUrl()).into(ivImage);
        return convertView;
    }
}
