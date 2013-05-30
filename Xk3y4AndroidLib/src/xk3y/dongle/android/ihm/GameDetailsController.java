package xk3y.dongle.android.ihm;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.acra.ErrorReporter;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.Xkey;
import xk3y.dongle.android.dto.XkeyResult;
import xk3y.dongle.android.exception.XkeyException;
import xk3y.dongle.android.utils.ConfigUtils;
import xk3y.dongle.android.utils.DialogBoxUtils;
import xk3y.dongle.android.utils.HttpServices;
import xk3y.dongle.android.utils.Xk3yParserUtils;
import xk3y.dongle.android.utils.XkeyGamesUtils;
import xk3y.dongle.android.youtube.YouTubeManager;
import xk3y.dongle.android.youtube.YouTubeVideo;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Listener for the main view
 * @author maloups
 *
 */
public class GameDetailsController implements OnClickListener {
	
	public String urlToListGames = "http://" + ConfigUtils.getConfig().getIpAdress() + "/data.xml";
	
	/** the view */
	private GameDetailsActivity view;
	
	
	private int detailMessage = R.string.not_connected;
	private ProgressDialog progressDialog;
	private static final int UPDATE_STARTED = 0;
	private static final int UPDATE_FINISHED = 1;
	private boolean SHOW_ERROR = false;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_STARTED:
				progressDialog = ProgressDialog.show(view, "",
						"Loading. Please wait...", true, false);
				break;
			case UPDATE_FINISHED:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				
				if (SHOW_ERROR) {
					new DialogBoxUtils(view,  detailMessage);
				}
				break;

			}
			
		};
	};
	
	private void sendMessage(int what){
        Message msg = Message.obtain();
        msg.what = what;
        handler.sendMessage(msg);
    }
	
	/** Contructor */
	public GameDetailsController (GameDetailsActivity maVue) {
		this.view = maVue;
	}

	/**
	 * Evenement r�lalis� lors d'un click sur un bouton
	 */
	public void onClick(View v) {
		if(v == view.getBtLaunchGame()) {
			ConfigUtils.getConfig().vivrate();
			new Thread(new Runnable() {
				@Override
				public void run() {
					sendMessage(UPDATE_STARTED);
					launchGame();
					sendMessage(UPDATE_FINISHED);
				}

			}).start();
			
		} else if (v == view.getBtBack()) {
			ConfigUtils.getConfig().vivrate();
			view.finish();
		} else if (v == view.getTextSummary()) {
			ConfigUtils.getConfig().vivrate();
			showSummary();
		} else if (v == view.getBtTrailer()) {
			ConfigUtils.getConfig().vivrate();
			showTrailer();
		}
		
		
	}

	/**
	 * Try to retrieve and launch game trailer from youtube
	 */
	private void showTrailer() {
		try {
			String youtubeUrl = view.getCurrentGame().getTrailer();
			// String videoId = "Fee5vbFLYM4";
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(youtubeUrl));
			view.startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Show the summary of the game
	 */
	private void showSummary() {
		new DialogBoxUtils(view, R.string.summary, view.getCurrentGame().getSummary());
	}

	/**
	 * Event to load games view
	 */
	private void launchGame() {
		try {
			XkeyResult result = XkeyGamesUtils.launchGame(view.getCurrentGame().getId());
			if (result.isShowError()){
				SHOW_ERROR = true;
				detailMessage = result.getMessageCode();
			}
		} catch (XkeyException e) {
			ErrorReporter.getInstance().putCustomData("\n\nDEBUG_MESSAGE", "\n" + e.getMessage());
			ErrorReporter.getInstance().handleException(e);
		}
			
		
		
	}

}
