package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
	public String singleMatcher(String regex, String input) {
		String output = null;
		Pattern patt = Pattern.compile(regex);
		Matcher match = patt.matcher(input);
		if (match.find()) {
			output = match.group(1);
		}
		return output;
	}

	public void ethMatcher(String vm, String regex, String input) {
		Pattern patt = Pattern.compile(regex);
		Matcher match = patt.matcher(input);
		while (true) {
			if (match.find()) {
				Data.vmMap.get(vm).setInterfaces(match.group(1), match.group(2));
			} else {
				break;
			}
		}
	}
	
	public void infMatcher(String hub, String regex, String input) {
		Pattern patt = Pattern.compile(regex);
		Matcher match = patt.matcher(input);
		if (match.find()) {
			ArrayList<String> aList= new ArrayList<String>(Arrays.asList(match.group(1).split(",\\s")));
			for(int i=0;i<aList.size();i++)
			{
				Data.hubMap.get(hub).setInf((String) aList.get(i));
			}
		}
	}

	public FileParser(File selectedFile) {
		String file = null;

		if (selectedFile != null) {
			file = application.FileOperations.readFile(selectedFile);
		}

		Pattern patt = Pattern.compile(Data.nodePattern, Pattern.DOTALL);
		Matcher match = patt.matcher(file);
		while (true) {
			if (match.find()) {
				if (match.group(1).equals("vm")) {
					Data.vmMap.put(match.group(2), new VM());
					Data.vmMap.get(match.group(2)).setName(match.group(2));
					Data.vmMap.get(match.group(2)).setOs(singleMatcher(Data.osPattern, match.group(3)));
					Data.vmMap.get(match.group(2))
							.setVer(Double.parseDouble(singleMatcher(Data.verPattern, match.group(3))));
					Data.vmMap.get(match.group(2)).setSrc(singleMatcher(Data.srcPattern, match.group(3)));
					ethMatcher(match.group(2), Data.ethPattern, match.group(3));
					
					// Prints out parsed data to cli for debugging purposes
					System.out.println("Found vm " + Data.vmMap.get(match.group(2)).getName());
					System.out.println(Data.vmMap.get(match.group(2)).getOs());
					System.out.println(Data.vmMap.get(match.group(2)).getVer());
					System.out.println(Data.vmMap.get(match.group(2)).getSrc());
					Data.vmMap.get(match.group(2)).getInterfaces();
				} else if (match.group(1).equals("hub")) {
					Data.hubMap.put(match.group(2), new HUB());
					Data.hubMap.get(match.group(2)).setName(match.group(2));
					Data.hubMap.get(match.group(2)).setSubnet(singleMatcher(Data.subnetPattern, match.group(3)));
					Data.hubMap.get(match.group(2)).setNetmask(singleMatcher(Data.netmaskPattern, match.group(3)));
					infMatcher(match.group(2), Data.infPattern, match.group(3));

					// Prints out parsed data to cli for debugging purposes
					System.out.println("Found hub " + Data.hubMap.get(match.group(2)).getName());
					System.out.println(Data.hubMap.get(match.group(2)).getSubnet());
					System.out.println(Data.hubMap.get(match.group(2)).getNetmask());
					Data.hubMap.get(match.group(2)).getInf();
				}
			} else {
				break;
			}
		}

	}
}
