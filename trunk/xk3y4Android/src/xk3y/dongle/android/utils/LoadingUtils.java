package xk3y.dongle.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import xk3y.dongle.android.R;
import xk3y.dongle.android.dto.GameInfo;
import xk3y.dongle.android.dto.Iso;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageButton;

public class LoadingUtils {

	public static final String COVER_FOLDER_PATH = 
			Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ "/Xk3y4Android/Cover/";
	public static final String GAME_FOLDER_PATH = 
			Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ "/Xk3y4Android/Games/";
	
	/**
	 * Load all the cover and resize
	 */
	/*
	public static void loadCoverGames() {
		try {
			File dir = new File (COVER_FOLDER_PATH);
			dir.mkdirs();
			
			List<Iso> listGames = ConfigUtils.getConfig().getListeGames();
			for (Iso game : listGames){
				loadGameCover(game);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	

	/**
	 * Load one cover and resize
	 */
	public static void loadGameInfo(Iso game) throws Exception {
		try {
			Iso gameFromSd = null;
			// Try to get info from sdcard
			if (ConfigUtils.getConfig().isCacheData()) {
				gameFromSd = loadGameFromSdCard(game);
			} else {
				LoadingUtils.removeDataCache();
			}
			
			if (gameFromSd == null) {
				// Try to get info from xml
				GameInfo gameInfo = loadGameFromXml(game);
				// if no info, try to load cover with png
				if (gameInfo == null) {
					loadGameCover(game);
				}
				
				if (ConfigUtils.getConfig().isCacheData()) {
					saveGameOnSdCard(game);
				}
			} else {
				game.setId(gameFromSd.getId());
				game.setTitle(gameFromSd.getTitle());
				game.setGender(gameFromSd.getGender());
				game.setSummary(gameFromSd.getSummary());
				game.setBanner(gameFromSd.getBanner());
				game.setCover(gameFromSd.getCover());
				game.setOriginalCover(gameFromSd.getOriginalCover());
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Load a game info from xml
	 * @param game a game
	 * @return info of the fame, null if not found
	 * @throws Exception 
	 */
	public static GameInfo loadGameFromXml(Iso game) throws Exception {
		GameInfo gameInfo = null;
		try {
			// Try to get xml Data Game
			String fileName = game.getId() + ".xml";
			String ip = ConfigUtils.getConfig().getIpAdress();
			String fileUrl = "http://" + ip + "/covers/" + fileName;

			// Get xml from http
			String xml = HttpServices.getInstance().getResponseFromUrl(fileUrl);

			// XML node keys
			String KEY_GAMEINFO = "gameinfo"; // parent node
			String KEY_TITLE = "title";
			String KEY_SUMMARY = "summary";
			String KEY_BANNER = "banner";
			String KEY_BOXART = "boxart";
			String KEY_INFO = "info";
			String KEY_GENRE = "infoitem";
			
			GameInfoParserUtils parser = new GameInfoParserUtils();
			Document doc = parser.getDomElement(xml); // getting DOM element
			 
			gameInfo = new GameInfo();
			
			NodeList nl = doc.getElementsByTagName(KEY_GAMEINFO);
			// looping through all item nodes <item>
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);
				
				gameInfo.setTitle(parser.getValue(e, KEY_TITLE));
				gameInfo.setSummary(parser.getValue(e, KEY_SUMMARY));
				gameInfo.setBanner(parser.getValue(e, KEY_BANNER));
				gameInfo.setBoxart(parser.getValue(e, KEY_BOXART));
				
		
				NodeList genre = e.getElementsByTagName(KEY_INFO);
				if (nl != null && genre != null) {
					for (int i2 = 0; i2 < nl.getLength(); i2++) {
						Element e2 = (Element) genre.item(i2);
						if (e2 != null) {
							gameInfo.setGenre(parser.getValue(e2, KEY_GENRE));
						}
					}
				}
			}
			
			// Convert base 64 text to Bitmap
			Bitmap cover = encodeImageFromBase64(gameInfo.getBoxart());
			if (gameInfo.getTitle() != null && !gameInfo.getTitle().equals("")
					&& !gameInfo.getTitle().equals("No Title")) {
				game.setTitle(gameInfo.getTitle());
			}
			
			if (gameInfo.getGenre() != null && !gameInfo.getGenre().equals("")) {
				game.setGender(gameInfo.getGenre());
			}
			
			if (gameInfo.getSummary() != null && !gameInfo.getSummary().equals("")) {
				game.setSummary(gameInfo.getSummary());
			}
			// If conver == null then no xml, no cover
			if (cover == null) {
				gameInfo = null;
			} else {
				addCoverToGame(game, resizeCover(cover));
			}
			
			Bitmap banner = encodeImageFromBase64(gameInfo.getBanner());
			// If banner == null then no xml, no banner
			if (banner == null) {
				game.setBanner(null);
			} else {
				game.setBanner(banner);
				//game.setBanner(banner);
			}
			
			
		} catch (Exception e) {
			gameInfo = null;
			//throw e;
		}
		return gameInfo;
	}
	
	/**
	 * Load one cover and resize from hhtp
	 */
	public static void loadGameCover(Iso game) throws Exception  {
		try {
			String imgName = game.getId() + ".jpg";
			String ip = ConfigUtils.getConfig().getIpAdress();
			String coverUrl = "http://" + ip + "/covers/" + imgName;

			// Get image from http
			Bitmap cover = HttpServices.getInstance().loadImage(coverUrl);
			
			// Set the Bitmap to the game
			addCoverToGame(game, resizeCover(cover));
			
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Load one cover and resize from hhtp
	 */
	public static void addCoverToGame(Iso game, Bitmap cover) throws Exception {
		try {
			// The bitmpa With title at top
			
			Bitmap coverWithTitle = addTextAtTopOfBitmap(
					cover, game.getTitle());
			
			//resizeCover.recycle();
			
			// Set the original cover
			game.setOriginalCover(cover);
			
			if (!ConfigUtils.getConfig().isLightTheme()) {
				// Set the cover with text and reflexion
				Bitmap coverWithReflextion = addReflextionToBitmap(coverWithTitle);
				game.setCover(coverWithReflextion);
				coverWithTitle.recycle();
			} else {
				// Set the cover with text without reflexion
				game.setCover(coverWithTitle);
			}

			System.gc();
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeCover(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		if (resizeCover == null) {
			resizeCover = ConfigUtils.getConfig().getDefaultCover();
		}
		try {
			// Resize the image
			int height = ConfigUtils.getConfig().getCoverHeight();
			float tmp = ((float)((float)height / (float)cover.getHeight())) * cover.getWidth();
			//int newWidth = (int) (tmp * 0.9);
			int newWidth = (int) tmp;
			ConfigUtils.getConfig().setCoverWidth(newWidth);
			
			resizeCover = Bitmap.createScaledBitmap(cover, newWidth, height, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeCoverForGameDetails(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		try {
			// Resize the image
			int height = (int) (ConfigUtils.getConfig().getCoverHeight() * 1.2);
			float tmp = ((float)((float)height / (float)cover.getHeight())) * cover.getWidth();
			//int newWidth = (int) (tmp * 0.9);
			int newWidth = (int) tmp;
			ConfigUtils.getConfig().setCoverWidth(newWidth);
			
			resizeCover = Bitmap.createScaledBitmap(cover, newWidth, height, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeCoverForList(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		try {
			// Resize the image
			int height = (int) (ConfigUtils.getConfig().getCoverHeight() * 0.4);
			float tmp = ((float)((float)height / (float)cover.getHeight())) * cover.getWidth();
			//int newWidth = (int) (tmp * 0.9);
			int newWidth = (int) tmp;
			ConfigUtils.getConfig().setCoverWidth(newWidth);
			
			resizeCover = Bitmap.createScaledBitmap(cover, newWidth, height, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Resize the cover
	 * @param cover the cover to resize
	 * @return the resized cover
	 * @throws Exception 
	 */
	public static Bitmap resizeBanner(Bitmap cover) throws Exception {
		Bitmap resizeCover = cover;
		try {
			// Resize the image
			int width  = ConfigUtils.getConfig().getScreenWidth();
			float tmp = ((float)((float)width / (float)cover.getWidth())) * cover.getHeight();
			//int newWidth = (int) (tmp * 0.9);
			int newHeight = (int) tmp;

			resizeCover = Bitmap.createScaledBitmap(cover, width, newHeight, true);

		} catch (Exception e) {
			throw e;
		}
		return resizeCover;
	}
	
	/**
	 * Add text at top of the image
	 * @param img the image
	 * @param text the text add
	 * @return image with text
	 * @throws Exception 
	 */
	public static Bitmap addTextAtTopOfBitmap(Bitmap img, String text) throws Exception{
		Bitmap bmOverlay = null;
		try {
			// create a mutable bitmap with the same size as the background image
			bmOverlay = Bitmap.createBitmap(img.getWidth(), img.getHeight() + 30, 
			    Bitmap.Config.ARGB_4444);
			// create a canvas on which to draw
			Canvas canvas = new Canvas(bmOverlay);
	
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(20);
	
			paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	
			// draw the text and the point
			canvas.drawText(text, 0, 20, paint);
			// overlay background
			canvas.drawBitmap(img, 0, 30, paint);
			
			canvas.drawPoint(30.0f, 50.0f, paint);

		} catch (Exception e) {
			throw e;
		}
		
		return bmOverlay;
	}

	/**
	 * Add text at top of the image
	 * 
	 * @param img
	 *            the image
	 * @param text
	 *            the text add
	 * @return image with text
	 * @throws Exception
	 */
	public static Bitmap addReflextionToBitmap(Bitmap img) throws Exception {
		Bitmap bitmapWithReflection = null;
		try {
			// The gap we want between the reflection and the original image
			final int reflectionGap = 4;
			// Get you bit map from drawable folder
			Bitmap originalImage = img;

			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			// This will not scale but will flip on the Y axis
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			// Create a Bitmap with the flip matix applied to it.
			// We only want the bottom half of the image
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);

			// Create a new bitmap with same width but taller to fit reflection
			bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 3), Config.ARGB_8888);

			// Create a new Canvas with the bitmap that's big enough for
			// the image plus gap plus reflection
			Canvas canvas = new Canvas(bitmapWithReflection);
			// Draw in the original image
			canvas.drawBitmap(originalImage, 0, 0, null);
			// Draw in the gap
			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);
			// Draw in the reflection
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			// Create a shader that is a linear gradient that covers the
			// reflection
			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0,
					originalImage.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			// Set the paint to use this shader (linear gradient)
			paint.setShader(shader);
			// Set the Transfer mode to be porter duff and destination in
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
			// Draw a rectangle using the paint with our linear gradient
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			reflectionImage.recycle();
		} catch (Exception e) {
			throw e;
		}

		return bitmapWithReflection;
	}
	
	/**
	 * Permet de d�coder une image encoder en Base64
	 * @param la chaine repr�senatant l'image
	 * @return l'image
	 * @throws Exception exception
	 */
	public static Bitmap encodeImageFromBase64(String encodedImage) throws Exception {
		Bitmap decodeImage = null;
		try {
			byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
			decodeImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
		} catch (Exception e) {
			decodeImage = null;
		}
		return decodeImage;
	}
	
	/**
	 * Permet d'encoder une image en Base 64
	 * @param path l'emplacement de l'image
	 * @return l'image sous forme de String
	 * @throws Exception exception
	 */
	public static String convertImageToBase64(Bitmap bm) throws Exception {
		String encodedImage = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b = baos.toByteArray(); 
			
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			throw e;
		}
		return encodedImage;
	}
	
	/**
	 * Save a game on sd card
	 * @param game a game
	 */
	public static void saveGameOnSdCard(Iso game) {
		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		
		try {
			String savePath = GAME_FOLDER_PATH + game.getId() + ".data";
			File dataGameFile = new File(savePath);
			if (!dataGameFile.exists()) {
				dataGameFile.createNewFile();
			}
			fos = new FileOutputStream(dataGameFile);
	
			os = new ObjectOutputStream(fos);
			os.writeObject(game);
			os.close();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Load a game from sd card
	 * @param game a game
	 */
	public static Iso loadGameFromSdCard(Iso game) {
		Iso loadGame = null;
		FileInputStream  fis = null;
		ObjectInputStream is = null;
		try {
			String savePath = GAME_FOLDER_PATH + game.getId() + ".data";
			File dataGameFile = new File(savePath);
			if (dataGameFile.exists()) {
				fis = new FileInputStream (new File(savePath));
		
				is = new ObjectInputStream(fis);
				loadGame = (Iso) is.readObject();
			}
		} catch (Exception e) {
			loadGame = null;
			e.getMessage();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return loadGame;
	}
	
	/**
	 * Remove data cache folder
	 * @throws Exception exception
	 */
	public static void removeDataCache() throws Exception {
		try {
			removeDataDir(GAME_FOLDER_PATH);
			removeDataDir(COVER_FOLDER_PATH);
		} catch (Exception e) {
			throw e;
		}
	} 
	
	/**
	 * Remove data cache folder
	 * @throws Exception exception
	 */
	public static void removeDataDir(String path) throws Exception {
		try {
			File dir = new File(path);
			if (dir.exists() && dir.isDirectory()) {
				String[] children = dir.list();
		        for (int i = 0; i < children.length; i++) {
		            new File(dir, children[i]).delete();
		        }
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} catch (Exception e) {
			throw e;
		}
	} 
	
	// Clear bitmap
	public static void clearBitmap(Bitmap bm) {
		bm.recycle();
		System.gc();

	}
	
}
