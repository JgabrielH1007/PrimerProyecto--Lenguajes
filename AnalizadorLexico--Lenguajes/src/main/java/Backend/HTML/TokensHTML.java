/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.HTML;

/**
 *
 * @author gabrielh
 */
public class TokensHTML {
    private final String APERTURA = "<";
    private final String CIERRE = ">";
    private final String BARRACIERRE = "/";
    private final String[] CONTENIDO_TOKENS = {"principal", "encabezado", "navegacion", "apartado", "listaordenada", 
    "listadesordenada", "itemlista", "anclaje", "contenedor", "seccion", 
    "articulo", "titulo1", "titulo2", "titulo3", "titulo4", "titulo5", 
    "titulo6", "parrafo", "span", "entrada", "formulario", "label", 
    "area", "boton", "piepagina"};
    private final String[] PALABRAS_RESERVADAS = {"class", "=", "href", "onClick", "id", "style", "type", "placeholder", 
    "required", "name"};

    // Método para verificar si un token de cadena es válido
    public boolean tokenCadena(String token) {
        if (token == null || token.length() < 2) {
            return false;  
        }

        if (token.charAt(0) == '\"' && token.charAt(token.length() - 1) == '\"') {
            for (int i = 1; i < token.length() - 1; i++) {
                if (token.charAt(i) == '\"') {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    // Método para verificar si el token es válido
    public boolean esTokenValido(String token) {
        if (token == null || token.length() < 3) {
            return false;
        }

        if (!(token.startsWith(APERTURA) && token.endsWith(CIERRE))) {
            return false;
        }

        String contenido = token.substring(1, token.length() - 1).trim();

        // Si el contenido comienza con '/', verificar si es un token de cierre
        boolean esCierre = contenido.startsWith(BARRACIERRE);
        if (esCierre) {
            contenido = contenido.substring(1).trim();  // Eliminar el '/'
        }

        String[] partes = contenido.split("\\s+");
        
        // Validar que la primera parte del token sea un nombre de contenido válido
        if (!esContenidoValido(partes[0])) {
            return false;
        }

        // Si es un token de cierre (por ejemplo, </contenedor>), la longitud de partes debe ser 1
        if (esCierre && partes.length == 1) {
            return true;
        }

        // Validar las partes adicionales como palabras reservadas y valores de atributos
        for (int i = 1; i < partes.length; i += 3) {
            if (i + 2 >= partes.length) {
                return false;  // Atributos incompletos (ej. falta valor o igual)
            }

            String palabraReservada = partes[i];
            String igual = partes[i + 1];
            String valor = partes[i + 2];

            // Validar palabra reservada, el signo igual y que el valor sea una cadena válida
            if (!esPalabraReservada(palabraReservada) || !igual.equals("=") || !tokenCadena(valor)) {
                return false;
            }
        }

        return true;
    }

    // Verifica si el contenido es válido (si pertenece a CONTENIDO_TOKENS)
    private boolean esContenidoValido(String contenido) {
        for (String token : CONTENIDO_TOKENS) {
            if (token.equals(contenido)) {
                return true;
            }
        }
        return false;
    }

    // Verifica si la palabra es una palabra reservada válida
    private boolean esPalabraReservada(String palabra) {
        for (String reservada : PALABRAS_RESERVADAS) {
            if (reservada.equals(palabra)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean esTextoValido(String texto) {
        if (texto == null || texto.isEmpty()) {
            return false;
        }

        // Verificar que no contenga los caracteres '<' o '>'
        for (char c : texto.toCharArray()) {
            if (c == '<' || c == '>') {
                return false;
            }
        }

        return true;
    }


}

