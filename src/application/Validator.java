package application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	//make sure name is not use in either hub or vm
	public static boolean validateName(String name) {
		boolean valid = true;
		try {
			if(name.trim().isEmpty()) {
				valid = false;
			}else {
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
			}
		}catch(NullPointerException e) {
			System.out.println("Please enter a String");
			valid = false;
		}
		return valid;
	}
	//make sure ip is not taken also make sure its in the range of 255.255.255.255
	public static boolean validateIp(String ip) {
		boolean valid = true;
		try {
			//check to make sure the ip is in the range of 255.255.255.255
			Pattern ipPattern = Pattern.compile(application.Data.IpPattern);
			Matcher matcher = ipPattern.matcher(ip.trim());
			
			if(matcher.find()) {
				//if the ip is at least with the range of 255.255.255.255
				//make sure it's not a hub subnet
				for(Map.Entry<String, HUB> hubEntry : application.Data.hubMap.entrySet()) {
					HUB currentHub = hubEntry.getValue();
					if(currentHub.getSubnet().equals(ip.trim())) {
						valid = false;
					}
				}
				//make sure it's not a vm interface
				for(Map.Entry<String, VM> vmEntry : application.Data.vmMap.entrySet()) {
					VM currentVM = vmEntry.getValue();
					for(Map.Entry<String, String> infEntry: currentVM.getInterfaces().entrySet()) {
						String currentIp = infEntry.getValue();
						if(currentIp.equals(ip.trim())) {
							valid = false;
						}
					}
				}
			}else {
				valid = false;
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	//make sure netmask is in the range of 255.255.255.255
	public static boolean validateNetmask(String netmask) {
		boolean valid = true;
		try {
			Pattern ipPattern = Pattern.compile(application.Data.IpPattern);
			Matcher matcher = ipPattern.matcher(netmask.trim());
			
			if(!matcher.find()) {
				valid = false;
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	//make sure ver is a double
	public static boolean validateVer(String ver) {
		boolean valid = true;
		try {
			Double.parseDouble(ver.trim());
		}catch(NumberFormatException e) {
			System.out.println("Not an integer");
			valid = false;
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	//make sure src is valid
	public static boolean validateSrc(String src) {
		boolean valid;
		try {
			if(!src.trim().equals("/srv/VMLibrary/JeOS")) {
				valid = false;
			}else {
				valid = true;
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	//make sure Os field is not empty
	public static boolean validateOs(String os) {
		boolean valid = false;
		try {
			List<String> validOs = Arrays.asList("LINUX", "WINDOW", "UNIX");
			for(String item : validOs) {
				if(item.equals(os.trim())) {
					valid = true;
				}
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	//make sure vlan field is not empty
	public static boolean validateVlan(String vlan) {
		boolean valid = true;
		try {
			if(vlan.trim().isEmpty()) {
				valid = false;
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	
	public static boolean validateHubInf(String inf) {
		boolean valid = false;
		try {
			//check for it's format
			Pattern pat = Pattern.compile(Data.hubInfPattern);
			Matcher matcher = pat.matcher(inf.trim());
			if(matcher.find()) {
				//check the vm object appointed to the given inf name
				VM vmObject = Data.vmMap.get(matcher.group(1));
				if(vmObject.getInterfaces().containsKey(matcher.group(2))) {
					//the number of octets for hub interface to match vm ip 
					valid = true;
				}
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
	
	public static boolean validateSubnetting(String hubNetmask, String hubInf, String hubSubnet) {
		boolean valid = true;
		try {
			int ipClass = Data.getIPClass(hubNetmask);
			Pattern hubInfPat = Pattern.compile(Data.hubInfPattern);
			Matcher hubInfMatcher = hubInfPat.matcher(hubInf);
			
			Pattern ipPat = Pattern.compile(Data.IpPattern);
			if(hubInfMatcher.find()) {
				String vmInf = Data.vmMap.get(hubInfMatcher.group(1)).getInterfaces().get(hubInfMatcher.group(2));
				
				Matcher hubSubnetMatcher = ipPat.matcher(hubSubnet);
				Matcher vmInfMatcher = ipPat.matcher(vmInf);
				
				if(hubSubnetMatcher.find() && vmInfMatcher.find()) {
					for(int i = 1; i <= ipClass; i++) {
						if(!hubSubnetMatcher.group(i).equals(vmInfMatcher.group(i))) {
							valid = false;
						}
					}
				}
			}
		}catch(NullPointerException e) {
			valid = false;
		}
		return valid;
	}
}
