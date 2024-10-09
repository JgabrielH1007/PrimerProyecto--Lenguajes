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
import java.util.List;
import java.util.Map;

/**
 *
 * @author gabrielh
 */
public class LectorTexto {

    private List<TokensHTML> listaTokensHtml;
    private List<TokensCSS> listaTokensCss;
    private List<TokensJS> listaTokensJS;
    private List<TokenErrado> listaTokenInvalido;
    private TokenDeEstado tokenE = new TokenDeEstado();
    private String textoCompleto;
    private StringBuilder textoTraducido;
    private String contenidoCSS;
    private String contenidoJS;
    private List<String> htmlList = new ArrayList<>();
    private List<String> cssList = new ArrayList<>();
    private List<String> jsList = new ArrayList<>();

    public LectorTexto() {
        listaTokensHtml = new ArrayList<>();
        listaTokensCss = new ArrayList<>();
        listaTokensJS = new ArrayList<>();
        listaTokenInvalido = new ArrayList<>();
        textoTraducido = new StringBuilder(); // Inicializar el texto traducido
    }

    public void leerTexto(String textoCompleto){
        String[] lineas = textoCompleto.split("\n");
        StringBuilder contenidoActual = new StringBuilder();
        String tokenActual = "";

        for (String linea : lineas) {
            if (linea.contains(tokenE.getTOKENHTML())) {
                enviarAlAnalizador(tokenActual, contenidoActual.toString());
                contenidoActual.setLength(0);
                tokenActual = tokenE.getTOKENHTML();
            } else if (linea.contains(tokenE.getTOKENCSS())) {
                enviarAlAnalizador(tokenActual, contenidoActual.toString());
                contenidoActual.setLength(0);
                tokenActual = tokenE.getTOKENCSS();
            } else if (linea.contains(tokenE.getTOKENJAVASCRIPT())) {
                enviarAlAnalizador(tokenActual, contenidoActual.toString());
                contenidoActual.setLength(0);
                tokenActual = tokenE.getTOKENJAVASCRIPT();
            } else {
                // Si no se encuentra un nuevo token, agregar la línea al contenido actual
                contenidoActual.append(linea).append("\n");
            }
        }

        // Al final, enviar cualquier contenido restante al analizador correspondiente
        enviarAlAnalizador(tokenActual, contenidoActual.toString());
    }

    private void enviarAlAnalizador(String token, String contenido){
        if (contenido.isEmpty()) {
            return; // No enviar si el contenido está vacío
        }
        switch (token) {
            case ">>[html]":
                String html = textoTraducido.append(analizarHtml(contenido)).toString();
                htmlList.add(html);// Acumular el contenido traducido
                break;
            case ">>[css]":
                cssList.add(analizarCSS(contenido));
                break;
            case ">>[js]":
                jsList.add(analizarJS(contenido));
                break;
            default:
                // Si no hay un token válido, no se realiza ninguna acción
                break;
        }
    }

    public String analizarHtml(String contenido){
        // Almacena cada token que se encuentre
        List<String> tokens = new ArrayList<>();
        StringBuilder contenidoTraducido = new StringBuilder(); // Acumular el contenido traducido

        // Variable auxiliar para acumular caracteres y formar un token
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroDeEtiqueta = false;

        // Recorremos cada carácter del contenido
        for (int i = 0; i < contenido.length(); i++) {
            char caracterActual = contenido.charAt(i);

            // Inicia una etiqueta
            if (caracterActual == '<') {
                // Si hay contenido fuera de etiquetas, agregarlo como texto
                if (tokenActual.length() > 0) {
                    tokens.add(tokenActual.toString().trim());
                    tokenActual.setLength(0);
                }
                dentroDeEtiqueta = true;
                tokenActual.append(caracterActual);
            } // Finaliza una etiqueta
            else if (caracterActual == '>') {
                tokenActual.append(caracterActual);
                tokens.add(tokenActual.toString().trim());
                tokenActual.setLength(0);
                dentroDeEtiqueta = false;
            } // Cualquier otro contenido se acumula en el token actual
            else {
                tokenActual.append(caracterActual);
            }
        }

        // Agregar cualquier contenido restante como un token
        if (tokenActual.length() > 0) {
            tokens.add(tokenActual.toString().trim());
        }

        // Procesar cada token y traducir si es necesario
        for (String token : tokens) {
            TokensHTML tokenHTML = new TokensHTML(); // Crear un nuevo objeto TokensHTML para cada token
            if (tokenHTML.esTokenValido(token)) {
                // Si el token es una etiqueta válida, traducirla y agregarla
                String traduccion = traducirToken(token);
                contenidoTraducido.append(traduccion);
                tokenHTML.setTexto(token);
                tokenHTML.setTipo("Token Html");
                tokenHTML.setExpresionRegular(token);
                listaTokensHtml.add(tokenHTML);
            } else if (tokenHTML.esComentario(token)) {
                // Si el token es un comentario, agregarlo como está
                contenidoTraducido.append(token).append("\n");
                tokenHTML.setTexto(token);
                tokenHTML.setTipo("Comentario");
                tokenHTML.setExpresionRegular("// [a-zA-Z]|[0-9]|[.]");
                listaTokensHtml.add(tokenHTML);
            } else if (tokenHTML.esTextoValido(token)) {
                // Si el token es texto fuera de etiquetas, conservarlo tal cual
                contenidoTraducido.append(token).append("\n");
                tokenHTML.setTexto(token);
                tokenHTML.setTipo("Texto");
                tokenHTML.setExpresionRegular("[a-zA-Z0-9\\\\s\\\\S]*");
                listaTokensHtml.add(tokenHTML);
            } else if (!token.isEmpty()) {
                // Si es un token inválido, agregarlo a la lista de tokens errados
                TokenErrado tokenIn = new TokenErrado();
                tokenIn.setTexto(token);
                tokenIn.setLenguaje("Html");
                tokenIn.setLenguajeSugerido("Html");
                listaTokenInvalido.add(tokenIn);
            }
        }

        return contenidoTraducido.toString();
    }

    public String traducirToken(String token) {
        // Remover '<' y '>' para obtener el contenido de la etiqueta
        String contenido = token.substring(1, token.length() - 1).trim();
        boolean esCierre = contenido.startsWith("/"); // Verificar si es una etiqueta de cierre

        if (esCierre) {
            contenido = contenido.substring(1).trim(); // Remover el '/'
        }

        // Separar el nombre de la etiqueta y sus atributos
        String[] partes = contenido.split("\\s+", 2);
        String nombreEtiqueta = partes[0];
        String atributos = partes.length > 1 ? partes[1] : ""; // Los atributos son la segunda parte, si existen

        // Traducir solo el nombre de la etiqueta
        String nombreTraducido = traducirNombreEtiqueta(nombreEtiqueta);

        // Reconstruir el token traducido con atributos, si es una etiqueta de cierre o apertura
        if (esCierre) {
            return "</" + nombreTraducido + ">";
        } else {
            return "<" + nombreTraducido + (atributos.isEmpty() ? "" : " " + atributos) + ">";
        }
    }

    private String traducirNombreEtiqueta(String nombreEtiqueta) {
        switch (nombreEtiqueta) {
            case "principal":
                return "main";
            case "encabezado":
                return "header";
            case "navegacion":
                return "nav";
            case "apartado":
                return "aside";
            case "listaordenada":
                return "ol";
            case "listadesordenada":
                return "ul";
            case "itemlista":
                return "li";
            case "anclaje":
                return "a";
            case "contenedor":
                return "div";
            case "seccion":
                return "section";
            case "articulo":
                return "article";
            case "titulo1":
                return "h1";
            case "titulo2":
                return "h2";
            case "titulo3":
                return "h3";
            case "titulo4":
                return "h4";
            case "titulo5":
                return "h5";
            case "titulo6":
                return "h6";
            case "parrafo":
                return "p";
            case "span":
                return "span";
            case "entrada":
                return "input";
            case "formulario":
                return "form";
            case "label":
                return "label";
            case "area":
                return "textarea";
            case "boton":
                return "button";
            case "piepagina":
                return "footer";
            default:
                return nombreEtiqueta;  // Si no es una etiqueta conocida, devolver el nombre original
        }
    }

    public String analizarCSS(String contenido){
        StringBuilder tokenActual = new StringBuilder(); // Construcción del token actual

        for (int i = 0; i < contenido.length(); i++) {
            char caracter = contenido.charAt(i);

            // Verificar si encontramos el inicio de un comentario de una línea
            if (caracter == '/' && i + 1 < contenido.length() && contenido.charAt(i + 1) == '/') {
                // Limpiar el token actual antes de procesar el comentario
                if (tokenActual.length() > 0) {
                    procesarTokenCSS(tokenActual.toString(), new TokensCSS());
                    tokenActual.setLength(0); // Limpiar el token actual
                }
                // Acumular hasta el final de la línea para el comentario
                while (i < contenido.length() && caracter != '\n') {
                    tokenActual.append(caracter);
                    i++;
                    if (i < contenido.length()) {
                        caracter = contenido.charAt(i);
                    }
                }
                // Procesar el comentario completo como un token
                procesarTokenCSS(tokenActual.toString(), new TokensCSS()); // Procesar el comentario de una línea
                tokenActual.setLength(0); // Limpiar el token actual
                continue; // Continuar al siguiente carácter
            }

            // Verificar si el carácter es un separador o delimitador
            if (esSeparador(caracter)) {
                // Si tenemos un token formado antes del separador, procesarlo
                if (tokenActual.length() > 0) {
                    procesarTokenCSS(tokenActual.toString(), new TokensCSS());
                    tokenActual.setLength(0); // Limpiar el token actual
                }

                // Procesar el separador como un token individual si es relevante
                if (caracter != ' ' && caracter != '\n' && caracter != '\t') {
                    procesarTokenCSS(String.valueOf(caracter), new TokensCSS());
                }
            } else {
                // Continuar construyendo el token actual
                tokenActual.append(caracter);
            }
        }

        // Procesar el último token si existe
        if (tokenActual.length() > 0) {
            procesarTokenCSS(tokenActual.toString(), new TokensCSS());
        }

        return contenido;
    }

    private boolean esSeparador(char c) {
        // Definir caracteres que actúan como separadores
        return c == ' ' || c == '\n' || c == '\t' || c == ':' || c == ';' || c == '{' || c == '}' || c == '(' || c == ')' || c == ',' || c == '*';
    }

    private void procesarTokenCSS(String token, TokensCSS tokenCSS){
        token = token.trim();  // Eliminar espacios en blanco alrededor del token
        char caracterDigito = token.charAt(0);

        // Verificar si es un número (entero o decimal)
        try {
            // Intentar convertir el token a un entero
            int numeroEntero = Integer.parseInt(token);
            System.out.println("Token entero válido: " + numeroEntero);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular("[0-9]+");
            tokenCSS.setTipo("Entero");
            listaTokensCss.add(tokenCSS);
            return; // Salir del método si se ha procesado como entero
        } catch (NumberFormatException e1) {
            try {
                // Intentar convertir el token a un decimal
                float numeroDecimal = Float.parseFloat(token);
                System.out.println("Token decimal válido: " + numeroDecimal);
                tokenCSS.setTexto(token);
                tokenCSS.setExpresionRegular("[0-9]+.[0-9]+");
                tokenCSS.setTipo("Decimal");
                listaTokensCss.add(tokenCSS);
                return; // Salir del método si se ha procesado como decimal
            } catch (NumberFormatException e2) {
            }
        }

        // Resto del código para verificar otros tipos de tokens
        if (tokenCSS.esEtiqueta(token)) {
            System.out.println("Etiqueta válida: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular(token);
            tokenCSS.setTipo("Etiqueta");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esClase(token)) {
            System.out.println("Clase válida: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular(".[a-z]+[0-9]*(-([a-z]|[0-9])+)*");
            tokenCSS.setTipo("De clase");
            listaTokensCss.add(tokenCSS);
        } else if (token.equals(tokenCSS.getUNIVERSAL())) {
            System.out.println("válida: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular(token);
            tokenCSS.setTipo("Universal");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esId(token)) {
            System.out.println("ID válido: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular("#[a-z]+[0-9]*(-([a-z]|[0-9])+)*");
            tokenCSS.setTipo("Id");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esRegla(token)) {
            System.out.println("Regla válida: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular(token);
            tokenCSS.setTipo("Regla");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esColor(token)) {
            System.out.println("Color válido: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular("#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})");
            tokenCSS.setTipo("Color");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esCadena(token)) {
            System.out.println("Cadena válida: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular("'([^'\\\\]*(\\\\.[^'\\\\]*)*)'");
            tokenCSS.setTipo("Cadena");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esIdentificador(token)) {
            System.out.println("Identificador válido: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular("[a-z]+[0-9]*(-([a-z]|[0-9])+)*");
            tokenCSS.setTipo("Identificador");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esCombinador(token)) {
            System.out.println("Combinador válido: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular(token);
            tokenCSS.setTipo("Combinador");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esOtro(token)) {
            System.out.println("Otro token válido: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular(token);
            tokenCSS.setTipo("Otro");
            listaTokensCss.add(tokenCSS);
        } else if (tokenCSS.esComentario(token)) {
            System.out.println("Comentario token válido: " + token);
            tokenCSS.setTexto(token);
            tokenCSS.setExpresionRegular("//[a-zA-Z]|[0-9]|[.]");
            tokenCSS.setTipo("Comentario");
            listaTokensCss.add(tokenCSS);
        } else {
            TokenErrado tokenIn = new TokenErrado();
            tokenIn.setTexto(token);
            tokenIn.setLenguaje("CSS");
            tokenIn.setLenguajeSugerido("CSS");
            listaTokenInvalido.add(tokenIn);
        }
    }

    public String analizarJS(String contenido){

        StringBuilder tokenActual = new StringBuilder(); // Construcción del token actual
        boolean dentroDeCadena = false;
        char delimitadorDeCadena = '\0';

        for (int i = 0; i < contenido.length(); i++) {
            char caracter = contenido.charAt(i);
            TokensJS tokenJS = new TokensJS();
            // Verificar si estamos dentro de una cadena
            if (caracter == '/' && i + 1 < contenido.length() && contenido.charAt(i + 1) == '/') {
                // Limpiar el token actual antes de procesar el comentario
                if (tokenActual.length() > 0) {
                    procesarToken(tokenActual.toString(), new TokensJS());
                    tokenActual.setLength(0); // Limpiar el token actual
                }
                // Acumular hasta el final de la línea para el comentario
                while (i < contenido.length() && caracter != '\n') {
                    tokenActual.append(caracter);
                    i++;
                    if (i < contenido.length()) {
                        caracter = contenido.charAt(i);
                    }
                }
                // Procesar el comentario completo como un token
                procesarToken(tokenActual.toString(), new TokensJS()); // Procesar el comentario de una línea
                tokenActual.setLength(0); // Limpiar el token actual
                continue; // Continuar al siguiente carácter
            }

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
                } else if (esSeparadorJS(caracter)) {
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
            TokensJS tokenJS = new TokensJS();
            procesarToken(tokenActual.toString(), tokenJS);
        }
        return contenido;
    }

    private boolean esSeparadorJS(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '(' || c == ')'
                || c == '[' || c == ']' || c == '{' || c == '}' || c == ';'
                || c == ',' || c == '+' || c == '-' || c == '*' || c == '/'
                || c == '=' || c == '<' || c == '>' || c == '.';
    }

    private void procesarToken(String token, TokensJS tokenJS) {
        if (tokenJS.esTokenValido(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular(token);
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esComentario(token)) {
            System.out.println("valido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular("//[a-zA-Z]|[0-9]|[.]");
            tokenJS.setTipo("Comentario");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esPalabraReservada(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular(token);
            tokenJS.setTipo("Palabra reservada");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.isValidInteger(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular("[0-9]+");
            tokenJS.setTipo("Entero");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.isValidFloat(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular("[0-9]+.[0-9]+");
            tokenJS.setTipo("Decimal");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esCadena(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular(token);
            tokenJS.setTipo("Cadena");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esIdentificadorValido(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular("[a-zA-Z]([a-zA-Z]|[0-9]|[ _ ])*");
            tokenJS.setTipo("Identificador");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esCorchete(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular(token);
            tokenJS.setTipo("Corchete");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esLlave(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular(token);
            tokenJS.setTipo("Llave");
            listaTokensJS.add(tokenJS);
        } else if (tokenJS.esParentesis(token)) {
            System.out.println("válido: " + token);
            tokenJS.setTexto(token);
            tokenJS.setExpresionRegular(token);
            tokenJS.setTipo("Parentesis");
            listaTokensJS.add(tokenJS);
        } else {
            TokenErrado tokenIn = new TokenErrado();
            tokenIn.setTexto(token);
            tokenIn.setLenguaje("JavaScript");
            tokenIn.setLenguajeSugerido("JavaScript");
            listaTokenInvalido.add(tokenIn);
        }
    }

    public TokenDeEstado getTokenE() {
        return tokenE;
    }

    public List<TokensJS> getListaTokensJS() {
        return listaTokensJS;
    }

    public void setListaTokensJS(List<TokensJS> listaTokensJS) {
        this.listaTokensJS = listaTokensJS;
    }

    public List<TokensCSS> getListaTokensCss() {
        return listaTokensCss;
    }

    public List<TokenErrado> getListaTokenInvalido() {
        return listaTokenInvalido;
    }

    public List<TokensHTML> getListaTokensHtml() {
        return listaTokensHtml;
    }

    public boolean esTextoValido() {
        return listaTokenInvalido.isEmpty();
    }

    public List<String> getHtmlList() {
        return htmlList;
    }

    public List<String> getCssList() {
        return cssList;
    }

    public List<String> getJsList() {
        return jsList;
    }

}
