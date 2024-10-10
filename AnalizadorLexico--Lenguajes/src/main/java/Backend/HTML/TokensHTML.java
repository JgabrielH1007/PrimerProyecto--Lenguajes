/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.HTML;

import Backend.Token;

/**
 *
 * @author gabrielh
 */
public class TokensHTML extends Token {

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
    private String texto;
    private final String LENGUAJE = "Html";
    private String expresionRegular;
    private String tipo;
    private int fila;
    private int columna;

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

    public boolean esTokenValido(String token) {
        if (token == null || token.length() < 3) {
            return false;
        }

        // Validar si el token tiene apertura y cierre "< >"
        if (!(token.startsWith(APERTURA) && token.endsWith(CIERRE))) {
            return false;
        }

        String contenido = token.substring(1, token.length() - 1).trim();

        // Si el contenido comienza con '/', es un token de cierre
        boolean esCierre = contenido.startsWith(BARRACIERRE);
        if (esCierre) {
            contenido = contenido.substring(1).trim();  // Eliminar el '/'
        }

        // Encontrar el nombre de la etiqueta (la primera palabra antes de los espacios)
        int primerEspacio = contenido.indexOf(' ');
        String nombreEtiqueta = primerEspacio == -1 ? contenido : contenido.substring(0, primerEspacio);

        // Validar si la primera parte es un nombre de etiqueta válido
        if (!esContenidoValido(nombreEtiqueta)) {
            return false;
        }

        // Si es un token de cierre (por ejemplo, </contenedor>), debe tener solo el nombre de la etiqueta
        if (esCierre && primerEspacio == -1) {
            return true;
        }

        // Si tiene más contenido, procesar los atributos
        String atributos = primerEspacio == -1 ? "" : contenido.substring(primerEspacio + 1).trim();

        // Validar cada atributo en el formato nombre="valor"
        int i = 0;
        while (i < atributos.length()) {
            // Encontrar el nombre del atributo hasta el '='
            int igualIndex = atributos.indexOf('=', i);
            if (igualIndex == -1) {
                return false;  // No se encontró '=' en el atributo
            }

            String nombreAtributo = atributos.substring(i, igualIndex).trim();
            if (!esPalabraReservada(nombreAtributo)) {
                return false;  // Atributo inválido
            }

            // Encontrar el valor del atributo entre comillas
            int valorInicio = igualIndex + 1;
            if (valorInicio >= atributos.length() || atributos.charAt(valorInicio) != '\"') {
                return false;  // El valor no comienza con comillas
            }

            // Buscar el final del valor del atributo
            int valorFin = valorInicio + 1;
            while (valorFin < atributos.length() && atributos.charAt(valorFin) != '\"') {
                valorFin++;
            }

            if (valorFin >= atributos.length() || atributos.charAt(valorFin) != '\"') {
                return false;  // No se encontró la comilla de cierre
            }

            String valorAtributo = atributos.substring(valorInicio, valorFin + 1);
            if (!tokenCadena(valorAtributo)) {
                return false;  // El valor no es una cadena válida
            }

            // Avanzar el índice a la siguiente parte después del atributo
            i = valorFin + 1;

            // Saltar espacios en blanco entre atributos
            while (i < atributos.length() && atributos.charAt(i) == ' ') {
                i++;
            }
        }

        return true;
    }

    public boolean esComentario(String linea) {
        String lineaSinEspacios = linea.trim();

        // Verificar si hay "//" en la línea
        int indiceComentario = lineaSinEspacios.indexOf("//");

        // Si encontramos "//", entonces es un comentario
        return indiceComentario != -1;
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

    private boolean esPalabraReservada(String palabra) {
        for (String reservada : PALABRAS_RESERVADAS) {
            if (reservada.equals(palabra)) {
                System.out.println(palabra);
                return true;
            }
        }
        return false;
    }

    public boolean esTextoValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        for (char c : texto.toCharArray()) {
            if (c == '<' || c == '>') {
                return false;
            }
        }
        return true;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public void setExpresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

}
