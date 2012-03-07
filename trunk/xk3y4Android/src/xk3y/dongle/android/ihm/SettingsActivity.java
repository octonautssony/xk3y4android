package xk3y.dongle.android.ihm;

import xk3y.dongle.android.R;
import xk3y.dongle.android.utils.ConfigUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Main class
 * @author maloups
 *
 */
public class SettingsActivity extends Activity {
	
	private CheckBox ckbCacheData;
	private CheckBox ckbLightTheme;
	private CheckBox ckbClearCache;
	private EditText textIp;
	private Button btSave;
	private Button btBack;
	private SettingsController controller;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        
        
        // create window
        setContentView(R.layout.settings);

        // Icon of the app
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        
        // Get IHM Components
        getScreenComponent();
        
        // Add listener
        addController();
        
    }
    
	/**
     * Init ihm component
     */
    private void getScreenComponent() {
    	try {
	        // Button to launch the game
	    	btSave = (Button)findViewById(R.id.bt_save);
	    	// Button to back
	    	btBack = (Button)findViewById(R.id.bt_back);
	    	
	    	textIp = (EditText)findViewById(R.id.txt_ip);
	    	textIp.setText(ConfigUtils.getConfig().getIpAdress());
	    	
	    	ckbCacheData = (CheckBox)findViewById(R.id.ckb_cache_info);
	    	ckbCacheData.setChecked(ConfigUtils.getConfig().isCacheData());
	    	
	    	ckbLightTheme = (CheckBox)findViewById(R.id.ckb_light_theme);
	    	ckbLightTheme.setChecked(ConfigUtils.getConfig().isLightTheme());
	    	
	    	ckbClearCache = (CheckBox)findViewById(R.id.ckb_clear_cache);
	    	ckbClearCache.setChecked(false);
	    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

    /**
     * Init controller and listener
     */
    private void addController() {
        controller = new SettingsController(this);

        btSave.setOnClickListener(controller);
        btBack.setOnClickListener(controller);
    }

	public CheckBox getCkbCacheData() {
		return ckbCacheData;
	}

	public CheckBox getCkbLightTheme() {
		return ckbLightTheme;
	}

	public EditText getTextIp() {
		return textIp;
	}

	public Button getBtSave() {
		return btSave;
	}

	public Button getBtBack() {
		return btBack;
	}

	public CheckBox getCkbClearCache() {
		return ckbClearCache;
	}

    
    
    
    
}