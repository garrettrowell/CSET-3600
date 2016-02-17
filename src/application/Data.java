package application;

import java.util.HashMap;

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
	static final String solPattern = "[(](\\w+[.]\\w+\\d+)\\s(\\w+\\d+[.]\\w+\\d+)[),]";
	// Hash maps containing vm's and hubs
	static HashMap<String, VM> vmMap = new HashMap<String, VM>();
	static HashMap<String, HUB> hubMap = new HashMap<String, HUB>();
	static HashMap<String, PARTIALSOLUTION> solMap = new HashMap<String, PARTIALSOLUTION>();
}
