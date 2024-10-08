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

        // Remover "<" y ">" para obtener el contenido del token
        String contenido = token.substring(1, token.length() - 1).trim();

        // Si el contenido comienza con '/', es un token de cierre
        boolean esCierre = contenido.startsWith(BARRACIERRE);
        if (esCierre) {
            contenido = contenido.substring(1).trim();  // Eliminar el '/'
        }

        // Dividir el contenido en partes separadas por espacios
        String[] partes = contenido.split("\\s+");

        // La primera parte debe ser el nombre de la etiqueta, que debe ser válido
        if (!esContenidoValido(partes[0])) {
            return false;
        }

        // Si es un token de cierre (por ejemplo, </contenedor>), debe tener solo el nombre de la etiqueta
        if (esCierre && partes.length == 1) {
            return true;
        }

        // Validar las partes adicionales como atributos (ejemplo: class="container")
        for (int i = 1; i < partes.length; i++) {
            String[] atributo = partes[i].split("=");

            // Un atributo válido tiene la forma nombre="valor"
            if (atributo.length != 2 || !esPalabraReservada(atributo[0])) {
                return false;
            }

            // El valor del atributo debe ser una cadena válida (ejemplo: "container")
            if (!tokenCadena(atributo[1])) {
                return false;
            }
        }

        return true;
    }

    public boolean esComentario(String linea) {
        // Eliminar espacios en blanco al inicio de la línea
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

    // Verifica si la palabra es una palabra reservada válida
    private boolean esPalabraReservada(String palabra) {
        for (String reservada : PALABRAS_RESERVADAS) {
            if (reservada.equals(palabra)) {
                return true;
            }
        }
        return false;
    }

    // Verificar si un texto es válido (sin '<' o '>')
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
