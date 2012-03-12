package xk3y.dongle.android.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import xk3y.dongle.android.utils.LoadingUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@Root(name="ISO")
public class Iso implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Element(name = "TITLE", required=false)
	private String title;
	@Element(name = "ID", required=false)
	private String id;
	
	private Bitmap banner;
	private Bitmap cover;
	private Bitmap originalCover;
	
	private String summary;
	private String gender;

	public byte[] imageByteArray;
	public byte[] bannerByteArray;
	/*
	private ByteBuffer dstOriginal;
		
	private void writeObject(ObjectOutputStream out) throws IOException
    {
		out.writeUTF(title);
	    out.writeUTF(id);
	    out.writeUTF(summary);
	    out.writeUTF(gender);

	    out.writeInt(originalCover.getRowBytes());
	    out.writeInt(originalCover.getHeight());
	    out.writeInt(originalCover.getWidth());
	    out.writeInt(originalCover.getConfig().ordinal());

	    final int bmOriginalSize = originalCover.getRowBytes() * originalCover.getHeight();
	    dstOriginal = ByteBuffer.allocate(bmOriginalSize);
	    dstOriginal.rewind();
	    originalCover.copyPixelsToBuffer(dstOriginal);
	    dstOriginal.flip();
	    out.write(dstOriginal.array(), 0, bmOriginalSize);

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
    	title = in.readUTF();
        id = in.readUTF();
        summary = in.readUTF();
        gender = in.readUTF();
        
        final int originalnbRowBytes = in.readInt();
        final int originalheight = in.readInt();
        final int originalwidth = in.readInt();
        final Bitmap.Config originalconfig = Bitmap.Config.values()[in.readInt()];

        final int originalbmSize = originalnbRowBytes * originalheight;
        dstOriginal = ByteBuffer.allocate(originalbmSize);
        dstOriginal.rewind();
        in.read(dstOriginal.array(), 0, originalbmSize);

        originalCover = Bitmap.createBitmap(originalwidth, originalheight, originalconfig);
        originalCover.copyPixelsFromBuffer(dstOriginal);

        try {
			LoadingUtils.addCoverToGame(this, originalCover);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    */
	
	/** Included for serialization - write this layer to the output stream. */
	private void writeObject(ObjectOutputStream out) throws IOException{
	    out.writeUTF(title);
	    out.writeUTF(id);
	    out.writeUTF(summary);
	    out.writeUTF(gender);
	 
	    String strBanner = " ";
		try {
			if (banner != null) {
				strBanner = LoadingUtils.convertImageToBase64(banner);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    out.writeUTF(strBanner);

	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    originalCover.compress(Bitmap.CompressFormat.PNG, 100, stream);
	    this.imageByteArray = stream.toByteArray();
	 
	    out.writeObject(this.imageByteArray);

	    /*
	    ByteArrayOutputStream bannerstream = new ByteArrayOutputStream();
	    banner.compress(Bitmap.CompressFormat.PNG, 100, bannerstream);
	    this.bannerByteArray = bannerstream.toByteArray();
	 
	    out.writeObject(this.bannerByteArray);
*/
	}
	 
	/** Included for serialization - read this object from the supplied input stream. */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
	    
	    title = in.readUTF();
	    id = in.readUTF();
        summary = in.readUTF();
        gender = in.readUTF();
	 
        
        String strBanner = in.readUTF();
		try {
			banner = LoadingUtils.encodeImageFromBase64(strBanner);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    this.imageByteArray = (byte[]) in.readObject();
	    originalCover = BitmapFactory.decodeByteArray(this.imageByteArray,
	                                               0, this.imageByteArray.length);

	
/*
	    this.bannerByteArray = (byte[]) in.readObject();
	    banner = BitmapFactory.decodeByteArray(this.bannerByteArray,
	                                               0, this.bannerByteArray.length);
	    */
	    try {
			LoadingUtils.addCoverToGame(this, originalCover);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Bitmap getCover() {
		return cover;
	}
	public void setCover(Bitmap cover) {
		this.cover = cover;
	}
	public Bitmap getOriginalCover() {
		return originalCover;
	}
	public void setOriginalCover(Bitmap originalCover) {
		this.originalCover = originalCover;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public Bitmap getBanner() {
		return banner;
	}

	public void setBanner(Bitmap banner) {
		this.banner = banner;
	}
	
	
	
	
}
