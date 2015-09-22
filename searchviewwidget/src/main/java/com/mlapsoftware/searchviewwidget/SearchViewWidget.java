package com.mlapsoftware.searchviewwidget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


public class SearchViewWidget extends RelativeLayout implements Filter.FilterListener {

    public static final int REQUEST_VOICE = 9999;

    public static final int STYLE_CLASSIC = 0;
    public static final int STYLE_COLOR = 1;
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    private boolean mIsSearchOpen = false;
    private boolean mClearingFocus;
    private View mSearchLayout;
    private View mTintView;
    private View mSeparatorView;
    private RecyclerView mSuggestionsRecyclerView;
    private EditText mSearchEditText;
    private ImageView mBackImageView;
    private ImageView mVoiceImageView;
    private ImageView mEmptyImageView;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private OnQueryTextListener mOnQueryChangeListener;
    private SearchViewListener mSearchViewListener;
    private SearchViewAdapter mAdapter;
    private SavedState mSavedState;
    private Context mContext;
    private CardView mCardView;
    private int mStyle = 0;


    public SearchViewWidget(Context context) {
        this(context, null);
    }

    public SearchViewWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchViewWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initiateView();
        initStyle(attrs, defStyleAttr);
    }

    private void initiateView() {
        LayoutInflater.from(mContext).inflate((R.layout.persistent_search_widget), this, true);

        mSearchLayout = findViewById(R.id.search_layout);
        mCardView = (CardView) mSearchLayout.findViewById(R.id.cardView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mSuggestionsRecyclerView = (RecyclerView) mSearchLayout.findViewById(R.id.recyclerView);
        mSuggestionsRecyclerView.setLayoutManager(layoutManager);
        //mSuggestionsRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, null));
        mSuggestionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSuggestionsRecyclerView.setVisibility(GONE);

        mSearchEditText = (EditText) mSearchLayout.findViewById(R.id.editText_input);
        mBackImageView = (ImageView) mSearchLayout.findViewById(R.id.imageView_arrow_back);
        mVoiceImageView = (ImageView) mSearchLayout.findViewById(R.id.imageView_mic);
        mEmptyImageView = (ImageView) mSearchLayout.findViewById(R.id.imageView_clear);// CLEAR
        mTintView = mSearchLayout.findViewById(R.id.transparent_view);
        mSeparatorView = mSearchLayout.findViewById(R.id.separator);

        mSearchEditText.setOnClickListener(mOnClickListener);
        mBackImageView.setOnClickListener(mOnClickListener);
        mVoiceImageView.setOnClickListener(mOnClickListener);
        mEmptyImageView.setOnClickListener(mOnClickListener);
        mTintView.setOnClickListener(mOnClickListener);
        mSeparatorView.setVisibility(GONE);

        showVoice(true);
        initSearchView();
    }

    private void initStyle(AttributeSet attributeSet, int defStyleAttr) {
        TypedArray attr = mContext.obtainStyledAttributes(attributeSet, R.styleable.SearchViewWidget, defStyleAttr, 0);
        if (attr != null) {
            try {
                if (attr.hasValue(R.styleable.SearchViewWidget_search_style)) {
                    setStyle(attr.getInt(R.styleable.SearchViewWidget_search_style, 0));
                }
                if (attr.hasValue(R.styleable.SearchViewWidget_search_theme)) {
                    setTheme(attr.getInt(R.styleable.SearchViewWidget_search_theme, 0));
                }
            } finally {
                attr.recycle();
            }
        }
    }

    public void setStyle(int style) {
        if (style == STYLE_CLASSIC) {
            mBackImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            mVoiceImageView.setImageResource(R.drawable.ic_mic_black_24dp);
            mEmptyImageView.setImageResource(R.drawable.ic_clear_black_24dp);
        }
        if (style == STYLE_COLOR) {
            mBackImageView.setImageResource(R.drawable.ic_arrow_back_color_24dp);
            mVoiceImageView.setImageResource(R.drawable.ic_mic_color_24dp);
            mEmptyImageView.setImageResource(R.drawable.ic_clear_color_24dp);
        }
        mStyle = style;
    }

    public void setTheme(int theme) {
        if (theme == THEME_LIGHT) {
            if (mStyle == STYLE_CLASSIC) {
                mBackImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
                mVoiceImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
                mEmptyImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_light_icon));
            }
            mSeparatorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_separator));
            mSuggestionsRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mSearchEditText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_light_background));
            mSearchEditText.setTextColor(ContextCompat.getColor(mContext, R.color.search_light_text));
            mSearchEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.search_light_text_hint));
        }
        if (theme == THEME_DARK) {
            if (mStyle == STYLE_CLASSIC) {
                mBackImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                mVoiceImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
                mEmptyImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.search_dark_icon));
            }
            mSeparatorView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_separator));
            mSuggestionsRecyclerView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mSearchEditText.setBackgroundColor(ContextCompat.getColor(mContext, R.color.search_dark_background));
            mSearchEditText.setTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text));
            mSearchEditText.setHintTextColor(ContextCompat.getColor(mContext, R.color.search_dark_text_hint));
        }
    }

    private void initSearchView() {
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserQuery = s;
                startFilter(s);
                SearchViewWidget.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(mSearchEditText);
                    showSuggestions();
                }
            }
        });
    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null) {
            (mAdapter).getFilter().filter(s, SearchViewWidget.this);
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            if (v == mBackImageView) {
                closeSearch();
            } else if (v == mVoiceImageView) {
                onVoiceClicked();
            } else if (v == mEmptyImageView) {
                mSearchEditText.setText(null);
            } else if (v == mSearchEditText) {
                showSuggestions();
            } else if (v == mTintView) {
                closeSearch();
            }
        }
    };

    private void onVoiceClicked() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, REQUEST_VOICE);
        }
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //       intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      /*  try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }*/
        //Toast.makeText(mContext, "sta", Toast.LENGTH_SHORT).show();
    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchEditText.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyImageView.setVisibility(VISIBLE);
            showVoice(false);
        } else {
            mEmptyImageView.setVisibility(GONE);
            showVoice(true);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(newText, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(newText.toString());
        }
        mOldQueryText = newText.toString();
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchEditText.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                closeSearch();
                mSearchEditText.setText(null);
            }
        }
    }

    private boolean isVoiceAvailable() {
        PackageManager pm = getContext().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public void showSuggestions() {
        if (mAdapter != null && mAdapter.getItemCount() > 0 && mSuggestionsRecyclerView.getVisibility() == GONE) {
            mSuggestionsRecyclerView.setVisibility(VISIBLE);
            mSeparatorView.setVisibility(VISIBLE);
        }
    }

    public void showVoice(boolean show) {
        if (show && isVoiceAvailable()) {
            mVoiceImageView.setVisibility(VISIBLE);
        } else {
            mVoiceImageView.setVisibility(GONE);
        }
    }

    public void dismissSuggestions() {
        if (mSuggestionsRecyclerView.getVisibility() == VISIBLE) {
            mSuggestionsRecyclerView.setVisibility(GONE);
            mSeparatorView.setVisibility(GONE);
        }
    }

    public void setAdapter(SearchViewAdapter adapter) {
        mAdapter = adapter;
        mSuggestionsRecyclerView.setAdapter(adapter);
        startFilter(mSearchEditText.getText());
    }

    public void setQuery(CharSequence query, boolean submit) {
        mSearchEditText.setText(query);
        if (query != null) {
            mSearchEditText.setSelection(mSearchEditText.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    public void setMenuItem(MenuItem menuItem) {
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                return true;
            }
        });
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    public void showSearch() {
        showSearch(true);
    }

    public void showSearch(boolean animate) {
        if (isSearchOpen()) {
            return;
        }
        mSearchEditText.setText(null);
        mSearchEditText.requestFocus();

        if (animate) {
            AnimationUtil.fadeInView(mSearchLayout, AnimationUtil.ANIMATION_DURATION_MEDIUM, new AnimationUtil.AnimationListener() {
                @Override
                public boolean onAnimationStart(View view) {
                    return false;
                }

                @Override
                public boolean onAnimationEnd(View view) {
                    if (mSearchViewListener != null) {
                        mSearchViewListener.onSearchViewShown();
                    }
                    return false;
                }

                @Override
                public boolean onAnimationCancel(View view) {
                    return false;
                }
            });
        } else {
            mSearchLayout.setVisibility(VISIBLE);
            if (mSearchViewListener != null) {
                mSearchViewListener.onSearchViewShown();
            }
        }
        mIsSearchOpen = true;
    }

    public void closeSearch() {
        if (!isSearchOpen()) {
            return;//finish()
        }

        mSearchEditText.setText(null);
        dismissSuggestions();
        clearFocus();

        mSearchLayout.setVisibility(GONE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }
        mIsSearchOpen = false;

    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    public void setOnSearchViewListener(SearchViewListener listener) {
        mSearchViewListener = listener;
    }

    @Override
    public void onFilterComplete(int count) {
        if (count > 0) {
            showSuggestions();
        } else {
            dismissSuggestions();
        }
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchEditText.clearFocus();
        mClearingFocus = false;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        mSavedState = (SavedState) state;
        if (mSavedState.isSearchOpen) {
            showSearch(false);
            setQuery(mSavedState.query, false);
        }
        super.onRestoreInstanceState(mSavedState.getSuperState());
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (mClearingFocus) return false;
        if (!isFocusable()) return false;
        return mSearchEditText.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        mSavedState = new SavedState(superState);
        mSavedState.query = mUserQuery != null ? mUserQuery.toString() : null;
        mSavedState.isSearchOpen = this.mIsSearchOpen;
        return mSavedState;
    }

    private static class SavedState extends BaseSavedState {
        String query;
        boolean isSearchOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.query = in.readString();
            this.isSearchOpen = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
            out.writeInt(isSearchOpen ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public interface OnQueryTextListener {

        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public interface SearchViewListener {

        void onSearchViewShown();

        void onSearchViewClosed();
    }

    //private int mType; // boolean Boolean String CharSequence
       /* @Override
    public void setBackgroundColor(int color) {
        mSearchLayout.setBackgroundColor(color); String
    }*/

   /* @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/

}

/*
    public float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public float convertDpToPixel(Context context, float dp) {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / 160f);
    }

    public float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public float convertPixelsToDp(Context context, float px) {
        return px / (context.getResources().getDisplayMetrics().densityDpi / 160f);
    }
//   LayoutInflater.from(mContext).inflate((mType == 0 ? R.layout.expandable_widget
//          : R.layout.persistent_widget), this, true);*/


// mBackBtn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_arrow_back_color_24dp));
//setStyle(ContextCompat.getColor(mContext, R.color.cardview_light_background));R.color.cardview_light_background));