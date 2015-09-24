# SearchViewWidget

Changed project from https://github.com/krishnakapil/MaterialSeachView.
Big THANK YOU !

![Screenshot 1]
(https://github.com/OCman/SearchViewWidget/blob/master/images/image_1.png)    ![Screenshot 2]
(https://github.com/OCman/SearchViewWidget/blob/master/images/image_2.png)    ![Screenshot 3]
(https://github.com/OCman/SearchViewWidget/blob/master/images/image_3.png)    ![Screenshot 4]
(https://github.com/OCman/SearchViewWidget/blob/master/images/image_4.png)    

Add SearchViewWidget to your layout file

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".MainActivity">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?colorPrimary"
            // app:elevation="4dp"
            android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
            app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />

        <com.ocman.searchviewwidget.SearchViewWidget
            android:id="@+id/search_view_widget"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:visibility="visible"
            android:layout_alignParentLeft="true"
            android:layout_alignParentStart="true"
            android:layout_alignParentTop="true"/>

</RelativeLayout>
```

#Styling SearchViewWidget
```
  app:search_style="color"
  app:search_style="classic"
  
  app:search_theme="dark"
  app:search_theme="light"
  
  app:search_divider="true"
  app:search_divider="false"
```
