package test;

import static org.junit.Assert.*;

import org.junit.Test;

import background.MyMessage;

public class test {

	@Test
	public void test() {
		MyMessage m1 = new MyMessage("123","1");
		MyMessage m2 = new MyMessage("123","1");
		System.out.println(m1.isDuplicated(m2)?"yes":"no");
	}

}
