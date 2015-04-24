package hellomaven;

import org.junit.Test;

public class HelloMavenTest {
	
	@Test
	public void testSayHello(){
		HelloMaven helloMaven = new HelloMaven();
		String result = helloMaven.sayHello();
		System.out.println(result);
	}
}
