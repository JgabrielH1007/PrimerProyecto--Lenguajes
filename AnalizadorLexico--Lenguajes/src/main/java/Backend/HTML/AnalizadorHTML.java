/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.HTML;

import Backend.TokenErrado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabrielh
 */
public class AnalizadorHTML {
    private List<TokensHTML> listaTokens;
    private List<TokenErrado> listaTokenInvalido;
    public AnalizadorHTML() {

    }

      public void analizarContenido(String contenido) {
        // Almacena cada token que se encuentre
        List<String> tokens = new ArrayList<>();

        // Variable auxiliar para acumular caracteres y formar un token
        StringBuilder tokenActual = new StringBuilder();
        boolean dentroDeEtiqueta = false;

        // Recorrer cada carácter del contenido
        for (int i = 0; i < contenido.length(); i++) {
            char caracterActual = contenido.charAt(i);

            if (caracterActual == '<') {
                // Si estamos en medio de un token de texto, agregarlo antes de la etiqueta
                if (tokenActual.length() > 0 && !dentroDeEtiqueta) {
                    tokens.add(tokenActual.toString().trim());
                    tokenActual.setLength(0);
                }
                // Iniciar la captura de una etiqueta
                dentroDeEtiqueta = true;
                tokenActual.append(caracterActual);
            } else if (caracterActual == '>') {
                // Finalizar la captura de una etiqueta
                tokenActual.append(caracterActual);
                tokens.add(tokenActual.toString().trim());
                tokenActual.setLength(0);
                dentroDeEtiqueta = false;
            } else {
                // Acumular caracteres para formar el token actual
                tokenActual.append(caracterActual);
            }
        }

        // Agregar cualquier contenido restante como un token
        if (tokenActual.length() > 0) {
            tokens.add(tokenActual.toString().trim());
        }

        // Analizar cada token con TokensHTML y mostrar su validez
         // Analizar cada token y almacenarlo en la lista
        for (String token : tokens) {
            TokensHTML tokenObjeto = new TokensHTML(); // Crear un nuevo objeto TokensHTML para cada token
            if (tokenObjeto.esTokenValido(token)) {
                tokenObjeto.setTexto(token);
                listaTokens.add(tokenObjeto);  // Agregar el objeto a la lista
                System.out.println("Token válido: " + token);
            } else if (tokenObjeto.esTextoValido(token)) {
                tokenObjeto.setTexto(token);
                listaTokens.add(tokenObjeto);  // Agregar el objeto a la lista
                System.out.println("Texto válido: " + token);
            } else if (!token.isEmpty()) { // Ignorar tokens vacíos
                tokenObjeto.setTexto(token);
                listaTokens.add(tokenObjeto);  // Agregar el objeto a la lista
                System.out.println("Token no válido o desconocido: " + token);
            }else{
                TokenErrado tokenIn = new TokenErrado();
                tokenIn.setTexto(token);
                tokenIn.setLenguaje("Html");
                
            }
        }
    }
      
    public List<TokensHTML> getListaTokens() {
        return listaTokens;
    }
}
