package factunomo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public final class StringUtil {
    
    /** Contructor privado para evitar multiples instancias de la clase. */
    private StringUtil() {
    }
    
    /**
     * Comprueba si una cadena es nula o cadena vacia.
     * @param value Cadena a comprobar.
     * @return true si la cadena es nula o vacia, false en caso contrario.
     */
    public static final boolean isNullOrBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
    
    /**
     * Comprueba si una cadena es nula y la convierte a cadena vacia.
     * @param value Cadena a convertir.
     * @return Cadena vacia si el valor pasado es null, sino el valor pasado..
     */
    public static final String nullToString(String value) {
        return (value == null ? "" : value);
    }
    
    /**
     * Comprueba si una cadena es nula y la convierte a cadena vacia.
     * @param value Cadena a convertir.
     * @return Cadena vacia si el valor pasado es null, sino el valor pasado en mayúsculas.
     */
    public static final String nullToStringUpperCase(String value) {
        return (value == null ? "" : value.toUpperCase());
    }
    
    
    public static final String recortaTexto(String value,int tam) {
    	String resultado = value;
    	if(!isNullOrBlank(resultado)){
    		resultado = resultado.replaceAll(">","&gt;");
    		resultado = resultado.replaceAll("<","&lt;");
    		System.out.println("*******************************************");
    		System.out.println(resultado);
    		System.out.println("*******************************************");
    	} else{
    		System.out.println("2*******************************************");
    		System.out.println(resultado);
    		System.out.println("2*******************************************");
    		resultado = "";
    	}
        if(resultado.length() > tam){
        	String aux = resultado.substring(0,tam) + " ...";
        	System.out.println("*******************************************");
    		System.out.println(aux);
    		System.out.println("*******************************************");
        	return aux;
        }
        return nullToString(resultado);
    }
    
    
    public static final String eliminaHTML(String value) {
    	String resultado = value;
    	if(!isNullOrBlank(resultado)){
    		resultado = resultado.replaceAll(">","&gt;");
    		resultado = resultado.replaceAll("<","&lt;");
    		resultado = resultado.replaceAll("'","&rdquo;");
    		resultado = resultado.replaceAll("\"","&rdquo;");
    	} else{
    		resultado = "";
    	}
    	return resultado;
    }
    
    
    public static final String rellenarIzquierda(String cadena, String caracter, int longitud) {
        String resultado = cadena;
        if (cadena != null) {
            for (int i = cadena.length(); i < longitud; i++) {
                resultado = caracter + resultado;
            }
        }
        return resultado;
    }
    
    
    
    public static final String toUpperCase(String value) {
    	if(value != null){
    		return value.trim().toUpperCase();
    	}
    	return "";
    }
    
    
    public static final String toLowerCase(String value) {
    	if(value != null){
    		return value.trim().toLowerCase();
    	}
    	return "";
    }
    
    
    
    public static String getContenidoFichero(File f){
    	String resultado = "";
    	try{
	    	if(f != null){
	    		BufferedReader bf = new BufferedReader(new FileReader(f));
	    		String cadena = "";
	    		while ((cadena = bf.readLine()) != null) {
	    			resultado += cadena +"\n";
	    		}
	            bf.close();
	    	}
    	} catch(Exception e){}
    	
    	return resultado;
    }
    
}