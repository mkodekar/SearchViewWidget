package com.mlapsoftware.materilsearchview.auto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mlapsoftware.searchviewwidget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> implements Filterable {

    String[] originalStrings;
    List<String> strings;
    private OnHintClicked onHintClicked;
    private AutoCompleteTextView.OnAutoCompleteListener onAutoCompleteListener;

    public AutoCompleteAdapter(String[] strings) {
        this.originalStrings = strings;
        this.strings = Arrays.asList(originalStrings);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.persistent_search_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.text.setText(strings.get(i));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHintClicked.onHintClicked(strings.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> values = new ArrayList<>();
                for (String s : originalStrings)
                    if (s.toLowerCase().startsWith(constraint.toString().toLowerCase()))
                        values.add(s);
                results.values = values;
                results.count = values.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //strings = (List<String>) results.values;
                notifyDataSetChanged();
                onAutoCompleteListener.onAutoComplete();
            }
        };
    }

    public void setOnHintClicked(OnHintClicked onHintClicked) {
        this.onHintClicked = onHintClicked;
    }

    public void setOnAutoCompleteListener(AutoCompleteTextView.OnAutoCompleteListener onAutoCompleteListener) {
        this.onAutoCompleteListener = onAutoCompleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            //text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
