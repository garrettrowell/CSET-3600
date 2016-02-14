package application;

import java.util.HashMap;

public class Data {
	static String nodePattern = "(\\w+)\\s(\\w+)\\s\\{(?<=\\{)(.*?)(?=\\})";
	static String osPattern = "os\\s?:\\s?(\\w+)";
	static String verPattern = "ver\\s?:\\s?\"(\\d+(?:\\.?)\\d+)\"";
	static String srcPattern = "src\\s?:\\s?\"((?:\\/\\w+)+)\"";
	static String ethPattern = "(eth\\d)\\s?:\\s?\"((?:\\d{0,3}\\.?)+)\"";
	static String subnetPattern = "subnet\\s?:\\s?\"((?:\\d{0,3}\\.?)+)\"";
	static String netmaskPattern = "netmask\\s?:\\s?\"((?:\\d{0,3}\\.?)+)\"";
	static String infPattern = "inf\\s?:\\s?(.*)";
	static HashMap <String, VM> vmMap = new HashMap<String, VM>();
	static HashMap <String, HUB> hubMap = new HashMap<String, HUB>();
}
