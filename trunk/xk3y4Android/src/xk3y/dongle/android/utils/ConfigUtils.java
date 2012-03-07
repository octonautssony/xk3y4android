package xk3y.dongle.android.utils;

import java.io.Serializable;
import java.util.List;

import xk3y.dongle.android.dto.Iso;
import xk3y.dongle.android.dto.Xkey;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;



/**
 * Load settings app
 *
 * @author maloups
 *
 */
public final class ConfigUtils implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    public static final String IP_ADRESS = "IP_ADRESSS";
    public static final String LIGHT_THEME = "LIGHT_THEME";
    public static final String CACHE_DATA = "CACHE_DATA";
    
	/** The Xkey IP adress */
	private String ipAdress;
	/** The default Cover */
	private Bitmap defaultCover;
	/** The settings file */
	SharedPreferences preferences;
	/** The xkey object with list games and infos */
	private Xkey xkey;
	/** The list of games In all partitions*/
	private List<Iso> listeGames;
	/** The game selected by the user */
	private Iso selectedGame;
	/** The cover with */
	private int coverWidth;
	/** The cover height */
	private int coverHeight;
	/** Avoid to reload All Cover */
	private boolean alreadyLoad = false;
	/** Light theme without reflection */
	private boolean lightTheme = false;
	/** Cache data to start more quicly */
	private boolean cacheData = false;
	
	private static ConfigUtils instance;


	private ConfigUtils() {
		
	}
	
	public static ConfigUtils getConfig() {
		if (instance == null) {
			instance = new ConfigUtils();
		}
		return instance;
	}


	private void savePreferences(String key, String value) {
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	public Xkey getXkey() {
		return xkey;
	}

	public void setXkey(Xkey xkey) {
		this.xkey = xkey;
		listeGames = xkey.getListeGames();
	}

	
	public List<Iso> getListeGames() {
		return listeGames;
	}

	public void setListeGames(List<Iso> listeGames) {
		this.listeGames = listeGames;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
		savePreferences(IP_ADRESS, ipAdress);
	}

	public Bitmap getDefaultCover() {
		return defaultCover;
	}

	public void setDefaultCover(Bitmap defaultCover) {
		this.defaultCover = defaultCover;
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

	public int getCoverWidth() {
		return coverWidth;
	}

	public void setCoverWidth(int coverWidth) {
		this.coverWidth = coverWidth;
	}

	public int getCoverHeight() {
		return coverHeight;
	}

	public void setCoverHeight(int coverHeight) {
		this.coverHeight = coverHeight;
	}

	public boolean isAlreadyLoad() {
		return alreadyLoad;
	}

	public void setAlreadyLoad(boolean alreadyLoad) {
		this.alreadyLoad = alreadyLoad;
	}

	public Iso getSelectedGame() {
		return selectedGame;
	}

	public void setSelectedGame(Iso selectedGame) {
		this.selectedGame = selectedGame;
	}

	public boolean isLightTheme() {
		return lightTheme;
	}

	public void setLightTheme(boolean lightTheme) {
		this.lightTheme = lightTheme;
		savePreferences(LIGHT_THEME, String.valueOf(lightTheme));
	}

	public boolean isCacheData() {
		return cacheData;
	}

	public void setCacheData(boolean cacheData) {
		this.cacheData = cacheData;
		savePreferences(CACHE_DATA, String.valueOf(cacheData));
	}

	
	
}
