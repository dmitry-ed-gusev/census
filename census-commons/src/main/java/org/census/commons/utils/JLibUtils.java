package org.census.commons.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Gusev Dmitry (Гусев Дмитрий)
 * @version 1.0 (DATE: 22.06.11)
*/

public final class JLibUtils
 {
  public static enum DatePeriod {YEAR, MONTH, DAY}

  private static Logger logger = Logger.getLogger(JLibUtils.class.getName());

  //
  private JLibUtils() {}

  /**Преобразование строковой даты в строку определенного формата
     * @param date - дата в виде строки
     * @param inPattern - формат даты dateTime, например: ddMMyy
     * @param outPattern - шаблон даны для преобразования, например: dd.MM.yyyy
     * @param defaultValue - возвращаемое значение по умолчанию, если не удастся преобразовать дату
     * @return - строковая дата отформатированная по шаблону*/
    public static String dateStrToPattern(String date, String inPattern, String outPattern, String defaultValue){

        String returnDate = defaultValue;

        if(date != null && !date.trim().equals("")){
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(inPattern);
            java.text.SimpleDateFormat df2 = new java.text.SimpleDateFormat(outPattern);

            try {
                returnDate = df2.format(df.parse(date));

            } catch (Exception e) {e.getMessage();}
        }

        return returnDate;
    }


   /**Преобразование даты (java.sql.Date) в строку определенного формата
     * @param date - дата
     * @param outPattern - шаблон даны для преобразования, например: dd.MM.yyyy
     * @param defaultValue - возвращаемое значение по умолчанию, если не удастся преобразовать дату
     * @return - строковая дата отформатированная по шаблону*/
    public static String dateToPattern(java.sql.Date date, String outPattern, String defaultValue){

        String returnDate = defaultValue;

        if(date != null){

            java.text.SimpleDateFormat df2 = new java.text.SimpleDateFormat(outPattern);

            try {
                returnDate = df2.format(date);

            } catch (Exception e) {e.getMessage();}
        }

        return returnDate;
    }

    /**Преобразование даты (java.util.Date) в строку определенного формата
     * @param date - дата
     * @param outPattern - шаблон даны для преобразования, например: dd.MM.yyyy
     * @param defaultValue - возвращаемое значение по умолчанию, если не удастся преобразовать дату
     * @return - строковая дата отформатированная по шаблону*/
    public static String dateToPattern(java.util.Date date, String outPattern, String defaultValue){

        String returnDate = defaultValue;

        if(date != null){

            java.text.SimpleDateFormat df2 = new java.text.SimpleDateFormat(outPattern);

            try {
                returnDate = df2.format(date);

            } catch (Exception e) {e.getMessage();}
        }

        return returnDate;
    }

   /**Преобразование строковой даты  в дату типа java.util.Date, с возможностью изменения на N-ое количество дней/месяцев/лет
     * @param date - дата
     * @param inPattern - формат даты date, например: ddMMyy
     * @param period - количество дней/месяцев/лет на которые требуется перенести дату
     * @param typePeriod - тип периода: день/месяц/год
     * @return -  дата типа java.util.Date*/
    public static Date dateToPeriod(String date, String inPattern, int period, DatePeriod typePeriod) throws ParseException
     {return JLibUtils.dateToPeriod(new SimpleDateFormat(inPattern).parse(date), period, typePeriod);}

    /**Изменение на N-ое количество дней/месяцев/лет даты типа java.util.Date
     * @param date - дата
     * @param period - количество дней/месяцев/лет на которые требуется перенести дату
     * @param typePeriod - тип периода: день/месяц/год
     * @return -  дата типа java.util.Date*/
    public static Date dateToPeriod(Date date, int period, DatePeriod typePeriod){

        Date returnDate = null;

        if(date != null){
                Calendar cal = new GregorianCalendar();
                cal.setTime(date);

                if(DatePeriod.YEAR.equals(typePeriod))       {cal.add(Calendar.YEAR, period);}
                else if(DatePeriod.MONTH.equals(typePeriod)) {cal.add(Calendar.MONTH, period);}
                else if(DatePeriod.DAY.equals(typePeriod))   {cal.add(Calendar.DATE, period);}

                returnDate = cal.getTime();

        }
        else {logger.warn("Input data is NULL!");}
        return returnDate;
    }
 }