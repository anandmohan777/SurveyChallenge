package com.challene.survey.service;

import java.io.IOException;

/**
 * Class to show the results of survey.
 * 
 * @author anandmohan
 *
 */
public class CalculateReport {

	private final String pathQuestion;
	private final String pathResponse;
	
	public CalculateReport(String pathQuestion, String pathResponse) {
		this.pathQuestion = pathQuestion;
		this.pathResponse = pathResponse;
	}
	
	public String getPathQuestion() {
		return pathQuestion;
	}

	public String getPathResponse() {
		return pathResponse;
	}

	public void calculateReportForCsv() throws IOException {
		final SourceUtil sourceUtilReader = new SourceUtil();

		sourceUtilReader.readCSV(getPathQuestion(), getPathResponse());
		sourceUtilReader.participationPercentage();
		sourceUtilReader.avgRatingQuestion();
	}
}
