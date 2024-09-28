/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.CSS;

/**
 *
 * @author gabrielh
 */
public class TokensCSS {
    private String token;
    private boolean tokenAceptado;
    private final String[] ETIQUETAS = {"body","header","main","nav","aside","div","ul","ol","li","a","h1"
    ,"h2","h3","h4","h5","h6","p","span","label","textarea","button","section","article","footer"};
    private final String[] COMBINADORES = {">","+"," "};
    private final String[] REGLAS = {"color","background-color","background","font-size","font-weight","font-family","font-align","width","height","min-width",
    "min-height","max-width","max-height","display","inline","block","inline-block","flex","grid","none","margin","border","padding",
    "content","border-color","border-style","border-width","border-top","border-bottom","border-left","border-right","box-sizing","border-box",
    "position","static","relative","absolute","sticky","fixed","top","bottom","left","right","z-index","justify-content","align-items","border-radius","auto",
    "float","list-style","text-align","box-shadow"};
    private final String[] OTROS = {"px","%","rem","em","vw","vh",":hover",":active",":not()",":nth-child()","odd","even","::before","::after",
    ":",";",",","(",")"};
    
    
    public boolean isTokenAceptado() {
        return tokenAceptado;
    }

    public void setTokenAceptado(boolean tokenAceptado) {
        this.tokenAceptado = tokenAceptado;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean tokenClase(String token) {
        if (token == null || token.isEmpty() || token.charAt(0) != '.') {
            return false;  // Si el token es nulo, vacío o no empieza con punto, es inválido
        }

        int i = 1; // Comenzar a revisar después del punto

        if (i >= token.length() || !isLowerCaseLetter(token.charAt(i))) {
            return false;
        }
        i++;

        // Recorrer el resto del token
        while (i < token.length()) {
            char c = token.charAt(i);

            if (isLowerCaseLetter(c) || isDigit(c) || c == '-') {
                // Si es un guion, verificar que no esté al inicio, al final, o seguido de otro guion
                if (c == '-') {
                    if (i == 1 || i == token.length() - 1 || token.charAt(i + 1) == '-') {
                        return false;  // Guiones consecutivos o en posiciones no válidas
                    }
                }
                i++;
            } else {
                return false;  // Caracter inválido
            }
        }

        return true;  // Si pasa todas las verificaciones, es válido
    }

    private boolean isLowerCaseLetter(char c) {
        return c >= 'a' && c <= 'z';
    }

    // Método auxiliar para verificar si un carácter es un dígito [0-9]
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    
    public boolean tokenId(String token) {
        if (token == null || token.isEmpty() || token.charAt(0) != '#') {
            return false;  // Si el token es nulo, vacío o no empieza con '#', es inválido
        }

        int i = 1; // Comenzar a revisar después del '#'

        // Verificar que el primer carácter después del '#' sea una letra minúscula
        if (i >= token.length() || !isLowerCaseLetter(token.charAt(i))) {
            return false;
        }
        i++;

        // Recorrer el resto del token
        while (i < token.length()) {
            char c = token.charAt(i);

            // Aceptar letras minúsculas, números y guiones
            if (isLowerCaseLetter(c) || isDigit(c) || c == '-') {
                // Si es un guion, verificar que no esté al inicio, al final, o seguido de otro guion
                if (c == '-') {
                    if (i == 1 || i == token.length() - 1 || token.charAt(i + 1) == '-') {
                        return false;  // Guiones consecutivos o en posiciones no válidas
                    }
                }
                i++;
            } else {
                return false;  // Caracter inválido
            }
        }

        return true; 
    }
    
    public boolean tokenCadena(String token) {
        if (token == null || token.length() < 2) {
            return false;  
        }

        if (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'') {
            // Validar que no haya comillas simples dentro del contenido de la cadena
            for (int i = 1; i < token.length() - 1; i++) {
                if (token.charAt(i) == '\'') {
                    return false;  // Comillas simples dentro del contenido no son permitidas
                }
            }
            return true;  // Token válido si pasa todas las verificaciones
        }

        return false;  // Si no empieza o no termina con comillas simples
    }
    
    public boolean tokenColor(String token) {
        if (token == null || token.isEmpty()) {
            return false;  // Si el token es nulo o vacío, es inválido
        }

        // Verificar si es un color hexadecimal
        if (token.charAt(0) == '#') {
            return isValidHexColor(token);
        }

        // Verificar si es un color rgba
        if (token.startsWith("rgba(") && token.endsWith(")")) {
            return isValidRgbaColor(token);
        }

        return false;  // Si no es ninguno de los formatos, es inválido
    }

    // Método para verificar si el token es un color hexadecimal válido
    private boolean isValidHexColor(String token) {
        int length = token.length();

        // El color hexadecimal debe tener 4 (#fff) o 7 caracteres (#ffffff)
        if (length == 4 || length == 7) {
            for (int i = 1; i < length; i++) {
                if (!isHexDigit(token.charAt(i))) {
                    return false;  // Caracter inválido
                }
            }
            return true;  // Es un color hexadecimal válido
        }

        return false; 
    }

    // Método auxiliar para verificar si un carácter es un dígito hexadecimal [0-9, a-f, A-F]
    private boolean isHexDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    // Método para verificar si el token es un color rgba válido
    private boolean isValidRgbaColor(String token) {
        // Remover 'rgba(' al inicio y ')' al final
        String params = token.substring(5, token.length() - 1);
        String[] parts = params.split(",");

        // Debe haber 3 o 4 parámetros
        if (parts.length != 3 && parts.length != 4) {
            return false;
        }

        // Verificar que los primeros tres valores sean enteros entre 0 y 255
        for (int i = 0; i < 3; i++) {
            if (!isValidInteger(parts[i].trim(), 0, 255)) {
                return false;  // Valor inválido
            }
        }

        // Si hay un cuarto valor, verificar que sea un número entre 0 y 1
        if (parts.length == 4) {
            if (!isValidFloat(parts[3].trim(), 0, 1)) {
                return false;  // Valor alfa inválido
            }
        }

        return true;  // Si todos los valores son válidos
    }

    private boolean isValidInteger(String str, int min, int max) {
        try {
            int value = Integer.parseInt(str);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;  // No es un número entero válido
        }
    }

    private boolean isValidFloat(String str, double min, double max) {
        try {
            double value = Double.parseDouble(str);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;  // No es un número flotante válido
        }
    }
    
    public boolean tokenIdentificador(String token) {
        if (token == null || token.isEmpty()) {
            return false;  // Si el token es nulo o vacío, es inválido
        }

        int i = 0;
        
        if (!isLowerCaseLetter(token.charAt(i))) {
            return false;  // Si no empieza con una letra minúscula, es inválido
        }
        
        while (i < token.length() && isLowerCaseLetter(token.charAt(i))) {
            i++;
        }

        // Ahora pueden seguir dígitos opcionales
        while (i < token.length() && isDigit(token.charAt(i))) {
            i++;
        }

        // Si llegamos al final aquí, el token es válido
        if (i == token.length()) {
            return true;
        }

        while (i < token.length()) {
            if (token.charAt(i) == '-') {
                // Si hay un guion al inicio o al final o guiones consecutivos, es inválido
                if (i == 0 || i == token.length() - 1 || token.charAt(i + 1) == '-') {
                    return false;
                }
                i++;  // Saltamos el guion
            }

            if (i < token.length() && (isLowerCaseLetter(token.charAt(i)) || isDigit(token.charAt(i)))) {
                i++;
            } else {
                return false;  // Si no hay ni letra minúscula ni dígito después del guion, es inválido
            }

            while (i < token.length() && (isLowerCaseLetter(token.charAt(i)) || isDigit(token.charAt(i)))) {
                i++;
            }
        }
        return true;
    }

}



    


