package com.challenge.Survey;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.opencsv.CSVReader;

/**
 * 
 * @author anand Util class common to all.
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

		validParticipant = FluentIterable.from(surveyResponses.entrySet())
				.filter(new Predicate<Map.Entry<String, SurveyResponse>>() {
					public boolean apply(Entry<String, SurveyResponse> input) {
						SurveyResponse s1Response = surveyResponses.get(input
								.getKey());
						if ("".equals(Strings.nullToEmpty(s1Response
								.getSubmittedAt()))) {
							return false;
						}
						return true;
					}
				}).toList().size();

		
		totalParticipant = surveyResponses.size();
		

		double result = (double) validParticipant / totalParticipant;
		result = result * 100;
		result = Math.round(result * 100.0) / 100.0;

		System.out.println("The participation percentage is " + result + "%");
		System.out.println("Total valid participant counts " + totalParticipant);
		System.out.println("Total participant counts " + validParticipant);
		
		return totalParticipant;

	}

	public void avgRatingQuestion() {
		int[][] ratingQuestionIndex = new int[surveyQuestions.size()][3];
		Integer index = 0;
		Integer start = 1;

		for (Question question : surveyQuestions) {
			if ("ratingquestion".equalsIgnoreCase(question.getType())) {
				ratingQuestionIndex[index++][0] = start;
			}
			++start;
		}

		Integer totalCount = 0;
		for (Map.Entry<String, SurveyResponse> entry : surveyResponses
				.entrySet()) {
			totalCount = 0;
			SurveyResponse s1Response = entry.getValue();
			if (!Strings.nullToEmpty(s1Response.getSubmittedAt()).isEmpty()) {
				List<String> responses = s1Response.getResponses();
				for (int i[] : ratingQuestionIndex) {

					try {
						if (i[0] != 0) {
							i[1] += (responses.get(i[0] - 1) != null
									&& !"".equals(responses.get(i[0] - 1)) ? Integer
									.valueOf(responses.get(i[0] - 1)) : 0);
							i[2] += + 1;
						}
					} catch (Exception e) {

					}
				}
			}
		}
		
		
		for (int i[] : ratingQuestionIndex) {
			
			try {
			double totalPoints = i[1];
			double totalVotes = i[2];
			double result = (double) totalPoints / totalVotes;
			result = Math.round(result * 100.0) / 100.0;
			
			Integer rank = i[0]-1;
			System.out.println("Rating Question : "+surveyQuestions.get(rank).getText() + ", Average :"+result );
			} catch (Exception e) {

			}
		}
		
		System.out.println(Arrays.deepToString(ratingQuestionIndex));
	}

	private List getQuestionData(String pathQuestion) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(pathQuestion));
		List<String[]> records = reader.readAll();
		Iterator<String[]> iterator = records.iterator();
		List<Question> surveyQuestionCsv = new ArrayList<Question>();

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
		//System.out.println("surveyQuestionCsv " + surveyQuestionCsv);
		return surveyQuestionCsv;
	}

	private Map<String, SurveyResponse> getResponseData(String pathResponse) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(pathResponse));
		List<String[]> records = reader.readAll();
		Iterator<String[]> iterator = records.iterator();
		Map<String, SurveyResponse> surveyResponsesCsv = new HashMap<String, SurveyResponse>();

		while (iterator.hasNext()) {
			String[] record = iterator.next();
			SurveyResponse response = new SurveyResponse(record[0], record[1],
					record[2], Arrays.asList(record).subList(3, record.length));
			surveyResponsesCsv.put(response.getId() + response.getEmail(),
					response);
		}
		//System.out.println("surveyResponsesCsv " + surveyResponsesCsv);
		return surveyResponsesCsv;
	}

}
