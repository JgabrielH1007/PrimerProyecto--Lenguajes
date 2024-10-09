/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Reportes;

import Backend.CSS.TokensCSS;
import Backend.HTML.TokensHTML;
import Backend.JS.TokensJS;
import Backend.Token;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gabrielh
 */
public class ReporteOptimizacion extends JFrame {

    private JTable tablaTokens;
    private DefaultTableModel modeloTabla;
    private JButton botonExportarHTML;
    private List<TokensHTML> listaTokensHtml;
    private List<TokensCSS> listaTokensCss;
    private List<TokensJS> listaTokensJS;

    public ReporteOptimizacion(List<TokensHTML> listaTokensHtml, List<TokensCSS> listaTokensCss, List<TokensJS> listaTokensJS) {
        this.listaTokensHtml = listaTokensHtml;
        this.listaTokensCss = listaTokensCss;
        this.listaTokensJS = listaTokensJS;

        initComponents(); // Inicializar JFrame

        inicializarTabla(); // Inicializar tabla con las listas de tokens
        inicializarBotonExportar(); // Inicializar botón de exportación

        this.setLayout(new BorderLayout());

        // Agregar la tabla y el botón al JFrame en posiciones específicas del BorderLayout
        add(new JScrollPane(tablaTokens), BorderLayout.CENTER); // Centro para la tabla con scroll
        add(botonExportarHTML, BorderLayout.SOUTH); // Sur para el botón de exportar

        this.setSize(800, 600); // Establecer tamaño del JFrame
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
    }

    private void inicializarTabla() {
        // Definir las columnas del modelo de la tabla
        String[] columnas = {"Texto", "Lenguaje", "Expresión Regular", "Tipo", "Fila", "Columna"};

        // Crear el modelo de la tabla sin permitir edición
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar la edición de todas las celdas
            }
        };

        // Crear la tabla con el modelo y evitar el reordenamiento de columnas
        tablaTokens = new JTable(modeloTabla);
        tablaTokens.getTableHeader().setReorderingAllowed(false); // Deshabilitar mover columnas

        // Llenar el modelo de la tabla con las listas de tokens
        agregarTokensATabla(listaTokensHtml, "HTML");
        agregarTokensATabla(listaTokensCss, "CSS");
        agregarTokensATabla(listaTokensJS, "JavaScript");
    }

    private void agregarTokensATabla(List<? extends Token> lista, String lenguaje) {
        // Añadir los tokens a la tabla
        for (Token token : lista) {
            Object[] fila = {
                token.getTexto(),
                lenguaje,
                token.getExpresionRegular(),
                token.getTipo(),
                token.getFila(),
                token.getColumna()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void inicializarBotonExportar() {
        botonExportarHTML = new JButton("Exportar HTML");
        botonExportarHTML.setFont(new Font("SansSerif", Font.BOLD, 14)); // Fuente del botón

        botonExportarHTML.addActionListener(e -> exportarTablaAHTML());
    }

    private void exportarTablaAHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<html><head><title>Reporte de Optimizacion</title>");
        html.append("<style>");
        html.append("table { width: 100%; border-collapse: collapse; }");
        html.append("th, td { padding: 10px; border: 1px solid black; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("</style></head><body>");
        html.append("<h1 style='font-family: Arial, sans-serif;'>Reporte de Tokens</h1>");
        html.append("<table>");
        html.append("<tr>");

        // Añadir encabezados de tabla
        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
            html.append("<th>").append(escapeHTML(modeloTabla.getColumnName(i))).append("</th>");
        }
        html.append("</tr>");

        // Añadir filas de la tabla
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            html.append("<tr>");
            for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                Object value = modeloTabla.getValueAt(i, j);
                html.append("<td>").append(value != null ? escapeHTML(value.toString()) : "").append("</td>");
            }
            html.append("</tr>");
        }

        html.append("</table></body></html>");

        // Escribir el HTML a un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_optimizacion.html"))) {
            writer.write(html.toString());
            JOptionPane.showMessageDialog(this, "Exportación exitosa: reporte_tokens.html", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String escapeHTML(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }


}
