package com.challenge.survey.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.challene.survey.service.SourceUtil;


public class SourceUtilTest {

	private SourceUtil sourceUtil;
	private int response;

	@BeforeMethod()
	public void setup() {
		sourceUtil = new SourceUtil();
		response = 0;
	}

	@Test
	public void testReadCSV() throws Exception {
		whenReadCSVCalled();
		thenCheckValueRetreived();
	}

	@Test
	public void testParticipationPercentage() throws Exception {
		givenData();
		whenParticipationPercentageCalled();
		thenCheckPercentageValueRetreived();
	}

	@Test
	public void testAvgRatingQuestion() throws Exception {
		givenData();
		whenAvgRatingQuestionCalled();
		thenCheckValueRetreived();
	}

	private void givenData() throws Exception {
		whenReadCSVCalled();
	}

	private void whenReadCSVCalled() throws Exception {
		sourceUtil.readCSV("resources/survey-1.csv",
				"resources/survey-1-responses.csv");
	}

	private void whenParticipationPercentageCalled() throws Exception {
		response = sourceUtil.participationPercentage();
	}

	private void whenAvgRatingQuestionCalled() throws Exception {
		sourceUtil.avgRatingQuestion();
	}

	private void thenCheckValueRetreived() {
		assertNotNull(sourceUtil.getSurveyQuestions());
		assertNotNull(sourceUtil.getSurveyResponses());
	}

	private void thenCheckPercentageValueRetreived() {
		assertEquals(response, 6);
	}

}
