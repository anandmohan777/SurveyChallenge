package com.challenge.Survey;

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
		super();
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
}
