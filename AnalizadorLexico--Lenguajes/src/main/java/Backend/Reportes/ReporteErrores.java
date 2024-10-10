/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Reportes;

import Backend.TokenErrado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author gabrielh
 */
public class ReporteErrores extends javax.swing.JFrame {

    private JTable tablaErrores;
    private DefaultTableModel modeloTabla;
    private JButton botonExportarHTML;

    public ReporteErrores(List<TokenErrado> listaErrores) {
        // Llamar al método initComponents para inicializar el JFrame
        initComponents();

        inicializarTabla(listaErrores);
        inicializarBotonExportar();

        this.setLayout(new BorderLayout());

        // Agregar la tabla y el botón al JFrame en posiciones específicas del BorderLayout
        add(new JScrollPane(tablaErrores), BorderLayout.CENTER); // Centro para la tabla con scroll
        add(botonExportarHTML, BorderLayout.SOUTH); // Sur para el botón de exportar

        this.setSize(800, 600); // Establecer tamaño del JFrame
        this.setVisible(true);  
        this.revalidate();     
        this.repaint();         
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void inicializarTabla(List<TokenErrado> listaErrores) {
        // Definir las columnas del modelo de la tabla
        String[] columnas = {"Token", "Lenguaje Encontrado", "Lenguaje Sugerido", "Fila", "Columna"};

        // Crear el modelo de la tabla sin permitir edición
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar la edición de todas las celdas
            }
        };

        // Crear la tabla con el modelo y evitar el reordenamiento de columnas
        tablaErrores = new JTable(modeloTabla);
        tablaErrores.getTableHeader().setReorderingAllowed(false); // Deshabilitar mover columnas

        // Llenar el modelo de la tabla con la lista de errores
        for (TokenErrado token : listaErrores) {
            // Depurar para confirmar el valor del texto
            System.out.println("Token encontrado: " + token.getTexto()); // Verificar en consola

            Object[] fila = {
                token.getTexto(), // Mostrar el texto del token
                token.getLenguaje(),
                token.getLenguajeSugerido(),
                token.getFila(),
                token.getColumna()
            };
            modeloTabla.addRow(fila); // Añadir fila al modelo de la tabla
        }
    }

    private void inicializarBotonExportar() {
        botonExportarHTML = new JButton("Exportar HTML");
        botonExportarHTML.setFont(new Font("SansSerif", Font.BOLD, 14)); // Fuente del botón

        botonExportarHTML.addActionListener(e -> exportarTablaAHTML());
    }

    private void exportarTablaAHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<html><head><title>Reporte de Errores</title>");
        html.append("<style>");
        html.append("table { width: 100%; border-collapse: collapse; }");
        html.append("th, td { padding: 10px; border: 1px solid black; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append("</style></head><body>");
        html.append("<h1 style='font-family: Arial, sans-serif;'>Reporte de Errores</h1>");
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_errores.html"))) {
            writer.write(html.toString());
            JOptionPane.showMessageDialog(this, "Exportación exitosa: reporte_errores.html", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
