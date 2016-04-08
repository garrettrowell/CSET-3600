package JunitTesting;

import static org.junit.Assert.assertEquals;

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
	TreeMap<String,String> ports1, ports2;
	
	@Before
	public void setup() {
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
		vmObject1.setName("vm1");
		vmObject1.setOs("LINUX");
		vmObject1.setSrc("/srv/VMLibrary/JeOS");
		vmObject1.setVer(7.3);
		vmObject1.setInterfaces(ports1);
		
		ports2 = new TreeMap<String,String>();
		ports2.put("eth0", "192.168.1.4");
		
		vmObject2 = new VM();
		vmObject2.setName("vm2");
		vmObject2.setOs("LINUX");
		vmObject2.setSrc("/srv/VMLibrary/JeOS");
		vmObject2.setVer(7.3);
		vmObject2.setInterfaces(ports2);
		
		application.Data.hubMap.put(hubObject.getName(), hubObject);
		application.Data.vmMap.put(vmObject1.getName(), vmObject1);
		application.Data.vmMap.put(vmObject2.getName(), vmObject2);
	}
	
	@Test
	public void nameTest() {
		boolean testName = Validator.validateName("Phouthasak");
		assertEquals(true,testName);		
	}
	
	@Test
	public void duplicateNamesInVMMapTest() {
		String testName = "Vm1";
		boolean validName = Validator.validateName(testName);
		assertEquals(false, validName);
	}
	
	@Test
	public void duplicateNamesInHUBMapTest() {
		String testName = "Hub1";
		boolean validName = Validator.validateName(testName);
		assertEquals(false, validName);
	}
	
	@Test
	public void nullNameTest() {
		String testName = null;
		boolean validName = Validator.validateName(testName);
		assertEquals(false, validName);
	}
	
	@Test
	public void emptyNameTest() {
		String testName = "";
		boolean validName = Validator.validateName(testName);
		assertEquals(false, validName);
	}
	
	@Test
	public void correctFormatAndRangeIpTest() {
		boolean testIp = Validator.validateIp("192.168.2.5");
		assertEquals(true,testIp);
	}
	
	@Test
	public void duplicateIpInVMMapTest() {
		String testIp = "192.168.1.3";
		boolean validIp = Validator.validateIp(testIp);
		assertEquals(false, validIp);
	}
	
	@Test
	public void duplicateIpInHUBMapTest() {
		String testIp = "192.168.1.0";
		boolean validIp = Validator.validateIp(testIp);
		assertEquals(false, validIp);
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
	public void nullIpTest() {
		boolean testIp = Validator.validateIp(null);
		assertEquals(false, testIp);
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
	public void nullNetmaskTest() {
		boolean testIp = Validator.validateNetmask(null);
		assertEquals(false, testIp);
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
	public void nullVerTest() {
		boolean testVer = Validator.validateVer(null);
		assertEquals(false, testVer);
	}
	
	@Test
	public void trueSrcTest() {
		boolean testSrc = Validator.validateSrc("/srv/VMLibrary/JeOS");
		assertEquals(true,testSrc);
	}
	
	@Test
	public void nullSrcTest() {
		boolean testSrc = Validator.validateSrc(null);
		assertEquals(false, testSrc);
	}
	
	@Test
	public void falseSrcTest() {
		boolean testSrc = Validator.validateSrc("/srv/HUBLibrary/JeOS");
		assertEquals(false, testSrc);
	}
	
	@Test
	public void foundOsTest() {
		boolean testOs = Validator.validateOs("LINUX");
		assertEquals(true, testOs);
	}
	
	@Test
	public void nullOsTest() {
		boolean testOs = Validator.validateOs(null);
		assertEquals(false, testOs);
	}
	
	@Test
	public void foundVlanTest() {
		boolean testVlan = Validator.validateVlan("v2");
		assertEquals(true, testVlan);
	}
	
	@Test
	public void nullVlanTest() {
		boolean testVlan = Validator.validateVlan(null);
		assertEquals(false, testVlan);
	}
	
	@Test
	public void emptyVlanTest() {
		boolean testVlan = Validator.validateVlan("");
		assertEquals(false, testVlan);
	}
}
