package inventario;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SortOrder;
import javax.swing.RowSorter;
import java.util.ArrayList;


public class VentanaProductos extends JFrame {

    private DefaultTableModel modelo;
    private JTable tablaGlobal;
    
    public VentanaProductos(JFrame menu) {
        setTitle("AutoLujos Guaman — Productos");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(MenuPrincipal.CARBON);
        setLayout(new BorderLayout());
        add(crearTopBar(menu), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
    }

    private JPanel crearTopBar(JFrame menu) {

        JPanel panel = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {

                g.setColor(new Color(13,13,13));
                g.fillRect(0,0,getWidth(),getHeight());

                g.setColor(new Color(35,35,35));
                g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };

        panel.setPreferredSize(new Dimension(1200, 50));
        panel.setOpaque(false);

        JButton btnVolver = botonVolver();

        btnVolver.addActionListener(e -> {

            menu.setVisible(true);
            dispose();
        });

        JLabel marca = new JLabel("AutoLujos Guaman");

        marca.setFont(new Font("Georgia", Font.BOLD, 13));
        marca.setForeground(MenuPrincipal.GOLD);
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));

        left.setOpaque(false);
        left.add(btnVolver);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 8));
        right.setOpaque(false);

        
        right.add(marca);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);

        
        return panel;
    }
    
    private JPanel crearCentro() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MenuPrincipal.DARK);

        JPanel banner = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {

                g.setColor(MenuPrincipal.DARK);
                g.fillRect(0,0,getWidth(),getHeight());

                g.setColor(MenuPrincipal.ORANGE);
                g.fillRect(0,0,3,getHeight());

                g.setColor(new Color(35,35,35));
                g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };

        banner.setOpaque(false);

        JLabel titulo = new JLabel("Productos");

        titulo.setFont(new Font("Georgia", Font.BOLD, 18));
        titulo.setForeground(MenuPrincipal.ORANGE);

        JLabel sub = new JLabel("— Catálogo de artículos");
        
        JLabel lblBuscar = new JLabel("BUSCAR:");

        lblBuscar.setForeground(MenuPrincipal.WHITE);
        lblBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));

        JTextField txtBuscar = new JTextField();

        txtBuscar.setPreferredSize(new Dimension(150, 26));
        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.setForeground(Color.BLACK);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtBuscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                // Buscar solo al presionar ENTER
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    buscar(
                        txtBuscar.getText().trim(),
                        tablaGlobal
                    );
                }
            }
        });
        
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(MenuPrincipal.SILVER);

        JPanel contenido = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        contenido.setOpaque(false);

        contenido.add(titulo);
        contenido.add(sub);

        contenido.add(Box.createHorizontalStrut(650));

        contenido.add(lblBuscar);
        contenido.add(txtBuscar);

        banner.add(contenido, BorderLayout.CENTER);
        
        String[] cols = {
            "CÓDIGO",
            "ARTÍCULO",
            "CATEGORÍA",
            "ENTRADA",
            "SALIDAS",
            "STOCK"
        };

        modelo = new DefaultTableModel(cols, 0) {

            @Override
            public boolean isCellEditable(int r, int c) {

                return false;
            }
        };

        tablaGlobal = new JTable(modelo);

        estilizarTabla(tablaGlobal);

        JScrollPane scroll = new JScrollPane(tablaGlobal);

        scroll.setBackground(MenuPrincipal.DARK);
        scroll.getViewport().setBackground(MenuPrincipal.DARK);

        scroll.setBorder(BorderFactory.createEmptyBorder());

        GestorArchivos.cargar("productos.csv", modelo);

        panel.add(banner, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(crearForm(tablaGlobal), BorderLayout.SOUTH);

        return panel;
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(MenuPrincipal.DARK);
        tabla.setForeground(new Color(200,200,200));
        tabla.setFont(new Font("SansSerif",Font.PLAIN,12));
        tabla.setRowHeight(28); tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0,1));
        tabla.setSelectionBackground(new Color(40,32,0));
        tabla.setSelectionForeground(MenuPrincipal.GOLD);
        tabla.getTableHeader().setBackground(new Color(20,16,0));
        tabla.getTableHeader().setForeground(MenuPrincipal.GOLD);
        tabla.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,10));
        tabla.getTableHeader().setPreferredSize(new Dimension(0,32));
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,MenuPrincipal.GOLD_DARK));

        // 1) Default renderer primero
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,
                boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                setBackground(sel?new Color(40,32,0):(r%2==0?MenuPrincipal.DARK:new Color(20,20,20)));
                setForeground(sel?MenuPrincipal.GOLD:new Color(200,200,200));
                setFont(new Font("SansSerif",Font.PLAIN,12));
                setBorder(new EmptyBorder(0,8,0,8));
                return this;
            }
        });

        // 2) Renderer categoría morado (columna 2) — después del default
        tabla.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(JTable t,Object v,
                    boolean sel,boolean foc,int r,int c){

                super.getTableCellRendererComponent(t,v,sel,foc,r,c);

                setBackground(sel
                    ? new Color(40,32,0)
                    : (r%2==0 ? MenuPrincipal.DARK : new Color(20,20,20)));

                // Blanco normal / dorado seleccionado
                setForeground(sel ? MenuPrincipal.GOLD : Color.WHITE);

                setFont(new Font("SansSerif",Font.PLAIN,12));
                setBorder(new EmptyBorder(0,8,0,8));

                return this;
            }
        });

        // 3) Renderer stock verde (columna 5) — después del default
        tabla.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,
                boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                setBackground(sel?new Color(40,32,0):MenuPrincipal.DARK);
                setForeground(new Color(90,184,90));
                setFont(new Font("SansSerif",Font.BOLD,12));
                setHorizontalAlignment(CENTER);
                setBorder(new EmptyBorder(0,0,0,0));
                return this;
            }
        });
    }

    private JPanel crearForm(JTable tabla) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8)) {

            @Override
            protected void paintComponent(Graphics g) {

                g.setColor(new Color(13,13,13));
                g.fillRect(0,0,getWidth(),getHeight());

                g.setColor(new Color(30,30,30));
                g.drawLine(0,0,getWidth(),0);
            }
        };

        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(1200, 56));

        JTextField txtCod = campo(80);
        JTextField txtArt = campo(100);

        JComboBox<String> cmbCat = new JComboBox<>();

        estilizarCombo(cmbCat, 150);
        cargarCategorias(cmbCat);

        JTextField txtEnt = campo(50);
        JTextField txtSal = campo(50);

        JButton btnAdd = botonAccion(
            "+ AGREGAR",
            new Color(80,50,0),
            MenuPrincipal.WHITE
        );

        JButton btnDel = botonAccion(
            "ELIMINAR",
            new Color(30,30,30),
            MenuPrincipal.SILVER
        );

        panel.add(etiqueta("CÓDIGO"));
        panel.add(txtCod);

        panel.add(etiqueta("ARTÍCULO"));
        panel.add(txtArt);

        panel.add(etiqueta("CATEGORÍA"));
        panel.add(cmbCat);

        panel.add(etiqueta("ENTRADA"));
        panel.add(txtEnt);

        panel.add(etiqueta("SALIDAS"));
        panel.add(txtSal);

        panel.add(btnAdd);
        panel.add(btnDel);

        // AGREGAR PRODUCTO
        btnAdd.addActionListener(e -> {

            try {

                String cod = txtCod.getText().trim();
                String art = txtArt.getText().trim();

                String cat = (String) cmbCat.getSelectedItem();

                if (cat == null || cat.startsWith("--")) {
                    cat = "";
                }

                int ent = Integer.parseInt(txtEnt.getText().trim());
                int sal = Integer.parseInt(txtSal.getText().trim());

                if (!cod.isEmpty() && !art.isEmpty()) {

                    modelo.addRow(new Object[]{
                        cod,
                        art,
                        cat,
                        ent,
                        sal,
                        ent - sal
                    });
                    ordenarAlfabeticamente();

                    txtCod.setText("");
                    txtArt.setText("");
                    txtEnt.setText("");
                    txtSal.setText("");

                    cmbCat.setSelectedIndex(0);

                    GestorArchivos.guardar("productos.csv", modelo);
                }

            } catch(NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Entrada y Salidas deben ser números."
                );
            }
        });

        // ELIMINAR PRODUCTO
        btnDel.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                modelo.removeRow(fila);

                GestorArchivos.guardar(
                    "productos.csv",
                    modelo
                );

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una fila."
                );
            }
        });

        return panel;
    }
    
    private void ordenarAlfabeticamente() {

        TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(modelo);

        tablaGlobal.setRowSorter(sorter);

        // Orden ascendente por ARTÍCULO
        java.util.List<RowSorter.SortKey> sortKeys =
            new java.util.ArrayList<>();

        sortKeys.add(
            new RowSorter.SortKey(
                1,
                SortOrder.ASCENDING
            )
        );

        sorter.setSortKeys(sortKeys);

        sorter.sort();
    }
    
    
    private void cargarCategorias(JComboBox<String> cmb) {
        cmb.removeAllItems();
        cmb.addItem("-- Categoría --");
        List<String> cats = GestorArchivos.obtenerCategorias();
        for (String c : cats) cmb.addItem(c);
    }



    private void buscar(String texto, JTable tabla) {

        TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(modelo);

        tabla.setRowSorter(sorter);

        // ORDENAR ALFABÉTICAMENTE
        ArrayList<RowSorter.SortKey> sortKeys =
            new ArrayList<RowSorter.SortKey>();

        sortKeys.add(
            new RowSorter.SortKey(
                1,
                SortOrder.ASCENDING
            )
        );

        sorter.setSortKeys(sortKeys);

        sorter.sort();

        // Si está vacío
        if (texto.trim().isEmpty()) {

            sorter.setRowFilter(null);

            tabla.clearSelection();

            return;
        }

        // Buscar por artículo y categoría
        sorter.setRowFilter(RowFilter.regexFilter(
            "(?i)" + texto,
            1,
            2
        ));

        // ENCONTRÓ
        if (tabla.getRowCount() > 0) {

            tabla.setRowSelectionInterval(0, 0);

            int filaVista = tabla.getSelectedRow();

            int filaModelo =
                tabla.convertRowIndexToModel(filaVista);

            String producto =
                modelo.getValueAt(filaModelo, 1).toString();

            String stock =
                modelo.getValueAt(filaModelo, 5).toString();

            JOptionPane.showMessageDialog(
                this,
                "Producto: " + producto +
                "\nStock disponible: " + stock
            );

            // RESTAURAR TABLA
            sorter.setRowFilter(null);

            tabla.clearSelection();
        }

        // NO ENCONTRÓ
        else {

            JOptionPane.showMessageDialog(
                this,
                "✗ No existe: " + texto
            );

            // RESTAURAR TABLA
            sorter.setRowFilter(null);

            tabla.clearSelection();
        }
    }
    
    private void estilizarCombo(JComboBox<String> cmb, int ancho) {
        cmb.setBackground(new Color(26,26,26));
        cmb.setForeground(MenuPrincipal.WHITE);
        cmb.setFont(new Font("SansSerif",Font.PLAIN,12));
        cmb.setPreferredSize(new Dimension(ancho,28));
        cmb.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
    }

    private JButton botonVolver() {
        JButton b = new JButton("◀  VOLVER AL MENÚ"){
            boolean h=false;
            {addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseEntered(java.awt.event.MouseEvent e){h=true;repaint();}
                public void mouseExited(java.awt.event.MouseEvent e){h=false;repaint();}
            });}
            @Override protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g;
                g2.setColor(h?new Color(30,22,0):new Color(20,16,0)); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(MenuPrincipal.GOLD_DARK); g2.drawRect(0,0,getWidth()-1,getHeight()-1);
                g2.setFont(new Font("SansSerif",Font.BOLD,11)); g2.setColor(MenuPrincipal.GOLD);
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
            }
        };
        b.setPreferredSize(new Dimension(170,34));
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setContentAreaFilled(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JTextField campo(int w){
        JTextField f=new JTextField();
        f.setPreferredSize(new Dimension(w,28));
        f.setBackground(new Color(26,26,26));
        f.setForeground(MenuPrincipal.WHITE);
        f.setCaretColor(MenuPrincipal.GOLD);
        f.setFont(new Font("SansSerif",Font.PLAIN,12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50,50,50)),new EmptyBorder(0,6,0,6)));
        return f;
    }
    private JLabel etiqueta(String t){
        JLabel l=new JLabel(t);
        l.setFont(new Font("SansSerif",Font.BOLD,9));
        l.setForeground(MenuPrincipal.SILVER);
        return l;
    }
    private JButton botonAccion(String t,Color bg,Color fg){
        JButton b=new JButton(t);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("SansSerif",Font.BOLD,10));
        b.setPreferredSize(new Dimension(95,28));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}