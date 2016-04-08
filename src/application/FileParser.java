package application;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
	public String singleMatcher(String regex, String input) {
		// Simple matcher that uses a specified regex and if found
		// in the specified input string returns the capture group
		String output = null;
		Pattern patt = Pattern.compile(regex);
		Matcher match = patt.matcher(input);
		if (match.find()) {
			output = match.group(1);
		}
		return output;
	}

	public void ethMatcher(String vm, String regex, String input) {
		// Matcher that finds each instance of eth# for a specified vm.
		// Populates the specified vm's interfaces hash map
		Pattern patt = Pattern.compile(regex);
		Matcher match = patt.matcher(input);
		TreeMap<String, String> interfaces = new TreeMap<String, String>();
		while (true) {
			if (match.find()) {
				interfaces.put(match.group(1), match.group(2));
			} else {
				break;
			}
		}
		Data.vmMap.get(vm).setInterfaces(interfaces);
	}

	public void infMatcher(String hub, String regex, String input) {
		// hub interface matcher first finds the line in the specified hub
		// that corresponds with the inf's then iterates through each
		// inf and adds them to the hubs inf arraylist
		Pattern linePatt = Pattern.compile(regex);
		Matcher lineMatch = linePatt.matcher(input);
		if (lineMatch.find()) {
			Pattern innerPatt = Pattern.compile("(\\w+\\.\\w+)");
			Matcher innerMatch = innerPatt.matcher(lineMatch.group(1));
			while (true) {
				if (innerMatch.find()) {
					Data.hubMap.get(hub).addInf(innerMatch.group(1));
				} else {
					break;
				}
			}
		}
	}

	public FileParser(File selectedFile) {
		String file = null;

		// if specified config file (represented as a string) is not empty
		// attempt to parse it
		if (selectedFile != null) {
			file = application.FileOperations.readFile(selectedFile);
		}

		// parses the file for each node or vm
		Pattern patt = Pattern.compile(Data.nodePattern, Pattern.DOTALL);
		Matcher match = patt.matcher(file);
		while (true) {
			// loop that is valid until we run out of nodes to parse
			if (match.find()) {
				/*
				 * if the file contains for example: vm vmName { ... }
				 */
				if (match.group(1).equals("vm")) {
					// if the node is a vm
					// create a new vm object
					Data.vmMap.put(match.group(2), new VM());
					// parse and set the vm's name
					Data.vmMap.get(match.group(2)).setName(match.group(2));
					// parse and set the vm's os
					Data.vmMap.get(match.group(2)).setOs(singleMatcher(Data.osPattern, match.group(3)));
					// parse and set the vm's ver
					Data.vmMap.get(match.group(2))
							.setVer(Double.parseDouble(singleMatcher(Data.verPattern, match.group(3))));
					// parse and set the vm's src
					Data.vmMap.get(match.group(2)).setSrc(singleMatcher(Data.srcPattern, match.group(3)));
					// parse and set the vm's eth(s)
					ethMatcher(match.group(2), Data.ethPattern, match.group(3));

					// Prints out parsed data to cli for debugging purposes
					System.out.println("Found vm:");
					System.out.println("name\t=\t" + Data.vmMap.get(match.group(2)).getName());
					System.out.println("os\t=\t" + Data.vmMap.get(match.group(2)).getOs());
					System.out.println("ver\t=\t" + Data.vmMap.get(match.group(2)).getVer());
					System.out.println("src\t=\t" + Data.vmMap.get(match.group(2)).getSrc());
					for (Map.Entry<String, String> entry : Data.vmMap.get(match.group(2)).getInterfaces().entrySet()) {
						System.out.println(entry.getKey() + "\t=\t" + entry.getKey());
					}
					Data.vmMap.get(match.group(2)).getInterfaces();
					System.out.println("---------------------------------");
				} else if (match.group(1).equals("hub")) {
					// if the node is a hub
					// create a new hub object
					Data.hubMap.put(match.group(2), new HUB());
					// parse and set the hubs name
					Data.hubMap.get(match.group(2)).setName(match.group(2));
					// parse and set the hubs subnet
					Data.hubMap.get(match.group(2)).setSubnet(singleMatcher(Data.subnetPattern, match.group(3)));
					// parse and set the hubs netmask
					Data.hubMap.get(match.group(2)).setNetmask(singleMatcher(Data.netmaskPattern, match.group(3)));
					// parse and set the hubs inf(s)
					infMatcher(match.group(2), Data.infPattern, match.group(3));

					// Prints out parsed data to cli for debugging purposes
					System.out.println("Found hub:");
					System.out.println("name\t=\t" + Data.hubMap.get(match.group(2)).getName());
					System.out.println("subnet\t=\t" + Data.hubMap.get(match.group(2)).getSubnet());
					System.out.println("netmask\t=\t" + Data.hubMap.get(match.group(2)).getNetmask());
					System.out.println("inf(s)\t=\t");
					for (String inf : Data.hubMap.get(match.group(2)).getInfs()) {
						System.out.println("\t\t" + inf);
					}
					System.out.println("---------------------------------");
				}
			} else {
				// we ran out of things to parse to break out of the loop
				break;
			}
		}
	}
}
