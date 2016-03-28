package application;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	//make sure name is not use in either hub or vm
	public static boolean validateName(String name) {
		boolean valid = true;
		//check every hub
		for(Map.Entry<String, HUB> hubEntry : application.Data.hubMap.entrySet()) {
			HUB currentHub = hubEntry.getValue();
			if(currentHub.getName().toLowerCase().equals(name.trim().toLowerCase())) {
				valid = false;
			}
		}
		//check every vm
		for(Map.Entry<String, VM> vmEntry : application.Data.vmMap.entrySet()) {
			VM currentVM = vmEntry.getValue();
			if(currentVM.getName().toLowerCase().equals(name.trim().toLowerCase())) {
				valid = false;
			}
		}
		return valid;
	}
	//make sure ip is not taken also make sure its in the range of 255.255.255.255
	public static boolean validateIp(String ip) {
		boolean valid = true;
		
		//check to make sure the ip is in the range of 255.255.255.255
		Pattern ipPattern = Pattern.compile(application.Data.IpPattern);
		Matcher matcher = ipPattern.matcher(ip);
		
		if(matcher.find()) {
			//if the ip is at least with the range of 255.255.255.255
			//make sure it's not a hub subnet
			for(Map.Entry<String, HUB> hubEntry : application.Data.hubMap.entrySet()) {
				HUB currentHub = hubEntry.getValue();
				if(currentHub.getSubnet().equals(ip)) {
					valid = false;
				}
			}
			//make sure it's not a vm interface
			for(Map.Entry<String, VM> vmEntry : application.Data.vmMap.entrySet()) {
				VM currentVM = vmEntry.getValue();
				for(Map.Entry<String, String> infEntry: currentVM.getInterfaces().entrySet()) {
					String currentIp = infEntry.getValue();
					if(currentIp.equals(ip)) {
						valid = false;
					}
				}
			}
		}else {
			valid = false;
		}
		return valid;
	}
	//make sure netmask is in the range of 255.255.255.255
	public static boolean validateNetmask(String netmask) {
		boolean valid = true;
		
		Pattern ipPattern = Pattern.compile(application.Data.IpPattern);
		Matcher matcher = ipPattern.matcher(netmask);
		
		if(!matcher.find()) {
			valid = false;
		}
		return valid;
	}
	//make sure ver is a double
	public static boolean validateVer(String ver) {
		boolean valid = true;
		try {
			Double.parseDouble(ver);
			System.out.println("Is correct");
		}catch(NumberFormatException e) {
			System.out.println("Not an integer");
			valid = false;
		}
		return valid;
	}
	//make sure src is valid
	public static boolean validateSrc(String src) {
		boolean valid;
		
		if(!src.equals("/srv/VMLibrary/JeOS")) {
			valid = false;
		}else {
			valid = true;
		}
		return true;
	}
}
