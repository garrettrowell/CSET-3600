package JunitTesting;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ graphicsTest.class, hubTest.class, validatorTest.class, vmTest.class, filewriterTest.class })
public class AllTests {

}
