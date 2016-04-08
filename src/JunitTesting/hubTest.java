package JunitTesting;

import static org.junit.Assert.assertEquals;

import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import application.HUB;

public class hubTest {
	HUB hubObject;
	TreeSet<String> inf;
	TreeSet<String> testingInf;
	
	@Before
	public void setup() {
		hubObject = new HUB();
		hubObject.setName("Hub1");
		hubObject.setSubnet("192.168.1.1");
		hubObject.setNetmask("255.255.0.0");
		hubObject.setPosY(5);
		hubObject.setPosX(5);
		
		inf = new TreeSet<String>();
		inf.add("Gem.eth0");
		inf.add("Ruby.eth0");
		inf.add("Diamond.eth1");
		
		hubObject.setInfs(inf);
		
		testingInf = new TreeSet<String>();
		testingInf.add("Gem.eth0");
		testingInf.add("Pearl.eth0");
		testingInf.add("Gem.eth0");
		testingInf.add("Ruby.eth0");
		testingInf.add("Diamond.eth1");
		
	}
	
	@Test
	public void hubNameTest() {
		assertEquals("Hub1", hubObject.getName());
	}
	
	@Test
	public void hubSubnetTest() {
		assertEquals("192.168.1.1", hubObject.getSubnet());
	}
	
	@Test
	public void hubNetmaskTest() {
		assertEquals("255.255.0.0",hubObject.getNetmask());
	}
	
	@Test
	public void hubSetInfTest() {
		assertEquals(inf, hubObject.getInfs());
	}
	
	@Test
	public void hubAddInfTest() {
		hubObject.addInf("Pearl.eth0");
		assertEquals(testingInf, hubObject.getInfs());
	}
	
	@Test
	public void hubSetYTest() {
		assertEquals(5, hubObject.getPosY(),0);
	}
	
	@Test
	public void hubSetXTest() {
		assertEquals(5, hubObject.getPosX(),0);
	}
}
