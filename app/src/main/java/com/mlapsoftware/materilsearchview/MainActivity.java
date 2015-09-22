package com.mlapsoftware.materilsearchview;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mlapsoftware.searchviewwidget.SearchViewAdapter;
import com.mlapsoftware.searchviewwidget.SearchViewItem;
import com.mlapsoftware.searchviewwidget.SearchViewWidget;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SearchViewWidget searchViewWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        searchViewWidget = (SearchViewWidget) findViewById(R.id.search_view_widget);
        searchViewWidget.setOnQueryTextListener(new SearchViewWidget.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchViewWidget.setOnSearchViewListener(new SearchViewWidget.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });

        List<SearchViewItem> mArrayList = new ArrayList<>();
        SearchViewAdapter mSearchViewAdapter = new SearchViewAdapter(this, mArrayList, true); // false
        mSearchViewAdapter.setOnItemClickListener(new SearchViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView mText = (TextView) view.findViewById(R.id.textView_result);
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();

            }
        });
        searchViewWidget.setAdapter(mSearchViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchViewWidget.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchViewWidget.isSearchOpen()) {
            searchViewWidget.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchViewWidget.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchViewWidget.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


       /*
                mNavDrawerAdapter = new NavDrawerAdapter(this, mNavDrawerList);
        mNavDrawerAdapter.setOnItemClickListener(new NavDrawerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setSelected(true);
                selectItem(position);
                View mainContent = findViewById(R.id.drawerLayout);
                if (mainContent != null) {
                    mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
                }
            }
        });

*/ /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/