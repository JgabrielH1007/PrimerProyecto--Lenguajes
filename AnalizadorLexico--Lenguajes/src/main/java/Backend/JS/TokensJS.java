/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.JS;

import Backend.Token;

/**
 *
 * @author gabrielh
 */
public class TokensJS extends Token{

    private String texto;
    private final String LENGUAJE = "JavaScript";
    private String expresionRegular;
    private String tipo;
    private int fila;
    private int columna;
    private final String SUMA = "+";
    private final String RESTA = "-";
    private final String MULTIPLICACION = "*";
    private final String DIVISION = "/";
    private final String IGUAL = "==";
    private final String MENORQUE = "<";
    private final String MAYORQUE = ">";
    private final String MENORIGUALQUE = "<=";
    private final String MAYORIGUALQUE = ">=";
    private final String DIFERENTE = "!=";
    private final String OR = "||";
    private final String AND = "&&";
    private final String NOT = "!";
    private final String INCREMENTO = "++";
    private final String DECREMENTO = "--";
    private final String TRUE = "true";
    private final String FALSE = "false";
    private final String[] PARENTESIS = {"(", ")"};
    private final String[] CORCHETES = {"[", "]"};
    private final String[] LLAVES = {"{", "}"};
    private final String ASIGNACION = "=";
    private final String PUNTOYCOMA = ";";
    private final String COMA = ",";
    private final String PUNTO = ".";
    private final String DOSPUNTOS = ":";
    private final String[] PALABRARESERVADA = {"function", "const", "let", "document", "event", "alert", "for", "while", "if", "else", "return", "console.log", "null"};

    public boolean esTokenValido(String token) {
        if (token.equals(SUMA)) {
            tipo="Aritmetico";
            return true;
        } else if (token.equals(RESTA)) {
            tipo="Aritmetico";
            return true;
        } else if (token.equals(MULTIPLICACION)) {
            tipo="Aritmetico";
            return true;
        } else if (token.equals(DIVISION)) {
            tipo="Aritmetico";
            return true;
        } else if (token.equals(IGUAL)) {
            tipo = "Relacionales";
            return true;
        } else if (token.equals(MENORQUE)) {
            tipo = "Relacionales";
            return true;
        } else if (token.equals(MAYORQUE)) {
            tipo = "Relacionales";
            return true;
        } else if (token.equals(MENORIGUALQUE)) {
            tipo = "Relacionales";
            return true;
        } else if (token.equals(MAYORIGUALQUE)) {
            tipo = "Relacionales";
            return true;
        } else if (token.equals(DIFERENTE)) {
            tipo = "Relacionales";
            return true;
        } else if (token.equals(OR)) {
            tipo = "Logico";
            return true;
        } else if (token.equals(AND)) {
            tipo = "Logico";
            return true;
        } else if (token.equals(NOT)) {
            tipo = "Logico";
            return true;
        } else if (token.equals(INCREMENTO)) {
            tipo = "Incrementales";
            return true;
        } else if (token.equals(DECREMENTO)) {
            tipo = "Incrementales";
            return true;
        } else if (token.equals(TRUE)) {
            tipo="booleano";
            return true;
        } else if (token.equals(FALSE)) {
            tipo="booleano";
            return true;
        } else if (token.equals(ASIGNACION)) {
            tipo = "Otro";
            return true;
        } else if (token.equals(PUNTOYCOMA)) {
            tipo = "Otro";
            return true;
        } else if (token.equals(COMA)) {
            tipo = "Otro";
            return true;
        } else if (token.equals(PUNTO)) {
            tipo = "Otro";
            return true;
        } else if (token.equals(DOSPUNTOS)) {
            tipo = "Otro";
            return true;
        }
        return false;
    }

    public boolean esParentesis(String token) {
        return verificarArreglos(token, PARENTESIS);
    }

    public boolean esCorchete(String token) {
        return verificarArreglos(token, CORCHETES);
    }

    public boolean esLlave(String token) {
        return verificarArreglos(token, LLAVES);
    }

    public boolean esPalabraReservada(String token) {
        return verificarArreglos(token, PALABRARESERVADA);
    }

    private boolean verificarArreglos(String token, String[] arreglo) {
        for (String elemento : arreglo) {
            if (elemento.equals(token)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidInteger(String token) {
        try {
            int value = Integer.parseInt(token);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;  // No es un número entero válido
        }
    }

    public boolean isValidFloat(String token) {
        try {
            double value = Double.parseDouble(token);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;  // No es un número flotante válido
        }
    }

    public boolean esCadena(String token) {
        // Verificar que la cadena no esté vacía o nula
        if (token == null || token.isEmpty()) {
            return false;
        }

        // Obtener el primer y último carácter de la cadena
        char primerCaracter = token.charAt(0);
        char ultimoCaracter = token.charAt(token.length() - 1);

        // Verificar si la cadena comienza y termina con los caracteres específicos
        return (primerCaracter == '"' && ultimoCaracter == '"')
                || // Comillas dobles
                (primerCaracter == '\'' && ultimoCaracter == '\'')
                || // Comillas simples
                (primerCaracter == '`' && ultimoCaracter == '`');     // Acento grave
    }

    public boolean esIdentificadorValido(String token) {
        // Verificar si el token está vacío o es nulo
        if (token == null || token.isEmpty()) {
            return false;
        }

        // Verificar que el primer carácter sea una letra (mayúscula o minúscula)
        char primerCaracter = token.charAt(0);
        if (!Character.isLetter(primerCaracter)) {
            return false;
        }

        // Verificar que los caracteres restantes sean letras, números o guiones bajos
        for (int i = 1; i < token.length(); i++) {
            char caracter = token.charAt(i);
            if (!Character.isLetterOrDigit(caracter) && caracter != '_') {
                return false;
            }
        }

        // Si pasa todas las validaciones, es un identificador válido
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
