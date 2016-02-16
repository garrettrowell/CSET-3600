package application;

import java.util.ArrayList;
import java.util.Collections;

public class HUB {
	private String subnet;
	private String netmask;
	private String name;
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

	public void getInf() {
		// Simply iterates through the array list of interfaces
		// and prints each out to the console
		System.out.println("Inf(s):");
		for (int i = 0; i < inf.size(); i++) {
			System.out.println("\t" + inf.get(i));
		}
	}

	public void setInf(String input) {
		inf.add(input);
	}
}
