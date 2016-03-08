package application;

import java.util.ArrayList;

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

	public String getInf() {
		// Simply iterates through the array list of interfaces
		// and prints each out to the console
		String infList = "";
		System.out.println("Inf(s):");
		for (int i = 0; i < inf.size(); i++) {
			if(inf.size() - i == 1) {
				infList += inf.get(i);
			}else {
				infList += inf.get(i) + " , ";
			}
			
			System.out.println("\t" + inf.get(i));
		}
		return infList;
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
