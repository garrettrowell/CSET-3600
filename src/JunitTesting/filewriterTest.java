package JunitTesting;

import static org.junit.Assert.assertEquals;

import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import application.FileWriter;
import application.HUB;
import application.VM;
import javafx.scene.control.TextArea;

public class filewriterTest {
	HUB hubObject;
	VM vmObject1, vmObject2;
	TreeMap<String,String> ports1, ports2;
	TextArea testTextArea;
	@Before
	public void setUp() {
		TreeSet<String> hubInterfaces = new TreeSet<String>();
		hubInterfaces.add("Vm1.eth0");
	
		hubObject = new HUB();
		hubObject.setName("Hub1");
		hubObject.setNetmask("255.255.255.0");
		hubObject.setSubnet("192.168.1.0");
		hubObject.setInfs(hubInterfaces);
		
		ports1 = new TreeMap<String,String>();
		ports1.put("eth0", "192.168.1.3");
		
		vmObject1 = new VM();
		vmObject1.setName("vm1");
		vmObject1.setOs("LINUX");
		vmObject1.setSrc("/srv/VMLibrary/JeOS");
		vmObject1.setVer(7.3);
		vmObject1.setInterfaces(ports1);
		
		application.Data.hubMap.put(hubObject.getName(), hubObject);
		application.Data.vmMap.put(vmObject1.getName(), vmObject1);
		
		testTextArea = new TextArea();
	}
	
	@Test
	public void fileWriterOutputTest() {
		FileWriter.writeFile(testTextArea);
		String testingString = testTextArea.getText();
		String testString = "vm Vm1 {\n" +
							"\tos : LINUX\n" +
							"\tver : \"7.3\"\n" +
							"\tsrc : \"/srv/VMLibrary/JeOS\"\n" +
							"\teth0 : \"192.168.1.3\"\n" +
							"}\n" +
							"\n" +
							"hub Hub1 {\n" +
							"\tinf : Vm1.eth0\n" + 
							"\tsubnet : \"192.168.1.0\"\n" +
							"\tnetmask : \"255.255.255.0\"\n" +
							"}\n" +
							"\n" +
							"partial_solution {\n" +
							"(Vm1.eth0 v2.inf21)\n" +
							"}";
		boolean test = testingString.equals(testString);
		assertEquals(true, test);
		
	}

}
