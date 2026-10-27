package inventario;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    public static void guardar(String nombreArchivo, DefaultTableModel modelo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                StringBuilder linea = new StringBuilder();
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    if (j > 0) linea.append(",");
                    Object val = modelo.getValueAt(i, j);
                    linea.append(val != null ? val.toString() : "");
                }
                pw.println(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public static void cargar(String nombreArchivo, DefaultTableModel modelo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            modelo.setRowCount(0);
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] datos = linea.split(",", -1);
                    modelo.addRow(datos);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar: " + e.getMessage());
        }
    }

    // Retorna lista de artículos para ComboBox (código - artículo)
    public static List<String[]> obtenerProductos() {
        List<String[]> productos = new ArrayList<>();
        File archivo = new File("productos.csv");
        if (!archivo.exists()) return productos;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] datos = linea.split(",", -1);
                    if (datos.length >= 2) {
                        productos.add(new String[]{datos[0].trim(), datos[1].trim()});
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo productos: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Retorna lista de NOMBRES de categorías para los ComboBox.
     * categorias.csv tiene columnas: CÓDIGO CAT., NOMBRE, DESCRIPCIÓN
     * Se retorna el NOMBRE (columna 1) para mostrarlo al usuario.
     */
    public static List<String> obtenerCategorias() {
        List<String> cats = new ArrayList<>();
        File archivo = new File("categorias.csv");
        if (!archivo.exists()) return cats;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] d = linea.split(",", -1);
                    if (d.length >= 2 && !d[1].trim().isEmpty()) {
                        cats.add(d[1].trim()); // columna 1 = NOMBRE
                    } else if (d.length >= 1 && !d[0].trim().isEmpty()) {
                        cats.add(d[0].trim()); // fallback al código si no hay nombre
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo categorías: " + e.getMessage());
        }
        return cats;
    }

    public static void actualizarProducto(String codigo, String articulo,
                                          int deltaEntrada, int deltaSalida) {
        File archivo = new File("productos.csv");
        List<String[]> filas = new ArrayList<>();
        boolean encontrado = false;

        if (archivo.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (!linea.trim().isEmpty())
                        filas.add(linea.split(",", -1));
                }
            } catch (IOException e) {
                System.err.println("Error leyendo productos: " + e.getMessage());
            }
        }

        // productos.csv ahora tiene 6 columnas: CÓDIGO, ARTÍCULO, CATEGORÍA, ENTRADA, SALIDAS, STOCK
        for (String[] fila : filas) {
            if (fila.length >= 4 && fila[0].trim().equalsIgnoreCase(codigo.trim())) {
                int entIdx = 3, salIdx = 4, stoIdx = 5;
                int entrada = parseIntSafe(fila.length > entIdx ? fila[entIdx] : "0") + deltaEntrada;
                int salidas = parseIntSafe(fila.length > salIdx ? fila[salIdx] : "0") + deltaSalida;
                int stock   = entrada - salidas;
                if (fila.length > entIdx) fila[entIdx] = String.valueOf(Math.max(0, entrada));
                if (fila.length > salIdx) fila[salIdx] = String.valueOf(Math.max(0, salidas));
                if (fila.length > stoIdx) fila[stoIdx] = String.valueOf(Math.max(0, stock));
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            int entrada = Math.max(0, deltaEntrada);
            int salidas = Math.max(0, deltaSalida);
            filas.add(new String[]{codigo, articulo, "",
                String.valueOf(entrada), String.valueOf(salidas),
                String.valueOf(Math.max(0, entrada - salidas))});
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            for (String[] fila : filas)
                pw.println(String.join(",", fila));
        } catch (IOException e) {
            System.err.println("Error guardando productos: " + e.getMessage());
        }
    }

    public static int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }
}