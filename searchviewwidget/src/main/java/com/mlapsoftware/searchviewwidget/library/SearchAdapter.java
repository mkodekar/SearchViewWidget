package com.mlapsoftware.searchviewwidget.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlapsoftware.searchviewwidget.R;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ResultViewHolder> implements Filterable {

    private List<SearchItem> mSearchList = new ArrayList<>();
    private final List<SearchItem> mFilteredSearchList = new ArrayList<>();
    public OnItemClickListener mItemClickListener;


    public SearchAdapter(List<SearchItem> mSearchList) {
        this.mSearchList = mSearchList;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new SearchFilter(this, mSearchList);
    }

    @Override
    public ResultViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        final View sView = mInflater.inflate(R.layout.persistent_search_item, parent, false);
        return new ResultViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder viewHolder, int position) {
        SearchItem item = mSearchList.get(position);
        viewHolder.icon.setBackgroundResource(item.get_icon());
        viewHolder.text.setText(item.get_text());
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

        public final ImageView icon;
        public final TextView text;

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