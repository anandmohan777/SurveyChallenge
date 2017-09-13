package com.challene.survey.service;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.challene.survey.model.Question;
import com.challene.survey.model.SurveyResponse;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.opencsv.CSVReader;

/**
 * Util class for read write csv and finding out the avg response.
 * 
 * @author anand
 */
public class SourceUtil {

	/*
	 * Use to store survey questions.
	 */
	private static List<Question> surveyQuestions;

	/*
	 * Use to store survey response data.
	 */
	private static Map<String, SurveyResponse> surveyResponses;
	
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss+hh:mm");

	public List<Question> getSurveyQuestions() {
		return surveyQuestions;
	}

	public Map<String, SurveyResponse> getSurveyResponses() {
		return surveyResponses;
	}

	/**
	 * Method to intialize surveyQuestions and surveyResponses from csv data.
	 * 
	 * @param pathQuestion
	 * @param pathResponse
	 * @throws IOException
	 */
	public void readCSV(final String pathQuestion, final String pathResponse)
			throws IOException {
		surveyQuestions = getQuestionData(pathQuestion);
		surveyResponses = getResponseData(pathResponse);
	}

	/**
	 * Method to calculate the Participation Percentage.
	 * 
	 * @return int
	 */
	public int participationPercentage() {
		int totalParticipant = 0;
		int validParticipant = 0;

		validParticipant = surveyResponses
				.entrySet()
				.stream()
				.filter(Predicates.notNull())
				.filter(entry -> isValidTimestamp(surveyResponses.get(
						entry.getKey()).getSubmittedAt()))
				.collect(Collectors.toList()).size();

		totalParticipant = surveyResponses.size();

		System.out.println("Valid participant count : " + validParticipant);
		System.out.println("Total participant count : " + totalParticipant);
		System.out.println("The participation percentage is : "+ calculatePercentage(validParticipant, totalParticipant) + "%");
		System.out.println("#################");

		return totalParticipant;
	}

	/**
	 * Method to calculate the average rating of each question.
	 * 
	 * @return void
	 */
	public void avgRatingQuestion() {
		/*
		 * First index is question index. Second index is total points. Third
		 * index is total count of people who filled that question response.
		 */
		Integer index = 0;
		Integer start = 1;

		final Map<Integer, List<Integer>> solution = new LinkedHashMap<>();

		/*
		 * Getting all the questions having ratingquestion.
		 */
		for (final Question question : surveyQuestions) {
			if ("ratingquestion".equalsIgnoreCase(question.getType())) {
				solution.put(start, new ArrayList<Integer>());
				index++;
			}
			++start;
		}

		/*
		 * Getting total points of ratingquestion and total count of responses.
		 */
		surveyResponses
				.entrySet()
				.stream()
				.filter(p -> isValidTimestamp(p.getValue().getSubmittedAt()))
				.forEach(
						key -> {
							solution.keySet()
									.stream()
									.forEach(
											k -> {
												final List<String> responses = key
														.getValue()
														.getResponses();
												final String points = responses
														.get(k - 1);
												if (isInteger(points)) {
													solution.get(k)
															.add(Integer
																	.valueOf(points));
												}
											});
						});
		printAverage(solution);
	}

	/**
	 * Method to calculate the percentage of participants.
	 * 
	 * @param validParticipant
	 * @param totalParticipant
	 * @return double
	 */
	private double calculatePercentage(final int validParticipant,
			final int totalParticipant) {
		double result = (double) validParticipant / totalParticipant;
		result = result * 100;
		return Math.round(result * 100.0) / 100.0;
	}

	/**
	 * Method to print the average of each rating question.
	 * 
	 * @param solution
	 */
	private void printAverage(final Map<Integer, List<Integer>> solution) {
		for (final Entry<Integer, List<Integer>> allvalue : solution.entrySet()) {
			if(!allvalue.getValue().isEmpty()) {
				final IntSummaryStatistics stats = allvalue.getValue().stream()
						.collect(Collectors.summarizingInt(Integer::intValue));
				System.out.println("Rating Question : "
						+ surveyQuestions.get(allvalue.getKey() - 1).getText()
						+ ", Average : " + stats.getAverage());
			}
		}
	}

	/**
	 * Method to read survey question data from csv.
	 * 
	 * @param pathQuestion
	 * @return {@link HashMap}
	 * @throws IOException
	 */
	private List<Question> getQuestionData(final String pathQuestion)
			throws IOException {
		final List<Question> surveyQuestionCsv = new ArrayList<Question>();
		try (CSVReader reader = new CSVReader(new FileReader(pathQuestion))) {
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
				final Question response = new Question(record[themeIndex],
						record[typeIndex], record[textIndex]);
				surveyQuestionCsv.add(response);
			}
		} catch (final IOException ex) {
			throw new IOException("File parse exception occured : " + pathQuestion);
		}

		return surveyQuestionCsv;
	}

	/**
	 * Method to check if the input string is integer value or not.
	 * 
	 * @param string
	 * @return boolean
	 */
	private boolean isInteger(final String intValue) {
		boolean isValidInteger = false;
		try {
			Integer.parseInt(intValue);
			isValidInteger = true;
		} catch (final NumberFormatException exception) {
			// Exception not throwing because we are just checking the data type.
		}
		return isValidInteger;
	}

	/**
	 * Method to check if the input string is valid timestamp.
	 * 
	 * @param string
	 * @return boolean
	 */
	private boolean isValidTimestamp(final String timeStamp) {
		boolean isValidTime = false;
		try {
			SIMPLE_DATE_FORMAT.parse(timeStamp);
			isValidTime = true;
		} catch (ParseException parseException) {
			// Exception not throwing because we are just checking the data type.
		}
		return isValidTime;
	}
	
	/**
	 * Method to read survey response data from csv.
	 * 
	 * @param pathResponse
	 * @return {@link HashMap}
	 * @throws IOException
	 */
	private Map<String, SurveyResponse> getResponseData(
			final String pathResponse) throws IOException {
		final Map<String, SurveyResponse> surveyResponsesCsv = new ConcurrentHashMap<>();
		try (CSVReader reader = new CSVReader(new FileReader(pathResponse))) {
			final List<String[]> records = reader.readAll();
			final Iterator<String[]> iterator = records.iterator();

			while (iterator.hasNext()) {
				final String[] record = iterator.next();
				if (!Strings.isNullOrEmpty(record[0])
						|| !Strings.isNullOrEmpty(record[1])) {
					final SurveyResponse response = new SurveyResponse(
							record[0], record[1], record[2], Arrays.asList(
									record).subList(3, record.length));
					surveyResponsesCsv.put(
							response.getId() + response.getEmail(), response);
				}
			}
		} catch (final IOException ex) {
			throw new IOException("File parsing exception occured : "
					+ pathResponse);
		}

		return surveyResponsesCsv;
	}
}
