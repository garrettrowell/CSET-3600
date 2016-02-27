package application;

import java.util.HashMap;
import java.util.Map;

public class VM {
	private String name;
	private String os;
	private String src;
	private Double ver;
	private HashMap<String, String> interfaces = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Double getVer() {
		return ver;
	}

	public void setVer(Double ver) {
		this.ver = ver;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getInterfaces() {
		// iterates through the interface hash map and prints each combination
		// of keys and values to the command line. Ex: eth0 192.168.0.1
		String ethoString = "";
		System.out.println("Interface(s):");
		for (Map.Entry<String, String> entry : interfaces.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			ethoString += key.toString() + ": " + value.toString() + "\n";
			System.out.println("\t" + key + " " + value);
		}
		return ethoString;
	}

	public void setInterfaces(String key, String value) {
		interfaces.put(key, value);
	}

}
