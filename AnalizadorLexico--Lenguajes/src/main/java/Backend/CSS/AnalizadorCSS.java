/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.CSS;

import Backend.HTML.TokensHTML;
import Backend.TokenErrado;
import java.util.List;

/**
 *
 * @author gabrielh
 */
public class AnalizadorCSS {


    public void analizarCSS(String contenido) {
        TokensCSS tokenVerificador = new TokensCSS(); // Instancia para verificar los tokens

        StringBuilder tokenActual = new StringBuilder(); // Construcción del token actual
        for (int i = 0; i < contenido.length(); i++) {
            char caracter = contenido.charAt(i);

            // Verificar si el carácter es un separador o delimitador
            if (esSeparador(caracter)) {
                // Si tenemos un token formado antes del separador, procesarlo
                if (tokenActual.length() > 0) {
                    procesarToken(tokenActual.toString(), tokenVerificador);
                    tokenActual.setLength(0); // Limpiar el token actual
                }

                // Procesar el separador como un token individual si es relevante
                if (caracter != ' ' && caracter != '\n' && caracter != '\t') {
                    procesarToken(String.valueOf(caracter), tokenVerificador);
                }
            } else {
                // Continuar construyendo el token actual
                tokenActual.append(caracter);
            }
        }

        // Procesar el último token si existe
        if (tokenActual.length() > 0) {
            procesarToken(tokenActual.toString(), tokenVerificador);
        }
    }

    private boolean esSeparador(char c) {
        // Definir caracteres que actúan como separadores
        return c == ' ' || c == '\n' || c == '\t' || c == ':' || c == ';' || c == '{' || c == '}' || c == '(' || c == ')' || c == ',' || c == '*';
    }

    private void procesarToken(String token, TokensCSS tokenCSS) {
        token = token.trim();  // Eliminar espacios en blanco alrededor del token
        char caracterDigito = token.charAt(0); 
        // Verificar el tipo de token y clasificarlo
        if (tokenCSS.esEtiqueta(token)) {
            System.out.println("Etiqueta válida: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esClase(token)) {
            System.out.println("Clase válida: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esId(token)) {
            System.out.println("ID válido: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esRegla(token)) {
            System.out.println("Regla válida: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esColor(token)) {
            System.out.println("Color válido: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esCadena(token)) {
            System.out.println("Cadena válida: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esIdentificador(token)) {
            System.out.println("Identificador válido: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esCombinador(token)) {
            System.out.println("Combinador válido: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.esOtro(token)) {
            System.out.println("Otro token válido: " + token);
            tokenCSS.setTexto(token);
        } else if (tokenCSS.isDigit(caracterDigito)) {
            System.out.println("Digito token válido: " + caracterDigito);
            tokenCSS.setTexto(token);
        } else {
            System.out.println("Token inválido: " + token);
            TokenErrado tokenIn = new TokenErrado();
                tokenIn.setTexto(token);
                tokenIn.setLenguaje("CSS");
        }
    }
          
}
