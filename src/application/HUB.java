package application;

import java.util.ArrayList;
import java.util.List;

public class HUB {
	private String subnet;
	private String netmask;
	private String name;
	private int posX, posY;
	private ArrayList<String> inf = new ArrayList<String>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubnet() {
		return subnet;
	}

	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public ArrayList<String> getInf() {
		return inf;
	}
	public List<String> compareInfs(List<String> list1, List<String> list2){
		
		return inf;
	}

	public void setInf(String input) {
		inf.add(input);
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}
}
