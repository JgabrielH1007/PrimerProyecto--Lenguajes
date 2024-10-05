/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.JS;

/**
 *
 * @author gabrielh
 */
public class AnalizadorJS {

   public void analizar(String contenido) {
    TokensJS tokenJS = new TokensJS();
    StringBuilder tokenActual = new StringBuilder(); // Construcción del token actual
    boolean dentroDeCadena = false; // Indicador de si estamos dentro de una cadena
    char delimitadorDeCadena = '\0'; // Para almacenar el tipo de delimitador de cadena ('"', '\'' o '`')

    for (int i = 0; i < contenido.length(); i++) {
        char caracter = contenido.charAt(i);

        // Verificar si estamos dentro de una cadena
        if (dentroDeCadena) {
            tokenActual.append(caracter);

            // Si encontramos el delimitador de cierre, procesamos el token completo
            if (caracter == delimitadorDeCadena) {
                procesarToken(tokenActual.toString(), tokenJS);
                tokenActual.setLength(0); // Limpiar el token actual
                dentroDeCadena = false; // Salir del estado de cadena
            }
        } else {
            // Verificar si el carácter es un delimitador de cadena
            if (caracter == '"' || caracter == '\'' || caracter == '`') {
                // Procesar el token actual antes de comenzar una nueva cadena
                if (tokenActual.length() > 0) {
                    procesarToken(tokenActual.toString(), tokenJS);
                    tokenActual.setLength(0); // Limpiar el token actual
                }

                // Iniciar una nueva cadena
                dentroDeCadena = true;
                delimitadorDeCadena = caracter;
                tokenActual.append(caracter); // Incluir el delimitador de apertura
            } else if (esSeparador(caracter)) {
                // Procesar el token actual si no está vacío
                if (tokenActual.length() > 0) {
                    procesarToken(tokenActual.toString(), tokenJS);
                    tokenActual.setLength(0); // Limpiar el token actual
                }

                // Procesar el separador si es relevante
                if (caracter != ' ' && caracter != '\n' && caracter != '\t') {
                    procesarToken(String.valueOf(caracter), tokenJS);
                }
            } else {
                // Continuar construyendo el token actual
                tokenActual.append(caracter);
            }
        }
    }

    // Procesar el último token si existe
    if (tokenActual.length() > 0) {
        procesarToken(tokenActual.toString(), tokenJS);
    }
}


    private boolean esSeparador(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '(' || c == ')'
                || c == '[' || c == ']' || c == '{' || c == '}' || c == ';'
                || c == ',' || c == '+' || c == '-' || c == '*' || c == '/'
                || c == '=' || c == '<' || c == '>' || c == '"' || c == '.';
    }

    private void procesarToken(String token, TokensJS tokenJS) {
        if (tokenJS.esTokenValido(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.esPalabraReservada(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.isValidInteger(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.isValidFloat(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.esCadena(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.esIdentificadorValido(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.esCorchete(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.esLlave(token)) {
            System.out.println("válido: " + token);
        } else if (tokenJS.esParentesis(token)) {
            System.out.println("válido: " + token);
        } else {
            System.out.println("Token desconocido: " + token);
        }
    }

    public static void main(String[] args) {
        String contenido = "function showSection(sectionId) {\n"
                + "    const sections = document.querySelectorAll('#main-content > div');\n"
                + "    sections.forEach(section => {\n"
                + "        section.style.display = 'none';\n"
                + "    });\n"
                + "    document.getElementById(sectionId).style.display = 'block';\n"
                + "}";
        AnalizadorJS analizador = new AnalizadorJS();
        analizador.analizar(contenido);
    }

}
