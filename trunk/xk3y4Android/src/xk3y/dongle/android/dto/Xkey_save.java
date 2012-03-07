package xk3y.dongle.android.dto;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="XKEY")
public class Xkey_save {
	
	@Element(name = "GUISTATE", required=false)
	private String guistate;
	@Element(name = "EMERGENCY", required=false)
	private String emergency;
	@Element(name = "TRAYSTATE", required=false)
	private String trayState;
	@ElementList(name = "GAMES", required=false)
	public List<Mount> listeMountDevices;
	
	// TODO Get the xkey infos list
	//@ElementList(inline=true)
	//private List<Item> about;
	
	public String getGuistate() {
		return guistate;
	}
	public void setGuistate(String guistate) {
		this.guistate = guistate;
	}
	public String getEmergency() {
		return emergency;
	}
	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public List<Mount> getListeMountDevices() {
		return listeMountDevices;
	}
	public void setListeMountDevices(List<Mount> listeMount) {
		this.listeMountDevices = listeMount;
	}
	public String getTrayState() {
		return trayState;
	}
	public void setTrayState(String trayState) {
		this.trayState = trayState;
	}

	
	
}
