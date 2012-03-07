package xk3y.dongle.android.dto;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

@Element(name="MOUNT")
public class Mount {
	
	@ElementList(inline=true, required=false)
	public List<Iso> listGames;

	public List<Iso> getListGames() {
		return listGames;
	}

	public void setListGames(List<Iso> listGames) {
		this.listGames = listGames;
	}


}
