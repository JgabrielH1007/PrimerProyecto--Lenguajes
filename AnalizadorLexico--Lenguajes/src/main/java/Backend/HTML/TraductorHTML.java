/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.HTML;

/**
 *
 * @author gabrielh
 */
public class TraductorHTML {
    
    
    public void traducirHtml(String contenidoToken){
        if (contenidoToken.equals("principal")) {
            contenidoToken = "main";
        } else if(contenidoToken.equals("encabezado")){
            contenidoToken = "header";
        } else if(contenidoToken.equals("navegacion")){
            contenidoToken = "nav";
        } else if(contenidoToken.equals("apartado")){
            contenidoToken = "aside";
        } else if(contenidoToken.equals("listaordenada")){
            contenidoToken = "ul";
        } else if(contenidoToken.equals("listadesordenada")){
            contenidoToken = "ol";
        } else if(contenidoToken.equals("itemlista")){
            contenidoToken = "li";
        } else if(contenidoToken.equals("anclaje")){
            contenidoToken = "a";
        } else if(contenidoToken.equals("contenedor")){
            contenidoToken = "div";
        } else if(contenidoToken.equals("seccion")){
            contenidoToken = "section";
        } else if(contenidoToken.equals("articulo")){
            contenidoToken = "article";
        } else if(contenidoToken.equals("titulo1")){
            contenidoToken = "h1";
        } else if(contenidoToken.equals("titulo2")){
            contenidoToken = "h2";
        } else if(contenidoToken.equals("titulo3")){
            contenidoToken = "h3";
        } else if(contenidoToken.equals("titulo4")){
            contenidoToken = "h4";
        } else if(contenidoToken.equals("titulo5")){
            contenidoToken = "h5";
        } else if(contenidoToken.equals("titulo6")){
            contenidoToken = "h6";
        } else if(contenidoToken.equals("parrafo")){
            contenidoToken = "p";
        } else if(contenidoToken.equals("span")){
            contenidoToken = "span";
        } else if(contenidoToken.equals("entrada")){
            contenidoToken = "input";
        } else if(contenidoToken.equals("formulario")){
            contenidoToken = "form";
        } else if(contenidoToken.equals("label")){
            contenidoToken = "label";
        } else if(contenidoToken.equals("area")){
            contenidoToken = "textarea";
        } else if(contenidoToken.equals("boton")){
            contenidoToken = "button";
        } else if(contenidoToken.equals("piepagina")){
            contenidoToken = "footer";
        }
    }
}
