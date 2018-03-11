package com.challene.survey.model;

import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * @author anand Entity to store the survey response.
 */
public class SurveyResponse {

	private String id;
	private String email;
	private String submittedAt;
	private List<String> responses;

	public SurveyResponse(String email, String id, String submittedAt,
			List<String> responses) {
		this.id = id;
		this.email = email;
		this.submittedAt = submittedAt;
		this.responses = responses;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(String submittedAt) {
		this.submittedAt = submittedAt;
	}

	public List<String> getResponses() {
		return responses;
	}

	public void setResponses(List<String> responses) {
		this.responses = responses;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("SurveyResponse").add("id", id)
				.add("email", email).add("submittedAt", submittedAt)
				.add("responses", responses).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (null == obj) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		return Objects.equals(this.id, ((SurveyResponse) obj).id);
	}
}
