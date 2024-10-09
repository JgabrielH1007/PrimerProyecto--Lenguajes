/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

/**
 *
 * @author gabrielh
 */
public class Token {

    private String texto;
    private final String lenguaje = "Html"; // Se puede modificar si es necesario
    private String expresionRegular;
    private String tipo;
    private int fila;
    private int columna;

    public Token() {

    }

    // Métodos getter
    public String getTexto() {
        return texto;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public String getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}
