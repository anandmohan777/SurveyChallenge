package com.challene.survey.controller;


/**
 * Entity to handle the logic of class in {@link Challenge}.
 * @author anand 
 */
public class Challenge {
	public static void main(String[] args) throws Exception {

		if( args.length < 2) {
			System.out.println("Please pass 2 arguments");
			System.exit(0);
		}
		
		final String pathQuestion = args[0];
		final String pathResponse = args[1];

		final SourceUtil sourceUtilReader = new SourceUtil();
		sourceUtilReader.readCSV(pathQuestion, pathResponse);
		
		sourceUtilReader.participationPercentage();
		sourceUtilReader.avgRatingQuestion();
	}
}
