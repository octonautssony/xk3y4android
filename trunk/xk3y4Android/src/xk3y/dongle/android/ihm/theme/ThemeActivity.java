package xk3y.dongle.android.ihm.theme;

import xk3y.dongle.android.R;
import xk3y.dongle.android.ihm.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ThemeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Création du menu inflater
		MenuInflater inflater = getMenuInflater();

		// On envoi a la variable menu le fichier xml parsé par l'inflater
		inflater.inflate(R.menu.option_menu, menu);

		return true;

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		// On récupère l'id de l'item et on le compare
		switch (item.getItemId()) {

		// S'il est égal à itemOptions
		case R.id.itemOptions:
			/*
			Intent myIntent = new Intent(this, SettingsActivity.class);
			this.startActivity(myIntent);
			*/
			Toast.makeText(this, "Options", Toast.LENGTH_SHORT).show();
			return true;

		// S'il est égal à itemQuitter
		case R.id.itemQuitter:

			// On ferme l'activité
			finish();
			return true;

		}

		return super.onMenuItemSelected(featureId, item);

	}
}
