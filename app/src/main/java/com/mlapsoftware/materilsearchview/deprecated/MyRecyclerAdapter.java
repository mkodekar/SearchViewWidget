package com.mlapsoftware.materilsearchview.deprecated;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mlapsoftware.searchviewwidget.R;
import com.mlapsoftware.searchviewwidget.library.SearchItem;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private SearchItem[] itemsData;
    private OnRecyclerViewItemClickListener<SearchItem> itemClickListener;

    public MyRecyclerAdapter(SearchItem[] itemsData) {
        this.itemsData = itemsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.persistent_search_item, null);
        ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
        // Search item = itemsData[position];
        //  viewHolder.itemView.setTag(item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            //    txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            SearchItem itemData = (SearchItem) view.getTag();
            itemClickListener.onItemClick(view, itemData);
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<SearchItem> listener) {
        this.itemClickListener = listener;
    }
}