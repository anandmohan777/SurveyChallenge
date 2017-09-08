package com.challenge.Survey;

import org.testng.annotations.Test;

public class ChallengeTest {

	@Test
	public void testMain() {
		giventestCSV();
		whenMainCalled();
		thenCheckValueRetreived();
	}

	private void giventestCSV() {
		
		InputOutput inputOutput= new InputOutput();

	    String input = "add 5";
	    InputStream in = new ByteArrayInputStream(input.getBytes());
	    System.setIn(in);
	}

	private void whenMainCalled() {

	}

	private void thenCheckValueRetreived() {

	}
}
