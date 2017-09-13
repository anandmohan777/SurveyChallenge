package com.challene.survey.controller;

import com.challene.survey.service.CalculateReport;

/**
 * Entity to handle the logic of class in {@link Challenge}.
 * 
 * @author anand
 */
public class Challenge {
	
	/**
	 * Service layer class to handle logic for report generation.
	 */
	private final CalculateReport calculateReport;
	
	public Challenge(final CalculateReport calculateReport) {
		this.calculateReport = calculateReport;
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			throw new IllegalArgumentException("Please pass 2 arguments");
		}
		
		final CalculateReport calculateReport = new CalculateReport(args[0], args[1]);
		final Challenge challenge = new Challenge(calculateReport);

		challenge.calculateReport.calculateReportForCsv();
	}
}
