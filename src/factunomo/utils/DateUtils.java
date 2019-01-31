package factunomo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase de utilidades genericas para fechas
 * @author M2DA
 *
 */
public class DateUtils {
	
	/**
	 * Array con los nombres de los meses del año
	 */
	public static final String[] MESES = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
	
	/** Patron de fechas por defecto. */
    public static final String PATTERN_DATE_DEFAULT = "dd-MM-yyyy";
    public static final String PATTERN_DATE_DEFAULT_BARRAS = "dd/MM/yyyy";
    
    /** Patron de fechas por defecto en la BB.DD. */
    public static final String PATTERN_DATE_BBDD = "MM/dd/yyyy";
    
    /** Patron de fechas para obtener el anno actual */
    public static final String PATTERN_DATE_YEAR_CONTADOR = "yyyy";
    
    /** Patron de fechas y horas con segundos por defecto. */
    public static final String PATTERN_DATE_TIME_SECOND_DEFAULT = "dd-MM-yyyy HH:mm:ss";
    
    /** Patron de fechas y horas por defecto. */
    public static final String PATTERN_DATE_TIME_DEFAULT = "dd-MM-yyyy HH:mm";
	
    /** Patron de horas por defecto. */
    public static final String PATTERN_TIME_DEFAULT = "HH:mm";
    
    /** Patron de horas por defecto. */
    public static final String PATTERN_TIME_SECOND = "HH:mm:ss";
   
    
    /**miercoles 8 de junio de 2010 10:35*/
	public static final String PATTERN_DATE_INFORMES = "EEEEEEEEE dd 'de' MMMMMMMMMM 'de' yyyy hh:mm";
	
	 /**miercoles 8 de junio de 2010*/
	public static final String PATTERN_DATE_INFORMES_NOTIME = "EEEEEEEEE dd 'de' MMMMMMMMMM 'de' yyyy";
    
     /** Patron de horas por defecto. */
    public static final String PATTERN_DATE_LONGO = "dd MMMM yyyy";
    
    /** Patron de horas por defecto. */
    public static final String PATTERN_DATE_WEEK = "dd EEE";
    
    /** Patron de horas por defecto. */
    public static final String PATTERN_DATE_WEEK_LONGO = "EEEE";
    
    /**8 junio 2010*/
	public static final String PATTERN_DATE_MES = "dd MMMMMMMMMM yyyy";
    
	/**
     * Convierte una fecha a String con el formato de fecha por defecto.
     * @param date Fecha a convertir.
     * @return Un String con la representacion de la fecha con el formato por defecto.
     */
    public static String dateToString(Date date) {
        return dateToString(date, PATTERN_DATE_DEFAULT);
    }
    
    /**
     * Convierte una fecha a String con el formato de fecha especificado en pattern.
     * @param date Fecha a convertir.
     * @param pattern Patron a utilizar. Si este es nulo o vacio se le asigna
     *        el patron por defecto.
     * @return Un String con la representacion de la fecha con el formato pasado.
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (pattern == null || pattern.length() == 0) {
            pattern = PATTERN_DATE_DEFAULT;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
    
    
    
    /**
     * Convierte una fecha de BB.DD a String con el formato de fecha por defecto.
     * @param date Fecha de BB.DD (java.sql.Date) a convertir.
     * @param pattern Patron a utilizar. Si este es nulo o vacio se le asigna el patron por defecto.
     * @return Un String con la representacion de la fecha de la BB.DD. con el formato por defecto.
     */
    public static Date stringToDate(String date) throws ParseException {
        return stringToDate(date, PATTERN_DATE_DEFAULT);
    }
    
    
    
    /**
     * Convierte una cadena que almacena una fecha con el formato por defecto
     * a una fecha.
     * @param date Cadena que almacena la fecha a convertir.
     * @param pattern Patron a utilizar. Si este es nulo o vacio se le asigna el patron por defecto.
     * @return Una fecha (Date) de la cadena pasada con el formato por defecto.
     */
    public static Date stringToDate(String date, String pattern) throws ParseException {
        if (date == null || date.length() == 0) {
            return null;
        }
        if (pattern == null || pattern.length() == 0) {
            pattern = PATTERN_DATE_DEFAULT;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(date);
    }
 
    
    /**
     * Convierte una fecha que almacena una fecha con el formato por defecto
     * a una fecha.
     * @param date Cadena que almacena la fecha a convertir.
     * @param pattern Patron a utilizar. Si este es nulo o vacio se le asigna el patron por defecto.
     * @return Una fecha (Date) de la cadena pasada con el formato por defecto.
     */   
    public static Date dateToDateTime(Date date) throws ParseException {
    	if (date == null) {
            return null;
        }
    	Calendar calendario = new GregorianCalendar();
    	calendario.setTime(date);
    	calendario.add(Calendar.HOUR, 24);
    	return calendario.getTime();
    }
    
    
    /**
     * Funcion que devuelve el dia del mes de la fecha que se pasa por parametro.
     * Si la fecha es nula, se devolvera cadena vacia.
     * @param date
     * @return
     */
    public static String getDia(Date date){
    	String dia = "";
    	if(date != null) {
    		Calendar c = Calendar.getInstance();
    		c.setTime(date);
    		dia = ""+c.get(Calendar.DAY_OF_MONTH);
    	}
    	return dia;
    }
    
    
    /**
     * Funcion que devuelve el mes, en numero, de la fecha que se pasa por parametro.
     * Si la fecha es nula, se devolvera cadena vacia.	
     * @param date
     * @return
     */
    public static String getMes(Date date){
    	String mes = "";
    	if(date != null) {
    		Calendar c = Calendar.getInstance();
    		c.setTime(date);
    		mes = ""+(c.get(Calendar.MONTH)+1);
    	}
    	return mes;
    }
    
    
    /**
     * Funcion que devuelve el nombre del mes de la fecha que se pasa por parametro.
     * Si la fecha es nula, se devolvera cadena vacia.
     * @param date
     * @return
     */
    public static String getMesToString(Date date){
    	String mes = "";
    	if(date != null) {
    		Calendar c = Calendar.getInstance();
    		c.setTime(date);
    		int pos = c.get(Calendar.MONTH);
    		return MESES[pos];
    	}
    	return mes;
    }
    
    
    /**
     * Funcion que devuelve el año de la fecha que se pasa por parametro.
     * Si la fecha es nula, se devolvera cadena vacia.
     * @param date
     * @return
     */
    public static String getAnio(Date date){
    	String anio = "";
    	if(date != null) {
    		Calendar c = Calendar.getInstance();
    		c.setTime(date);
    		anio = ""+c.get(Calendar.YEAR);
    	}
    	return anio;
    }
    
    /**
     * Funci—n que cambia de formato la fecha pasada como par‡metro
     * 
     * @param date
     * @param formatDest
     * @param formatOrig
     * @return
     * @throws ParseException
     */
    public static String changeDateFormatString(String date, String formatDest, String formatOrig) throws ParseException{
    	return dateToString(stringToDate(date, formatOrig), formatDest);
    }
    
    
    /**
     * Funcion que incrementa 1 dia a la fecha que se pasa por parametro
     * @param fecha
     * @return Devuelve la fecha incrementada 1 dia.
     */
    public static Date incrementaDia(Date fecha){
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		calendario.add(Calendar.DATE, 1);
		return calendario.getTime();
	}
    
    
    /**
     * Funcion que devuelve el numero de dias entre dos fechas.
     * Si devuelve negativo es que la primera fecha es menor que la segunda, si devuelve
     * 0 es que son iguales y si devuelve >0 es que la primera fecha es mayor que
     * la segunda.
     * @param fecha1
     * @param fecha2
     * @return
     */
    public static long compareDate(Date fecha1,Date fecha2) {
    	if(fecha1 != null && fecha2 != null){
	    	long dif = fecha1.getTime() - fecha2.getTime();
	    	long res = dif / 86400000L;
	    	System.out.println("Dias entre fechas: " + res );
	    	return res;
    	}
    	return -1;
    }
    
}
