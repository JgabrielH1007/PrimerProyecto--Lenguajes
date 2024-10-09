/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author gabrielh
 */
public class GeneradorHTML {

    public void generarHtml(String html, String css, String js) {
        // Definir la estructura básica del archivo HTML
        String htmlContent = "<!DOCTYPE html>\n"
                + "<html lang=\"es\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>DOCUMENTO LENGUAJES</title>\n"
                + "    <style>\n"
                + css + "\n"
                + // Incluir el contenido CSS dentro de <style>
                "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + html + "\n"
                + // Incluir el contenido HTML dentro de <body>
                "</body>\n"
                + "<script>\n"
                + js + "\n"
                + // Incluir el contenido JavaScript dentro de <script>
                "</script>\n"
                + "</html>";

        // Escribir el contenido en un archivo HTML
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("documentoLenguajes.html"))) {
            writer.write(htmlContent);
            System.out.println("Archivo HTML generado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
