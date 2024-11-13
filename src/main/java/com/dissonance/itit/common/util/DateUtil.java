package com.dissonance.itit.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;

public class DateUtil {
	public static LocalDate stringToDate(String dateString) {
		if (dateString == null) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
		try {
			return LocalDate.parse(dateString, formatter);
		} catch (DateTimeParseException e) {
			throw new CustomException(ErrorCode.INVALID_DATE_FORMAT);
		}
	}

	public static String formatPeriod(LocalDate startDate, LocalDate endDate) {
		if (startDate == null && endDate == null)
			return null;
		return formatDate(startDate) + " ~ " + formatDate(endDate);
	}

	public static String formatDate(LocalDate date) {
		if (date == null)
			return "";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
		return date.format(formatter);
	}
}
