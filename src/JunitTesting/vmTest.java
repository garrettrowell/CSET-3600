package JunitTesting;

import static org.junit.Assert.assertEquals;

import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import application.VM;

public class vmTest {
	VM vmObject;
	TreeMap<String,String> inf;
	
	@Before
	public void setup() {
		vmObject = new VM();
		vmObject.setName("Vm1");
		vmObject.setSrc("/srv/lib");
		vmObject.setVer(3.14);
		vmObject.setOs("LINUX");
		vmObject.setPosY(5);
		vmObject.setPosX(5);
		
		inf = new TreeMap<String,String>();
		inf.put("eth0", "192.168.3.3");
		inf.put("eth1", "192.168.3.4");
		inf.put("eth2", "192.168.3.5");

		vmObject.setInterfaces(inf);
	}
	
	@Test
	public void vmNameTest() {
		assertEquals("Vm1", vmObject.getName());
	}
	
	@Test
	public void vmSrcTest() {
		assertEquals("/srv/lib", vmObject.getSrc());
	}

	@Test
	public void vmVerTest() {
		assertEquals(3.14, vmObject.getVer(),0);
	}
	
	@Test
	public void vmOsTest() {
		assertEquals("LINUX", vmObject.getOs());
	}
	
	@Test
	public void vmSetInfTest() {
		assertEquals(inf, vmObject.getInterfaces());
	}
	
	@Test
	public void vmAddInfTest() {
		vmObject.addInterface("eth3", "192.168.3.6");
		assertEquals("192.168.3.6", vmObject.getInterfaces().get("eth3"));
	}
	
	@Test
	public void vmSetYTest() {
		assertEquals(5, vmObject.getPosY());
	}
	
	@Test
	public void vmSetXTest() {
		assertEquals(5, vmObject.getPosX());
	}
}
