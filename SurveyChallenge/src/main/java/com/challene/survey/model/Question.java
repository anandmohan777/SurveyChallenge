package com.challene.survey.model;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * 
 * @author anand Entity for question.
 */
public class Question {
	private String theme;
	private String type;
	private String text;

	public Question(String theme, String type, String text) {
		this.theme = theme;
		this.type = type;
		this.text = text;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("Question").add("theme", theme)
				.add("type", type).add("text", text).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.type);
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

		final Question input = (Question) obj;
		return Objects.equals(this.theme, input.getTheme())
				&& Objects.equals(this.text, input.getText())
				&& Objects.equals(this.type, input.getType());
	}
}
