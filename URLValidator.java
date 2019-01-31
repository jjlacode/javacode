// Programa Java para verificar si una URL es valida usando Apache
// common validator.
import org.apache.commons.validator.routines.UrlValidator;

class URLValidator {
    public static boolean urlValidator(String url)
    {
        /* Obteniendo UrlValidator */
        UrlValidator defaultValidator = new UrlValidator();
        return defaultValidator.isValid(url);
    }

    public static void main(String[] args)
    {
       System.out.println("Introducir web a comprobar");
       String url = System.console().readLine();

        /* validar una url */
        if (urlValidator(url))
            System.out.print("La url dada " + url + " es valida");
        else
            System.out.print("La url dada " + url + " no es valida");
    }
}
