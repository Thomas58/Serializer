package org.jspace.io.test.binary;

import org.jspace.io.xml.exceptions.XMLSyntaxException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class Main {

	public static void main(String[] args) throws IllegalAccessException, InstantiationException, XMLSyntaxException {
		Result result = JUnitCore.runClasses(StandardTests.class);
	    for (Failure failure : result.getFailures()) {
	      System.out.println(failure.toString());
	    }
	    result = JUnitCore.runClasses(FileTests.class);
	    for (Failure failure : result.getFailures()) {
	      System.out.println(failure.toString());
	    }
	}
}
