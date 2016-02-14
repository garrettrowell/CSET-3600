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

	public void getInterfaces() {
		for (Map.Entry<String, String> entry : interfaces.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println(key + " " + value);
		}
	}

	public void setInterfaces(String key, String value) {
		interfaces.put(key, value);
	}

}
