package com.challene.survey.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.challene.survey.model.Question;
import com.challene.survey.model.SurveyResponse;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.math.DoubleMath;
import com.opencsv.CSVReader;

/**
 * Util class common to all.
 * 
 * @author anand
 */
public class SourceUtil {

	private static List<Question> surveyQuestions;
	private static Map<String, SurveyResponse> surveyResponses;

	public static List<Question> getSurveyQuestions() {
		return surveyQuestions;
	}

	public static Map<String, SurveyResponse> getSurveyResponses() {
		return surveyResponses;
	}

	public void readCSV(String pathQuestion, String pathResponse)
			throws Exception {
		surveyQuestions = getQuestionData(pathQuestion);
		surveyResponses = getResponseData(pathResponse);
	}

	public int participationPercentage() {
		int totalParticipant = 0;
		int validParticipant = 0;

		validParticipant = surveyResponses
				.entrySet()
				.stream().filter(Predicates.notNull())
				.filter(entry -> {
					SurveyResponse s1Response = surveyResponses.get(entry.getKey());
					if (Strings.nullToEmpty(s1Response.getSubmittedAt()).isEmpty()) {
						return false;
					}
					return true;
				}).collect(Collectors.toList()).size();

		totalParticipant = surveyResponses.size();
		System.out.println("Total valid participant counts " + totalParticipant);
		System.out.println("Total participant counts " + validParticipant);
		System.out.println("The participation percentage is "+ calculatePercentage(validParticipant, totalParticipant) + "%");

		return totalParticipant;
	}

	public void avgRatingQuestion() {
		/*
		 * First index is question index.
		 * Second index is total points.
		 * Third index is total count of people who filled that question response.
		 */
		final int[] ratingQuestionIndex = new int[surveyQuestions.size()];
		Integer index = 0;
		Integer start = 1;

		final Map<Integer, List<Integer>> solution = new ConcurrentHashMap<>();
		/*
		 * Getting all the questions having ratingquestion.
		 */
		for (Question question : surveyQuestions) {
			if ("ratingquestion".equalsIgnoreCase(question.getType())) {
				ratingQuestionIndex[index] = start;
				solution.put(index, new ArrayList<Integer>());
				index++;
			}
			++start;
		}

		/*
		 * Getting total points of ratingquestion and total count of responses.
		 */
		for (Map.Entry<String, SurveyResponse> entry : surveyResponses.entrySet()) {
			SurveyResponse sResponse = entry.getValue();
			if (!Strings.nullToEmpty(sResponse.getSubmittedAt()).isEmpty()) {				
				for (int i : ratingQuestionIndex) {
					try {
						final List<String> responses = sResponse.getResponses();
						final String points = responses.get(i - 1);
						if (isInteger(points)) {
							solution.get(i-1).add(Integer.valueOf(points));
						}
					} catch (Exception e) {
						//System.out.println("Parsing Exception occured :" + e.getMessage());
						//e.printStackTrace();
					}
				}
			}
		}
		printAverage(ratingQuestionIndex, solution);
	}

	private  boolean isInteger(String s) {
		boolean isValidInteger = false;
		try
		{
			Integer.parseInt(s);
			isValidInteger = true;
		}
		catch (NumberFormatException ex) {
		}
		return isValidInteger;
	}


	private List<Question> getQuestionData(String pathQuestion) throws IOException {
		final CSVReader reader = new CSVReader(new FileReader(pathQuestion));
		final List<Question> surveyQuestionCsv = new ArrayList<Question>();
		final List<String[]> records = reader.readAll();
		final Iterator<String[]> iterator = records.iterator();


		int themeIndex = 0;
		int typeIndex = 1;
		int textIndex = 2;

		String[] record = iterator.next();
		for (int i = 0; i < record.length; i++) {
			if ("type".equalsIgnoreCase(record[i])) {
				typeIndex = i;
			} else if ("theme".equalsIgnoreCase(record[i])) {
				themeIndex = i;
			} else if ("text".equalsIgnoreCase(record[i])) {
				textIndex = i;
			}
		}

		while (iterator.hasNext()) {
			record = iterator.next();

			Question response = new Question(record[themeIndex],
					record[typeIndex], record[textIndex]);
			surveyQuestionCsv.add(response);
		}
		System.out.println(surveyQuestionCsv);
		return surveyQuestionCsv;
	}

	private double calculatePercentage(int validParticipant,
			int totalParticipant) {
		double result = (double) validParticipant / totalParticipant;
		result = result * 100;
		return Math.round(result * 100.0) / 100.0;
	}

	private void printAverage(final int[] ratingQuestionIndex,
			final Map<Integer, List<Integer>> solution) {

		for (Entry<Integer, List<Integer>> allvalue : solution.entrySet()) {
			try {
				IntSummaryStatistics stats = allvalue.getValue().stream()
						.collect(Collectors.summarizingInt(Integer::intValue));
				System.out.println("Rating Question : "	+ surveyQuestions.get(allvalue.getKey()).getText() + ", Average :" + stats.getAverage());
			} catch (ArithmeticException e) {
				System.out.println("ArithmeticException occured :" +  e.getMessage() + ", for question: " + surveyQuestions.get(allvalue.getKey()).getText());
			}
		}

	}

	private Map<String, SurveyResponse> getResponseData(String pathResponse)
			throws IOException {
		final CSVReader reader = new CSVReader(new FileReader(pathResponse));
		final List<String[]> records = reader.readAll();
		final Iterator<String[]> iterator = records.iterator();
		final Map<String, SurveyResponse> surveyResponsesCsv = new HashMap<String, SurveyResponse>();

		while (iterator.hasNext()) {
			final String[] record = iterator.next();
			SurveyResponse response = new SurveyResponse(record[0], record[1],
					record[2], Arrays.asList(record).subList(3, record.length));
			surveyResponsesCsv.put(response.getId() + response.getEmail(),
					response);
		}
		System.out.println(surveyResponsesCsv);
		return surveyResponsesCsv;
	}

}
