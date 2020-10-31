package com.rvmarra17.listacompra.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rvmarra17.listacompra.R;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter {
    public ItemArrayAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Context CONTEXT = this.getContext();
        final LayoutInflater INFLATER = LayoutInflater.from(CONTEXT);
        final String ITEM = (String) this.getItem(position);

        if (view == null) {
            view = INFLATER.inflate(R.layout.layout_items, null);
        }

        final TextView lblNombre = view.findViewById(R.id.lblNombre);
        lblNombre.setText(ITEM);
        return view;
    }
}
