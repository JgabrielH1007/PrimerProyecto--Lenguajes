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
    private AnalizadorHTML html;
    private AnalizadorCSS css;
    private AnalizadorJS js;
    private TokenDeEstado tokenE = new TokenDeEstado();
    
    public void leerTexto(String texto) {
    // Separar el texto por líneas usando el método split
    String[] lineas = texto.split("\n");
    
    // Iterar sobre cada línea
    for (String linea : lineas) {
         // Verificar si la línea contiene uno de los tokens de estado
        if (linea.contains(tokenE.getTOKENHTML())) {
        } else if (linea.contains(tokenE.getTOKENCSS())) {
        } else if (linea.contains(tokenE.getTOKENJAVASCRIPT())) {
        } else {
        }
        
    }
}

}
