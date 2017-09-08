package com.challenge.Survey;

import java.util.Scanner;

/**
 * @author anand Entity to handle the logic of challenge.
 */
public class Challenge {
	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter csv path for Survey Data");
		System.out.println("Enter csv path for Response Data");
		String pathQuestion = sc.next();
		String pathResponse = sc.next();

		SourceUtil sourceUtilReader = new SourceUtil();
		sourceUtilReader.readCSV(pathQuestion, pathResponse);
		
		sourceUtilReader.participationPercentage();
		sourceUtilReader.avgRatingQuestion();
	}
}
