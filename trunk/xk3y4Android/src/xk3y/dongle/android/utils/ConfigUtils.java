package xk3y.dongle.android.utils;

import java.io.Serializable;
import java.util.List;

import xk3y.dongle.android.dto.FullGameInfo;
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
    public static final String CACHE_DATA = "CACHE_DATA";
    public static final String AUTO_LOAD = "AUTO_LOAD";
    public static final String THEME = "THEME";
    public static final String NB_SPLIT = "NB_SPLIT";
    
    public static final int THEME_COVER_FLOW = 0;
    public static final int THEME_COVER_FLOW_LIGHT = 1;
    public static final int THEME_BANNER_LIST = 2;
    public static final int THEME_COVER_LIST = 3;
    
    public static final int NB_GAME_INCREMENT = 5;
    
	/** The Xkey IP adress */
	private String ipAdress;
	/** The default Cover */
	private Bitmap defaultCover;
	/** The default Banner */
	private Bitmap defaultBanner;
	/** The settings file */
	SharedPreferences preferences;
	/** The xkey object with list games and infos */
	private Xkey xkey;
	/** The list of games In all partitions*/
	private List<FullGameInfo> listeGames;
	/** The game selected by the user */
	private FullGameInfo selectedGame;
	/** The screen with */
	private int screenWidth;
	/** The screen with */
	private int screenHeight;
	/** The cover with */
	private int coverWidth;
	/** The cover height */
	private int coverHeight;
	/** Avoid to reload All Cover */
	private boolean alreadyLoad = false;
	/** Light theme without reflection */
	//private boolean lightTheme = false;
	/** Cache data to start more quicly */
	private boolean cacheData = true;
	/** The selected theme */
	private int theme;
	/** The selected nb split by page */
	private int nbSplit;
	/** Games auto loading */
	private boolean autoLoad = false;
	
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
		LoadingUtils.saveXkeyOnSdCard(xkey);
	}

	
	public List<FullGameInfo> getListeGames() {
		return listeGames;
	}

	public void setListeGames(List<FullGameInfo> listeGames) {
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

	
	public Bitmap getDefaultBanner() {
		return defaultBanner;
	}

	public void setDefaultBanner(Bitmap defaultBanner) {
		this.defaultBanner = defaultBanner;
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

	public FullGameInfo getSelectedGame() {
		return selectedGame;
	}

	public void setSelectedGame(FullGameInfo selectedGame) {
		this.selectedGame = selectedGame;
	}
	
	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
		savePreferences(THEME, String.valueOf(theme));
	}

	
	public int getNbSplit() {
		return nbSplit;
	}

	public void setNbSplit(int nbSplit) {
		this.nbSplit = nbSplit;
		savePreferences(NB_SPLIT, String.valueOf(nbSplit));
	}

	public boolean isAutoLoad() {
		return autoLoad;
	}

	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
		savePreferences(AUTO_LOAD, String.valueOf(autoLoad));
	}

	public boolean isCacheData() {
		return cacheData;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/** Switch theme, load the banner */
	public boolean loadBanner() {
		boolean res = false;
		if (getTheme() == THEME_BANNER_LIST) {
			res = true;
		}
		return res;
	}
	
	/** Switch theme, add title over cover */
	public boolean addCoverTitle() {
		boolean res = false;
		if (getTheme() == THEME_COVER_FLOW || getTheme() == THEME_COVER_FLOW_LIGHT) {
			res = true;
		}
		return res;
	}
	
	/**
	 * Get the number of pages to display
	 * @return the number of pages to display
	 */
	public int getNbPages() {
		int nbGameToLoad = getNbGamesToLoad();
		int nbPage = 1;
		if (nbSplit != 0) {
			nbPage = (ConfigUtils.getConfig().getListeGames().size() / nbGameToLoad) + 1;
		}
		return nbPage;
	}
	
	
	/**
	 * Get the number of games to load
	 * @return the number of games to load
	 */
	public int getNbGamesToLoad() {
		return nbSplit * NB_GAME_INCREMENT;
	}
	
	
	/*
	public void setCacheData(boolean cacheData) {
		this.cacheData = cacheData;
		savePreferences(CACHE_DATA, String.valueOf(cacheData));
	}
*/
	
	
}
