/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import Backend.CSS.AnalizadorCSS;
import Backend.HTML.AnalizadorHTML;
import Backend.JS.AnalizadorJS;

/**
 *
 * @author gabrielh
 */
public class LectorTexto {
    private AnalizadorHTML html = new AnalizadorHTML();
    private AnalizadorCSS css = new AnalizadorCSS();
    private AnalizadorJS js = new AnalizadorJS();
    private TokenDeEstado tokenE = new TokenDeEstado();
    
    public void leerTexto(String texto) {
        // Separar el texto por líneas usando el método split
        String[] lineas = texto.split("\n");
        StringBuilder contenidoActual = new StringBuilder();
        String tokenActual = ""; // Token de estado actual

        // Iterar sobre cada línea
        for (String linea : lineas) {
            // Verificar si la línea contiene uno de los tokens de estado
            if (linea.contains(tokenE.getTOKENHTML())) {
                // Si hay contenido acumulado y tenemos un token actual, enviarlo al analizador correspondiente
                enviarAlAnalizador(tokenActual, contenidoActual.toString());
                // Reiniciar el acumulador de contenido
                contenidoActual.setLength(0);
                // Actualizar el token actual
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

    private void enviarAlAnalizador(String token, String contenido) {
        if (contenido.isEmpty()) return; // No enviar si el contenido está vacío

        switch (token) {
            case ">>[html]":
                html.analizarContenido(contenido);
                break;
            case ">>[css]":
                css.analizarCSS(contenido);
                break;
            case ">>[js]":
                js.analizar(contenido);
                break;
            default:
                // Si no hay un token válido, no se realiza ninguna acción
                break;
        }
    }

}
