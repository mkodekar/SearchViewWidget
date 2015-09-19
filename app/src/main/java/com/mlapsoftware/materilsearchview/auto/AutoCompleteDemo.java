package com.mlapsoftware.materilsearchview.auto;

//public class AutoCompleteDemo extends Activity {

// String[] language = {"Apple", "Lime", "Lemon", "Orange", "Strawberry", "Watermelon", "Blueberry", "Plum"};
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autocomplete);

        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(language);

        final View dropDown = findViewById(R.id.resultsDropDown);

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.results);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final ViewGroup.LayoutParams recyclerLayoutParams = recycler.getLayoutParams();
        recyclerLayoutParams.height = (int) (Math.min(3, adapter.getItemCount()) * getResources().getDimension(R.dimen.carbon_toolbarHeight));
        recycler.setLayoutParams(recyclerLayoutParams);

        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        search.setAdapter(T);
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dropDown.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });

        adapter.setOnAutoCompleteListener(new AutoCompleteTextView.OnAutoCompleteListener() {
            @Override
            public void onAutoComplete() {
                recyclerLayoutParams.height = (int) (Math.min(3, adapter.getItemCount()) * getResources().getDimension(R.dimen.carbon_toolbarHeight));
                recycler.setLayoutParams(recyclerLayoutParams);
                dropDown.setVisibility(View.VISIBLE);
            }
        });

        recycler.setAdapter(adapter);
        adapter.setOnHintClicked(new OnHintClicked() {

            @Override
            public void onHintClicked(String hint) {
                search.performCompletion();
                dropDown.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        View editText = getCurrentFocus();
        if (editText != null && editText instanceof AutoCompleteTextView) {
            editText.clearFocus();
            return;
        }
        super.onBackPressed();
    }*/
//}
