package umt.ml.unittest;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import umt.ml.ID3.Classifier;

public class TestClassifierGetClass {
	
	@Test
	public void testGetClassListOfInteger() throws Exception {
		Classifier c=new Classifier("src/train.csv");
		assertEquals(c.getClassLabel(Arrays.asList(5,1,1,1,3,2,2,2,1)),0);
		assertEquals(c.getClassLabel(Arrays.asList(4,1,1,1,2,1,1,1,1)),0);
		assertEquals(c.getClassLabel(Arrays.asList(5,8,4,10,5,8,9,10,1)),1);
		
	}

}
