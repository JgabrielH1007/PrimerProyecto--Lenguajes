/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import Backend.CSS.TokensCSS;
import Backend.HTML.TokensHTML;
import Backend.JS.TokensJS;
import Exceptions.ExceptionToken;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gabrielh
 */
public class Optimizador {

    private List<TokensHTML> listaTokensHtml;
    private List<TokensCSS> listaTokensCss;
    private List<TokensJS> listaTokensJS;
    private TokenDeEstado tokenE = new TokenDeEstado();
    private String html;
    private String css;
    private String js;
    private List<String> htmlList = new ArrayList<>();
    private List<String> cssList = new ArrayList<>();
    private List<String> jsList = new ArrayList<>();

    public Optimizador() {
        listaTokensHtml = new ArrayList<>();
        listaTokensCss = new ArrayList<>();
        listaTokensJS = new ArrayList<>();

    }

    public Map<String, List<String>> optimizarTexto(String contenido) throws ExceptionToken {
        // Inicializar las listas
        htmlList.clear();
        cssList.clear();
        jsList.clear();

        String[] lineas = contenido.split("\n");
        StringBuilder contenidoActual = new StringBuilder();
        String tokenActual = "";

        for (String linea : lineas) {
            String lineaSinEspacios = linea.trim();

            if (linea.contains(tokenE.getTOKENHTML())) {
                enviarAOptimizar(tokenActual, contenidoActual.toString());
                contenidoActual.setLength(0);
                tokenActual = tokenE.getTOKENHTML();
            } else if (linea.contains(tokenE.getTOKENCSS())) {
                enviarAOptimizar(tokenActual, contenidoActual.toString());
                contenidoActual.setLength(0);
                tokenActual = tokenE.getTOKENCSS();
            } else if (linea.contains(tokenE.getTOKENJAVASCRIPT())) {
                enviarAOptimizar(tokenActual, contenidoActual.toString());
                contenidoActual.setLength(0);
                tokenActual = tokenE.getTOKENJAVASCRIPT();
            } else {
                // Si no se encuentra un nuevo token, agregar la línea al contenido actual
                contenidoActual.append(linea).append("\n");
            }
        }
        // Optimizar el último bloque si existe
        enviarAOptimizar(tokenActual, contenidoActual.toString());

        // Crear un mapa para retornar las listas con el contenido acumulado
        Map<String, List<String>> resultado = new HashMap<>();
        resultado.put("html", htmlList);
        resultado.put("css", cssList);
        resultado.put("js", jsList);

        return resultado;
    }

    private void enviarAOptimizar(String token, String contenido) throws ExceptionToken {
        if (contenido.isEmpty()) {
            return; // No enviar si el contenido está vacío
        }
        switch (token) {
            case ">>[html]":
                htmlList.add(optimizarHTML(contenido)); // Añadir el contenido optimizado a la lista HTML
                break;
            case ">>[css]":
                cssList.add(optimizarCss(contenido)); // Añadir el contenido optimizado a la lista CSS
                break;
            case ">>[js]":
                jsList.add(optimizarJs(contenido)); // Añadir el contenido optimizado a la lista JS
                break;
            default:
                break;
        }
    }

    // Método principal para optimizar el contenido HTML
    public String optimizarHTML(String contenido) throws ExceptionToken {
        List<String> tokensBorrados = new ArrayList<>();
        StringBuilder contenidoOptimizado = new StringBuilder(); // Acumular el contenido optimizado
        String tokenEstado = ">>[html]";
        String[] lineas = contenido.split("\n");

        // Agregar siempre el token de estado al inicio del contenido optimizado
        contenidoOptimizado.append(tokenEstado).append("\n");

        for (String linea : lineas) {
            String lineaTrim = linea.trim(); // Remover espacios al inicio y final para identificar mejor

            // Preservar el token de estado si es encontrado
            if (lineaTrim.equals(tokenEstado)) {
                continue; // No procesar más esta línea, simplemente agregarla
            }

            if (lineaTrim.contains("//")) {
                // Dividir la línea en tokens antes de eliminarla
                guardarTokensSeparados(lineaTrim, tokensBorrados);
                continue; // Omitir esta línea y pasar a la siguiente
            }

            if (!lineaTrim.isEmpty()) {
                contenidoOptimizado.append(linea).append("\n"); // Conservar la estructura original (indentación)
            }
        }

        for (String tokenBorrado : tokensBorrados) {
            TokensHTML tokenHTML = new TokensHTML(); // Crear un nuevo objeto TokensHTML para cada token
            if (tokenHTML.esTokenValido(tokenBorrado)) {
                tokenHTML.setTexto(tokenBorrado);
                tokenHTML.setTipo("Token Html");
                tokenHTML.setExpresionRegular(tokenBorrado);
                listaTokensHtml.add(tokenHTML);
            } else if (tokenHTML.esComentario(tokenBorrado)) {
                tokenHTML.setTexto(tokenBorrado);
                tokenHTML.setTipo("Comentario");
                tokenHTML.setExpresionRegular("// [a-zA-Z]|[0-9]|[.]");
                listaTokensHtml.add(tokenHTML);
            } else if (tokenHTML.esTextoValido(tokenBorrado)) {
                tokenHTML.setTexto(tokenBorrado);
                tokenHTML.setTipo("Texto");
                listaTokensHtml.add(tokenHTML);
            } else if (!tokenBorrado.isEmpty()) {
                tokenHTML.setTexto(tokenBorrado);
                tokenHTML.setTipo("Texto");
                tokenHTML.setExpresionRegular("[a-zA-Z0-9\\\\s\\\\S]*");
                listaTokensHtml.add(tokenHTML);
            } // Agregarlo a la lista de tokens eliminados
        }

        // Imprimir el contenido de la lista de tokens borrados
        for (TokensHTML token : listaTokensHtml) {
            System.out.println("Token borrado: " + token.getTexto() + token.getTipo());
        }

        // Retornar el contenido optimizado como String
        return contenidoOptimizado.toString();
    }

    private void guardarTokensSeparados(String linea, List<String> tokensBorrados) {
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroDeEtiqueta = false;
        boolean dentroDeComentario = false;

        for (int i = 0; i < linea.length(); i++) {
            char caracterActual = linea.charAt(i);

            if (!dentroDeEtiqueta && caracterActual == '/' && i + 1 < linea.length() && linea.charAt(i + 1) == '/') {
                if (tokenActual.length() > 0) {
                    tokensBorrados.add(tokenActual.toString().trim()); // Guardar el contenido antes del comentario
                    tokenActual.setLength(0);
                }
                dentroDeComentario = true;
            }

            if (dentroDeComentario) {
                tokenActual.append(caracterActual);
                continue;
            }

            if (caracterActual == '<') {
                dentroDeEtiqueta = true;
                if (tokenActual.length() > 0) {
                    tokensBorrados.add(tokenActual.toString().trim()); // Guardar el contenido de texto antes de la etiqueta
                    tokenActual.setLength(0);
                }
            } else if (caracterActual == '>') {
                dentroDeEtiqueta = false;
                tokenActual.append(caracterActual);
                tokensBorrados.add(tokenActual.toString().trim()); // Guardar la etiqueta completa
                tokenActual.setLength(0);
                continue;
            }

            // Agregar caracteres al token actual
            tokenActual.append(caracterActual);
        }

        if (tokenActual.length() > 0) {
            tokensBorrados.add(tokenActual.toString().trim());
        }
    }

    public String optimizarCss(String contenido) throws ExceptionToken {
        List<String> tokensBorrados = new ArrayList<>();
        StringBuilder contenidoOptimizado = new StringBuilder(); // Acumular el contenido optimizado
        String tokenEstado = ">>[css]";
        String[] lineas = contenido.split("\n");

        // Agregar siempre el token de estado al inicio del contenido optimizado
        contenidoOptimizado.append(tokenEstado).append("\n");

        for (String linea : lineas) {
            String lineaTrim = linea.trim(); // Remover espacios al inicio y final para identificar mejor

            // Saltar el token de estado si se encuentra en las líneas
            if (lineaTrim.equals(tokenEstado)) {
                continue; // No procesar más esta línea, simplemente ignorarla
            }

            // Manejo de comentarios CSS con //
            if (lineaTrim.contains("//")) {
                // Dividir la línea en tokens antes de eliminarla
                guardarTokensSeparadosCSS(lineaTrim, tokensBorrados);
                continue; // Omitir esta línea y pasar a la siguiente
            }

            // Si la línea no está vacía, agregarla al contenido optimizado
            if (!lineaTrim.isEmpty()) {
                contenidoOptimizado.append(linea).append("\n"); // Conservar la estructura original (indentación)
            }
        }

        // Procesar los tokens eliminados y clasificarlos
        for (String tokenBorrado : tokensBorrados) {
            TokensCSS tokenCSS = new TokensCSS(); // Crear un nuevo objeto TokensCSS para cada token
            char caracterDigito = tokenBorrado.charAt(0); // Obtener el primer carácter para detectar dígitos
            try {
                // Intentar convertir el token a un entero
                int numeroEntero = Integer.parseInt(tokenBorrado);
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular("[0-9]+");
                tokenCSS.setTipo("Entero");
                listaTokensCss.add(tokenCSS);
            } catch (NumberFormatException e1) {
                try {
                    // Intentar convertir el token a un decimal
                    float numeroDecimal = Float.parseFloat(tokenBorrado);
                    tokenCSS.setTexto(tokenBorrado);
                    tokenCSS.setExpresionRegular("[0-9]+.[0-9]+");
                    tokenCSS.setTipo("Decimal");
                    listaTokensCss.add(tokenCSS);
                } catch (NumberFormatException e2) {
                }
            }

            // Resto del código para verificar otros tipos de tokens
            if (tokenCSS.esEtiqueta(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular(tokenBorrado);
                tokenCSS.setTipo("Etiqueta");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esClase(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular(".[a-z]+[0-9]*(-([a-z]|[0-9])+)*");
                tokenCSS.setTipo("De clase");
                listaTokensCss.add(tokenCSS);
            } else if (tokenBorrado.equals(tokenCSS.getUNIVERSAL())) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular(tokenBorrado);
                tokenCSS.setTipo("Universal");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esId(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular("#[a-z]+[0-9]*(-([a-z]|[0-9])+)*");
                tokenCSS.setTipo("Id");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esRegla(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular(tokenBorrado);
                tokenCSS.setTipo("Regla");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esColor(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular("#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})");
                tokenCSS.setTipo("Color");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esCadena(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular("'([^'\\\\]*(\\\\.[^'\\\\]*)*)'");
                tokenCSS.setTipo("Cadena");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esIdentificador(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular("[a-z]+[0-9]*(-([a-z]|[0-9])+)*");
                tokenCSS.setTipo("Identificador");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esCombinador(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular(tokenBorrado);
                tokenCSS.setTipo("Combinador");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esOtro(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular(tokenBorrado);
                tokenCSS.setTipo("Otro");
                listaTokensCss.add(tokenCSS);
            } else if (tokenCSS.esComentario(tokenBorrado)) {
                tokenCSS.setTexto(tokenBorrado);
                tokenCSS.setExpresionRegular("//[a-zA-Z]|[0-9]|[.]");
                tokenCSS.setTipo("Comentario");
                listaTokensCss.add(tokenCSS);
            }
        }

        // Imprimir los tokens borrados
        for (TokensCSS token : listaTokensCss) {
            System.out.println("Token borrado: " + token.getTexto() + " (" + token.getTipo() + ")");
        }

        return contenidoOptimizado.toString();
    }

    private void guardarTokensSeparadosCSS(String linea, List<String> tokensBorrados) {
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroDeComentario = false;

        for (int i = 0; i < linea.length(); i++) {
            char caracterActual = linea.charAt(i);

            // Detectar el inicio de un comentario y guardar el comentario completo
            if (!dentroDeComentario && caracterActual == '/' && i + 1 < linea.length() && linea.charAt(i + 1) == '/') {
                dentroDeComentario = true; // Empezamos a procesar el comentario
                if (tokenActual.length() > 0) {
                    tokensBorrados.add(tokenActual.toString().trim());
                    tokenActual.setLength(0);
                }
                tokenActual.append("//"); // Agregamos los dos slashes iniciales
                i++; // Saltar el siguiente slash ya que lo hemos procesado
                continue;
            }

            // Si estamos dentro de un comentario, agregar el resto de la línea al token
            if (dentroDeComentario) {
                tokenActual.append(caracterActual);
                if (i == linea.length() - 1) {
                    tokensBorrados.add(tokenActual.toString().trim()); // Guardar el comentario completo
                }
                continue;
            }

            // Separar los tokens por los caracteres especiales
            if (esSeparador(caracterActual)) {
                if (tokenActual.length() > 0) {
                    tokensBorrados.add(tokenActual.toString().trim());
                    tokenActual.setLength(0);
                }
                tokensBorrados.add(String.valueOf(caracterActual)); // Agregar separadores como tokens
            } else {
                tokenActual.append(caracterActual);
            }
        }

        // Guardar el último token, si queda alguno
        if (tokenActual.length() > 0) {
            tokensBorrados.add(tokenActual.toString().trim());
        }
    }

    private boolean esSeparador(char c) {
        // Definir caracteres que actúan como separadores
        return c == ' ' || c == '\n' || c == '\t' || c == ':' || c == ';' || c == '{' || c == '}' || c == '(' || c == ')' || c == ',' || c == '*';
    }

    public String optimizarJs(String contenido) throws ExceptionToken {
        List<String> tokensBorrados = new ArrayList<>();
        StringBuilder contenidoOptimizado = new StringBuilder(); // Acumular el contenido optimizado
        String tokenEstado = ">>[js]";
        String[] lineas = contenido.split("\n");

        // Agregar siempre el token de estado al inicio del contenido optimizado
        contenidoOptimizado.append(tokenEstado).append("\n");

        for (String linea : lineas) {
            String lineaTrim = linea.trim(); // Remover espacios al inicio y final para identificar mejor

            // Saltar el token de estado si se encuentra en las líneas
            if (lineaTrim.equals(tokenEstado)) {
                continue; // No procesar más esta línea, simplemente ignorarla
            }

            // Manejo de comentarios JS con //
            if (lineaTrim.contains("//")) {
                // Dividir la línea en tokens antes de eliminarla
                guardarTokensSeparadosJS(lineaTrim, tokensBorrados);
                continue; // Omitir esta línea y pasar a la siguiente
            }

            // Si la línea no está vacía, agregarla al contenido optimizado
            if (!lineaTrim.isEmpty()) {
                contenidoOptimizado.append(linea).append("\n"); // Conservar la estructura original (indentación)
            }
        }

        // Procesar los tokens eliminados y clasificarlos
        for (String tokenBorrado : tokensBorrados) {
            TokensJS tokenJS = new TokensJS(); // Crear un nuevo objeto TokensJS para cada token

            if (tokenJS.esTokenValido(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular(tokenBorrado);
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esComentario(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular("//[a-zA-Z]|[0-9]|[.]");
                tokenJS.setTipo("Comentario");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esPalabraReservada(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular(tokenBorrado);
                tokenJS.setTipo("Palabra reservada");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.isValidInteger(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular("[0-9]+");
                tokenJS.setTipo("Entero");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.isValidFloat(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular("[0-9]+.[0-9]+");
                tokenJS.setTipo("Decimal");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esCadena(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular(tokenBorrado);
                tokenJS.setTipo("Cadena");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esIdentificadorValido(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular("[a-zA-Z]([a-zA-Z]|[0-9]|[ _ ])*");
                tokenJS.setTipo("Identificador");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esCorchete(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular(tokenBorrado);
                tokenJS.setTipo("Corchete");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esLlave(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular(tokenBorrado);
                tokenJS.setTipo("Llave");
                listaTokensJS.add(tokenJS);
            } else if (tokenJS.esParentesis(tokenBorrado)) {
                tokenJS.setTexto(tokenBorrado);
                tokenJS.setExpresionRegular(tokenBorrado);
                tokenJS.setTipo("Parentesis");
                listaTokensJS.add(tokenJS);
            }
        }

        // Imprimir los tokens borrados para verificar el proceso
        for (TokensJS token : listaTokensJS) {
            System.out.println("Token borrado: " + token.getTexto() + " " + token.getTipo());
        }

        return contenidoOptimizado.toString();
    }

    private void guardarTokensSeparadosJS(String linea, List<String> tokensBorrados) {
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroDeComentario = false;

        for (int i = 0; i < linea.length(); i++) {
            char caracterActual = linea.charAt(i);

            // Detectar el inicio de un comentario y guardar el comentario completo
            if (!dentroDeComentario && caracterActual == '/' && i + 1 < linea.length() && linea.charAt(i + 1) == '/') {
                dentroDeComentario = true; // Empezamos a procesar el comentario
                if (tokenActual.length() > 0) {
                    tokensBorrados.add(tokenActual.toString().trim());
                    tokenActual.setLength(0);
                }
                tokenActual.append("//"); // Agregamos los dos slashes iniciales
                i++; // Saltar el siguiente slash ya que lo hemos procesado
                continue;
            }

            // Si estamos dentro de un comentario, agregar el resto de la línea al token
            if (dentroDeComentario) {
                tokenActual.append(caracterActual);
                if (i == linea.length() - 1) {
                    tokensBorrados.add(tokenActual.toString().trim()); // Guardar el comentario completo
                }
                continue;
            }

            // Separar los tokens por los caracteres especiales
            if (esSeparadorJS(caracterActual)) {
                if (tokenActual.length() > 0) {
                    tokensBorrados.add(tokenActual.toString().trim());
                    tokenActual.setLength(0);
                }
                tokensBorrados.add(String.valueOf(caracterActual)); // Agregar separadores como tokens
            } else {
                tokenActual.append(caracterActual);
            }
        }

        // Guardar el último token, si queda alguno
        if (tokenActual.length() > 0) {
            tokensBorrados.add(tokenActual.toString().trim());
        }
    }

    private boolean esSeparadorJS(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '(' || c == ')'
                || c == '[' || c == ']' || c == '{' || c == '}' || c == ';'
                || c == ',' || c == '+' || c == '-' || c == '*' || c == '/'
                || c == '=' || c == '<' || c == '>' || c == '.';
    }

    public List<TokensHTML> getListaTokensHtml() {
        return listaTokensHtml;
    }

    public List<TokensCSS> getListaTokensCss() {
        return listaTokensCss;
    }

    public List<TokensJS> getListaTokensJS() {
        return listaTokensJS;
    }

}
