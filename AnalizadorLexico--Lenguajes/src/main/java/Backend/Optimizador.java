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
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author gabrielh
 */
public class Optimizador {

    private List<TokensHTML> listaTokensHtml;
    private List<TokensCSS> listaTokensCss;
    private List<TokensJS> listaTokensJS;
    private TokenDeEstado tokenE = new TokenDeEstado();
    private String prueba;

    public Optimizador() {
        listaTokensHtml = new ArrayList<>();
        listaTokensCss = new ArrayList<>();
        listaTokensJS = new ArrayList<>();

    }

    public String optimizarTexto(String contenido) throws ExceptionToken {
        String[] lineas = contenido.split("\n");
        StringBuilder contenidoActual = new StringBuilder();
        String tokenActual = "";

        for (String linea : lineas) {
            String lineaSinEspacios = linea.trim();
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

        // Imprimir los tokens eliminados
        return optimizarHTML(contenidoActual.toString());
    }

    private void enviarAlAnalizador(String token, String contenido) throws ExceptionToken {
        if (contenido.isEmpty()) {
            return; // No enviar si el contenido está vacío
        }
        switch (token) {
            case ">>[html]":
                prueba = optimizarHTML(contenido); // Acumular el contenido traducido
                break;
            case ">>[css]":
                break;
            case ">>[js]":
                break;
            default:
                // Si no hay un token válido, no se realiza ninguna acción
                break;
        }
    }

    // Método principal para optimizar el contenido HTML
    public String optimizarHTML(String contenido) throws ExceptionToken {
        // Almacena cada token válido o erróneo que se encuentre
        List<String> tokensBorrados = new ArrayList<>();
        StringBuilder contenidoOptimizado = new StringBuilder(); // Acumular el contenido optimizado

        // El token de estado a preservar (modificar según el formato de estado)
        String tokenEstado = ">>[html]"; // Token de estado que no se debe eliminar

        // Almacena todas las líneas del contenido original separadas por salto de línea
        String[] lineas = contenido.split("\n");

        // Iterar a través de cada línea para procesarla
        for (String linea : lineas) {
            String lineaTrim = linea.trim(); // Remover espacios al inicio y final para identificar mejor

            // Preservar el token de estado si es encontrado
            if (lineaTrim.equals(tokenEstado)) {
                contenidoOptimizado.append(lineaTrim).append("\n");
                continue; // No procesar más esta línea, simplemente agregarla
            }

            // Saltar la línea completa si contiene un comentario "//" (eliminar línea completa)
            if (lineaTrim.contains("//")) {
                // Dividir la línea en tokens antes de eliminarla
                guardarTokensSeparados(lineaTrim, tokensBorrados);
                continue; // Omitir esta línea y pasar a la siguiente
            }

            // Agregar la línea al contenido optimizado si no está vacía
            if (!lineaTrim.isEmpty()) {
                contenidoOptimizado.append(linea).append("\n"); // Conservar la estructura original (indentación)
            }
        }

        // Procesar cada token borrado según las reglas de la clase TokensHTML
        for (String tokenBorrado : tokensBorrados) {
            TokensHTML tokenObjeto = new TokensHTML(); // Crear un nuevo objeto TokensHTML para cada token
            tokenObjeto.setTexto(tokenBorrado); // Asignar el token borrado al objeto
            listaTokensHtml.add(tokenObjeto); // Agregarlo a la lista de tokens eliminados
        }

        // Imprimir el contenido de la lista de tokens borrados
        for (TokensHTML token : listaTokensHtml) {
            System.out.println("Token borrado: " + token.getTexto());
        }

        // Retornar el contenido optimizado como String
        return contenidoOptimizado.toString();
    }

    // Método para separar y guardar los tokens de una línea
    private void guardarTokensSeparados(String linea, List<String> tokensBorrados) {
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroDeEtiqueta = false;
        boolean dentroDeComentario = false;

        for (int i = 0; i < linea.length(); i++) {
            char caracterActual = linea.charAt(i);

            // Detectar inicio de comentario
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

            // Detectar inicio y fin de etiqueta HTML
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

        // Guardar el token restante si es un comentario o un texto
        if (tokenActual.length() > 0) {
            tokensBorrados.add(tokenActual.toString().trim());
        }
    }

}
