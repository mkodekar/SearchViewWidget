package com.mlapsoftware.searchviewwidget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;


public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ResultViewHolder> implements Filterable {

    public OnItemClickListener mItemClickListener;
    private List<SearchViewItem> mSearchList = new ArrayList<SearchViewItem>();
    private List<SearchViewItem> typeAheadData;
    private Context mContext;
    private boolean theme;

    public SearchViewAdapter(Context mContext, List<SearchViewItem> mSearchList, boolean theme) {
        this.mContext = mContext;
        this.mSearchList = mSearchList;
        this.theme = theme;
        typeAheadData = new ArrayList<>();
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "gps"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rg43g43g4gwghj gps"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rhfqwj gps ufuydui"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rfwqhj"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rhfgwe3j"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rg3hj"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rgh344hj"));
        typeAheadData.add(new SearchViewItem(R.drawable.ic_search_black_24dp, "rgageghj"));
    }

    /*private Drawable tint(int i, int theme)
    {
        Drawable dr = ContextCompat.getDrawable(mContext, i);
        dr.setColorFilter(ContextCompat.getColor(mContext, theme == 0 ? R.color.search_light_icon : R.color.search_dark_icon), PorterDuff.Mode.SRC_OVER);
        return dr;
    }*/

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {
                    List<SearchViewItem> searchData = new ArrayList<>();

                    for (SearchViewItem str : typeAheadData) {
                        if (str.get_text().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            searchData.add(str);
                        }
                    }

                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    mSearchList = (ArrayList<SearchViewItem>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.persistent_search_item, parent, false);
        return new ResultViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        SearchViewItem item = mSearchList.get(position);
        viewHolder.icon.setImageResource(item.get_icon());
        viewHolder.text.setText(item.get_text());
        if (theme) {
            //viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            viewHolder.icon.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
        } else {
            //viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            viewHolder.icon.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            viewHolder.text.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
        }
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView icon;//final
        public TextView text;

        public ResultViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.imageView_result);
            text = (TextView) view.findViewById(R.id.textView_result);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition()); //getAdapterPosition()
            }
        }
    }

}