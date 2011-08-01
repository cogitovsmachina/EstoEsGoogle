/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.androidtitlan.estoesgoogle.ui;

import org.androidtitlan.estoesgoogle.provider.ScheduleContract;
import org.androidtitlan.estoesgoogle.provider.ScheduleContract.Rooms;
import org.androidtitlan.estoesgoogle.util.AnalyticsUtils;
import org.androidtitlan.estoesgoogle.util.ParserUtils;

import org.androidtitlan.estoesgoogle.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Shows a {@link WebView} with a map of the conference venue.
 */
public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";

    /**
     * When specified, will automatically point the map to the requested room.
     */
    public static final String EXTRA_ROOM = "com.google.android.iosched.extra.ROOM";

    private static final String MAP_JSI_NAME = "MAP_CONTAINER";
    private static final String MAP_URL = "http://maps.google.com/maps?q=Hip%C3%B3dromo+de" +
    		"+Las+Americas,+Lomas+de+Sotelo,+Miguel+Hidalgo,+Ciudad+de+M%C3%A9xico," +
    		"+M%C3%A9xico&hl=es&ie=UTF8&ll=19.438246,-99.2224&spn=" +
    		"0.008944,0.016115&sll=37.0625,-95.677068&sspn=30.819956," +
    		"66.005859&geocode=FYmbKAEdUvwV-g&z=16";
    private static boolean CLEAR_CACHE_ON_LOAD = false;

    private WebView mWebView;
    private View mLoadingSpinner;
    private boolean mMapInitialized = false;

	private WebChromeClient mWebChromeClient;

	private WebViewClient mWebViewClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        AnalyticsUtils.getInstance(getActivity()).trackPageView("/Map");
        } 

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_webview_with_spinner, null);

        // For some reason, if we omit this, NoSaveStateFrameLayout thinks we are
        // FILL_PARENT / WRAP_CONTENT, making the progress bar stick to the top of the activity.
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));

        mLoadingSpinner = root.findViewById(R.id.loading_spinner);
        mWebView = (WebView) root.findViewById(R.id.webview);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);

        mWebView.post(new Runnable() {
            public void run() {
                // Initialize web view
                if (CLEAR_CACHE_ON_LOAD) {
                    mWebView.clearCache(true);
                }

                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
                mWebView.loadUrl(MAP_URL);
            }
        });

        return root;
    }

  

}
