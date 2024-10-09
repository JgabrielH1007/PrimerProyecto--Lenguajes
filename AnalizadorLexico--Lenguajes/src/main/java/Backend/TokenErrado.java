/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

/**
 *
 * @author gabrielh
 */

public class TokenErrado {

    private String texto;
    private String lenguaje;
    private String lenguajeSugerido;
    private int fila;
    private int columna;

    public String getLenguajeSugerido() {
        return lenguajeSugerido;
    }

    public void setLenguajeSugerido(String lenguajeSugerido) {
        this.lenguajeSugerido = lenguajeSugerido;
    }
    
    
    
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
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
