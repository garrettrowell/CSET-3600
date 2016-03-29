package JunitTesting;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import application.HUB;
import application.VM;
import application.Validator;

public class validatorTest {
	HUB hubObject;
	VM vmObject1, vmObject2;
	LinkedHashMap<String, VM> vmMap = new LinkedHashMap<String, VM>();
	LinkedHashMap<String, HUB> hubMap;
	TreeMap<String,String> ports1, ports2;
	
	@Before
	public void setup() {
		vmMap = new LinkedHashMap<String, VM>();
		hubMap = new LinkedHashMap<String, HUB>();
		
		TreeSet<String> hubInterfaces = new TreeSet<String>();
		hubInterfaces.add("Vm1.eth0");
		hubInterfaces.add("Vm2.eth0");
		hubInterfaces.add("Vm3.eth1");
		
		hubObject = new HUB();
		hubObject.setName("Hub1");
		hubObject.setNetmask("255.255.255.0");
		hubObject.setSubnet("192.168.1.0");
		hubObject.setInfs(hubInterfaces);
		
		ports1 = new TreeMap<String,String>();
		ports1.put("eth0", "192.168.1.3");
		
		vmObject1 = new VM();
		vmObject1.setName("Vm1");
		vmObject1.setOs("LINUX");
		vmObject1.setSrc("/srv/VMLibrary/JeOS");
		vmObject1.setVer(7.3);
		vmObject1.setInterfaces(ports1);
		
		ports2 = new TreeMap<String,String>();
		ports2.put("eth0", "192.168.1.4");
		
		vmObject2 = new VM();
		vmObject2.setName("Vm2");
		vmObject2.setOs("LINUX");
		vmObject2.setSrc("/srv/VMLibrary/JeOS");
		vmObject2.setVer(7.3);
		vmObject2.setInterfaces(ports2);
		
		hubMap.put(hubObject.getName(), hubObject);
		vmMap.put(vmObject1.getName(), vmObject1);
		vmMap.put(vmObject2.getName(), vmObject2);
	}
	
	@Test
	public void nameTest() {
		boolean testName = Validator.validateName("Phouthasak");
		assertEquals(true,testName);		
	}
	
	@Test
	public void duplicateNamesInVMMapTest() {
		boolean validName = true;
		String testName = "Gem";
		
		for(Map.Entry<String, VM> entry : vmMap.entrySet()) {
			VM vmObject = entry.getValue();
			if(vmObject.getName().trim().toLowerCase().equals(testName.trim().toLowerCase())){
				validName = false;
			}
		}
		
		assertEquals(true, validName);
	}
	
	@Test
	public void duplicateNamesInHUBMapTest() {
		boolean validName = true;
		String testName = "Star";
		for(Map.Entry<String, HUB> entry : hubMap.entrySet()) {
			HUB hubObject = entry.getValue();
			if(hubObject.getName().trim().toLowerCase().equals(testName.trim().toLowerCase())){
				validName = false;
			}
		}
		assertEquals(true,validName);
	}
	
	@Test
	public void correctFormatAndRangeIpTest() {
		boolean testIp = Validator.validateIp("192.168.2.5");
		assertEquals(true,testIp);
	}
	
	@Test
	public void outOfRangeIpTest() {
		boolean testIp = Validator.validateIp("300.0.0.0");
		assertEquals(false,testIp);
	}
	
	@Test
	public void wrongFormatIpTest() {
		boolean testIp = Validator.validateIp("1955.122.168.1");
		assertEquals(false,testIp);
	}
	
	@Test
	public void correctFormatandRangeNetmaskTest() {
		boolean testNetmask = Validator.validateNetmask("255.255.255.0");
		assertEquals(true, testNetmask);
	}
	
	@Test
	public void outOfRangeNetmaskTest() {
		boolean testNetmask = Validator.validateNetmask("300.300.300.0");
		assertEquals(false, testNetmask);
	}
	
	@Test
	public void notCorrectFormatNetmaskTest() {
		boolean testNetmask = Validator.validateNetmask("Hello World");
		assertEquals(false, testNetmask);
	}
	
	@Test
	public void doubleVerTest() {
		boolean testVer = Validator.validateVer("7.3");
		assertEquals(true,testVer);
	}
	
	@Test
	public void stringVerTest() {
		boolean testVer = Validator.validateVer("Hello World");
		assertEquals(false,testVer);
	}
	@Test
	public void trueSrcTest() {
		boolean testSrc = Validator.validateSrc("/srv/VMLibrary/JeOS");
		assertEquals(true,testSrc);
	}
	
	@Test
	public void falseSrcTest() {
		boolean testSrc = Validator.validateSrc("/srv/HUBLibrary/JeOS");
		assertEquals(false, testSrc);
	}
}
