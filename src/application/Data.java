package application;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	static final String ipPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	//Coordinates for node positions and size
	public static int nodeLength = 100;
	public static int nodeWidth = 100;
	public static int hubPosX = 50;
	public static int hubPosY = 50;
	public static int vmPosX = 200;
	public static int vmPosY = 50;
	public static int xPos = 50;
	public static int yPos = 50;
	// Hash maps containing vm's and hubs
	public static LinkedHashMap<String, VM> vmMap = new LinkedHashMap<String, VM>();
	public static LinkedHashMap<String, HUB> hubMap = new LinkedHashMap<String, HUB>();
	
	//this method is for determining the ip class type ex: 255.255.255.0 is C class or 3
	//this will be used to determine how many octet we have to match in the hub's subnet and the vm's interface
	public static int getIPClass(String hubNetmask) {
		int ipClass = 0;
		Pattern patt = Pattern.compile(ipPattern);
		Matcher match = patt.matcher(hubNetmask);
		
		if(match.find()) {
			for(int i = 1; i < 5; i++) {
				if(Integer.parseInt(match.group(i).toString()) > 0) {
					ipClass++;
				}
			}
		}else {
			System.out.println("Class could not be determine.");
		}
		
		return ipClass;
	}
	
	//Use to populate the con arraylist in every HUB object.
	//This method loops through every hub and vm in the two Hashmaps and
	//and test to see wither that hub is connect to this vm through ip.
	public static void sortConnections() {
		for(Map.Entry<String, HUB> hubEntry : hubMap.entrySet()) {
			Pattern patt = Pattern.compile(ipPattern);
			String currentHubName = hubEntry.getKey();
			HUB currentHubObject = hubMap.get(currentHubName);
			int ipClass = getIPClass(currentHubObject.getNetmask());
			
			for(Map.Entry<String, VM> vmEntry : vmMap.entrySet()) {
				String currentVMName = vmEntry.getKey();
				VM currentVmObject = vmMap.get(currentVMName);
				
				Matcher hubMatcher = patt.matcher(currentHubObject.getSubnet());
				for(Map.Entry<String, String> infEntry : currentVmObject.getInterfaceHashMap().entrySet()) {
					String currentInfName = infEntry.getKey();
					String currentInfIP = currentVmObject.getInterfaceHashMap().get(currentInfName);
					int octetMatchCounter = 0;
					Matcher vmMatcher = patt.matcher(currentInfIP);
					
					if(hubMatcher.find() && vmMatcher.find()) {
						for(int i = 1; i <= ipClass; i++) {
							if(hubMatcher.group(i).toString().equals(vmMatcher.group(i).toString())) {
								octetMatchCounter++;
							}
						}
					}
					
					//based off the ipClass, if the same amount of octets match then add it
					if(octetMatchCounter == ipClass) {
						currentHubObject.setCon(currentVMName);
					}
				}
			}
		}
	}
}
