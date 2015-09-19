package com.mlapsoftware.searchviewwidget.library;

import android.text.TextUtils;
import android.widget.Filter;

import com.mlapsoftware.searchviewwidget.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFilter extends Filter{

    private List<SearchItem> originalList;
    private List<SearchItem> filteredList;


    private UserFilter(SearchAdapter adapter, List<User> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = new ArrayList<>();
        this.filteredList = new ArrayList<>();
    }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            if (!TextUtils.isEmpty(constraint)) {
                List<SearchItem> searchData = new ArrayList<>();

                  /*  for (String str : typeAheadData) {
                        if (str.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            searchData.add(new Search(R.drawable.ic_search_black_24dp, str));
                        }
                    }*/

                results.values = searchData;
                results.count = searchData.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                mArrayList.add(new SearchItem(R.drawable.ic_search_black_24dp, results.values.toString()));
                //SearchAdapter.notifyDataSetChanged();
            }
        }
    };





}
