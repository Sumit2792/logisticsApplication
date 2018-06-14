package com.fw.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Date time utility

 */

public class DateTimeUtils {
	
	private final static Logger log = Logger.getLogger(DateTimeUtils.class);
	
	private final static String DEFAULT_DATE_TIME_FORMAT="yyyy-MM-dd hh:mm:ss";
	
	/**
	 * returns LocalDateTime according to zone id.
	 * @param zId
	 * @return LocalDateTime
	 */
	public static LocalDateTime getInstantDateTimeWithZoneId(String zId)
	{
		try {
			
			ZoneId zoneId = ZoneId.of(zId);
			DateTimeFormatter format = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
			ZonedDateTime zoneDateTime = ZonedDateTime.ofInstant(Instant.now(), zoneId);
			String dateTime = zoneDateTime.format(format);
			log.info(" Time Zone: "+zId + " time : "+dateTime);
			return zoneDateTime.toLocalDateTime();
			
		} catch (Exception e) {
			log.error("Error: "+e.getMessage(), e);
			return LocalDateTime.now();
		}
	}
	
	public static LocalDateTime convertDateTimeAsPerTimeZone(String zId, int year, int month, int dayOfMonth, int hour ,int minutes)
	{
		try
		{
			return convertDateTimeAsPerTimeZone(zId, LocalDateTime.of(year, month, dayOfMonth, hour, minutes));
		} catch (Exception e) {
			log.error("Error: "+e.getMessage(), e);
			return LocalDateTime.now();
		}
	}
	/**
	 * convert the input date according to timeZone
	 * @param zId
	 * @param ldt
	 * @return {LocalDateTime}
	 */
	public static LocalDateTime convertDateTimeAsPerTimeZone(String zId, LocalDateTime ldt)
	{
		try{
				ZoneId zoneId = ZoneId.of(zId);
				DateTimeFormatter format = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
				ZonedDateTime zoneDateTime = ZonedDateTime.of(ldt, zoneId);
				String dateTime = zoneDateTime.format(format);
				//log.info(" Time Zone: "+zId + " time : "+dateTime);
				return zoneDateTime.toLocalDateTime();
				
	} catch (Exception e) {
		log.error("Error: "+e.getMessage(), e);
		return LocalDateTime.now();
	}
		
	}
	
	/**
	 * convert the input date according to timeZone
	 * @param zId
	 * @param ldt
	 * @return {LocalDateTime}
	 */
	public static Date convertLocalDateTimeToUtilDate(String zId, LocalDateTime ldt)
	{
		try{
				ZoneId zoneId = ZoneId.of(zId);
				DateTimeFormatter format = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
				ZonedDateTime zoneDateTime = ZonedDateTime.of(ldt, zoneId);
				String dateTime = zoneDateTime.format(format);
				//log.info(" Time Zone: "+zId + " time : "+dateTime);
				return Date.from(zoneDateTime.toInstant());
				
		} catch (Exception e) {
		log.error("Error: "+e.getMessage(), e);
		return new Date();
		}
		
	}
	/**
	 * returns LocalDateTime according to zone id.
	 * @param zId
	 * @return LocalDateTime
	 */
	public static LocalDateTime convertUtilDateToLocalDateTime(String zId , Date dt)
	{
		try {
				
				ZoneId zoneId = ZoneId.of(zId);
				DateTimeFormatter format = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
				ZonedDateTime zoneDateTime = ZonedDateTime.ofInstant(dt.toInstant(), zoneId);
				String dateTime = zoneDateTime.format(format);
				//log.info(" Time Zone: "+zId + " time : "+dateTime);
				
				return zoneDateTime.toLocalDateTime();
			
		} catch (Exception e) {
			log.error("Error: "+e.getMessage(), e);
			return LocalDateTime.now();
		}
	}

	 
}
