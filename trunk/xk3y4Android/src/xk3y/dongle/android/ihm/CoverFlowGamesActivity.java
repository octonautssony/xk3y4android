/*
 * Copyright (C) 2010 Neil Davies
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
 * 
 * This code is base on the Android Gallery widget and was Created 
 * by Neil Davies neild001 'at' gmail dot com to be a Coverflow widget
 * 
 * @author Neil Davies
 */
package xk3y.dongle.android.ihm;

import java.util.ArrayList;
import java.util.List;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.CoverFlow;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CoverFlowGamesActivity extends Activity {
	
	private List<Iso> listGames = new ArrayList<Iso>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//dalvik.system.VMRuntime.getRuntime().setMinimumHeapSize(64 * 1024 * 1024);
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		
		listGames = ConfigUtils.getConfig().getListeGames();
		
		CoverFlow coverFlow;
		coverFlow = new CoverFlow(this);

		coverFlow.setAdapter(new ImageAdapter(this));

		ImageAdapter coverImageAdapter = new ImageAdapter(this);
		
		coverFlow.setAdapter(coverImageAdapter);

		coverFlow.setSpacing(0);
		coverFlow.setSelection(4, true);
		coverFlow.setAnimationDuration(1000);
		coverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView arg0, View v, int position,
					long rowID) {
				
				Iso iso = ConfigUtils.getConfig().getListeGames().get(position);
				ConfigUtils.getConfig().setSelectedGame(iso);
				
				// Open liste games window
				Intent myIntent = new Intent(CoverFlowGamesActivity.this, GameDetailsActivity.class);
				CoverFlowGamesActivity.this.startActivity(myIntent);
			}
		});
		
		setContentView(coverFlow);
		
		// Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);

	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return listGames.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final Iso iso = listGames.get(position);
			// Use this code if you want to load from resources
			ImageView i = new ImageView(mContext);
			i.setImageBitmap(iso.getCover());
			int width = iso.getCover().getWidth();
			int height = iso.getCover().getHeight();
			
			i.setLayoutParams(new CoverFlow.LayoutParams(width, height));
			i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			// Make sure we set anti-aliasing otherwise we get jaggies
			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			drawable.setAntiAlias(true);
			
			return i;

		}

		/**
		 * Returns the size (0.0f to 1.0f) of the views depending on the
		 * 'offset' to the center.
		 */
		public float getScale(boolean focused, int offset) {
			/* Formula: 1 / (2 ^ offset) */
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}

	}
}