package com.example.fmoyader.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.fmoyader.popularmovies.R;

import java.util.List;

/**
 * Created by fmoyader on 23/4/17.
 */

public class MovieSpinnerAdapter extends ArrayAdapter implements SpinnerAdapter {

    Context context;
    int textViewResourceId;
    List<String> list;

    public MovieSpinnerAdapter(Context context, int textViewResourceId, List<String> list) {
        super(context, textViewResourceId, list);

        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.list = list;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.sorting_mode_dropdown_spinner, null);
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(list.get(position));

        if (position  == 0) {
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_up_white_24dp,
                    0);
        }
        else{
            textView.setHeight(100);
        }

        return convertView;
    }
}
