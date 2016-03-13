package application;

import java.util.LinkedHashMap;

public class Data {
	// Regex used to parse various patterns of the config file
	static final String nodePattern = "(\\w+)\\s(\\w+)\\s\\{(?<=\\{)(.*?)(?=\\})";
	static final String osPattern = "os\\s?:\\s?(\\w+)";
	static final String verPattern = "ver\\s?:\\s?\"(\\d+(?:\\.?)\\d+)\"";
	static final String srcPattern = "src\\s?:\\s?\"((?:\\/\\w+)+)\"";
	static final String ethPattern = "(eth\\d)\\s?:\\s?\"((?:\\d{0,3}\\.?)+)\"";
	static final String subnetPattern = "subnet\\s?:\\s?\"((?:\\d{0,3}\\.?)+)\"";
	static final String netmaskPattern = "netmask\\s?:\\s?\"((?:\\d{0,3}\\.?)+)\"";
	static final String infPattern = "inf\\s?:\\s?(.*)";
	
	//Coordinates for node positions and size
	public static int nodeLength = 100;
	public static int nodeWidth = 100;
	public static int hubStartPosX = 50;
	public static int hubStartPosY = 50;
	public static int vmStartPosX = 200;
	public static int vmStartPosY = 50;
	// Hash maps containing vm's and hubs
	public static LinkedHashMap<String, VM> vmMap = new LinkedHashMap<String, VM>();
	public static LinkedHashMap<String, HUB> hubMap = new LinkedHashMap<String, HUB>();
}
