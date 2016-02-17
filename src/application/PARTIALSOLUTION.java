package application;

import java.util.HashMap;
import java.util.Map;

public class PARTIALSOLUTION {
	private HashMap<String, String> connections = new HashMap<String, String>();

	public void getConnections() {
		System.out.println("Connections(s):");
		for (Map.Entry<String, String> entry : connections.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("\t" + key + " " + value);
		}
	}

	public void setConnections(String key, String value) {
		connections.put(key, value);
	}
}
