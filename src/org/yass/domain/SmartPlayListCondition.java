package org.yass.domain;

public class SmartPlayListCondition {

	public String term;
	public String operator;
	public String value;

	public SmartPlayListCondition(final String term, final String operator, final String value) {
		this.term = term;
		this.operator = operator;
		this.value = value;
	}
}