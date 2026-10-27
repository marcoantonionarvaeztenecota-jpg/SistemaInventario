package inventario;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaEntradas extends JFrame {

    private DefaultTableModel modelo;
    private JTable tablaGlobal;

    public VentanaEntradas(JFrame menu) {

        setTitle("AutoLujos Guaman — Entradas");
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

                g.setColor(MenuPrincipal.GREEN);
                g.fillRect(0,0,3,getHeight());

                g.setColor(new Color(35,35,35));
                g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };

        banner.setOpaque(false);

        JLabel titulo = new JLabel("Entradas");

        titulo.setFont(new Font("Georgia", Font.BOLD, 18));
        titulo.setForeground(MenuPrincipal.GREEN);

        JLabel sub = new JLabel("— Registro de ingresos");

        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(MenuPrincipal.SILVER);

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
            public void keyReleased(KeyEvent e) {

                buscar(txtBuscar.getText().trim(), tablaGlobal);
            }
        });

        JPanel contenido = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));

        contenido.setOpaque(false);

        contenido.add(titulo);
        contenido.add(sub);

        // MOVER BUSCADOR MÁS A LA IZQUIERDA
        contenido.add(Box.createHorizontalStrut(650));

        contenido.add(lblBuscar);
        contenido.add(txtBuscar);

        banner.add(contenido, BorderLayout.CENTER);

        String[] cols = {
            "CÓDIGO",
            "ARTÍCULO",
            "CATEGORÍA",
            "FECHA",
            "CANTIDAD"
        };

        modelo = new DefaultTableModel(cols, 0) {

            @Override
            public boolean isCellEditable(int r, int c) {

                return false;
            }
        };

        tablaGlobal = new JTable(modelo);

        estilizarTabla(
            tablaGlobal,
            MenuPrincipal.GREEN,
            new Color(0,40,0),
            new Color(0,20,0)
        );

        JScrollPane scroll = new JScrollPane(tablaGlobal);

        scroll.setBackground(MenuPrincipal.DARK);

        scroll.getViewport().setBackground(MenuPrincipal.DARK);

        scroll.setBorder(BorderFactory.createEmptyBorder());

        GestorArchivos.cargar("entradas.csv", modelo);

        panel.add(banner, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(crearForm(tablaGlobal), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearForm(JTable tabla) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 8)) {

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

        JComboBox<String> cmbProducto = new JComboBox<>();

        estilizarCombo(cmbProducto, 190);

        List<String[]> productos = GestorArchivos.obtenerProductos();

        cmbProducto.addItem("-- Seleccionar --");

        for (String[] p : productos) {

            cmbProducto.addItem(p[0] + " - " + p[1]);
        }

        JComboBox<String> cmbCat = new JComboBox<>();

        estilizarCombo(cmbCat, 140);

        cmbCat.addItem("-- Categoría --");

        List<String> cats = GestorArchivos.obtenerCategorias();

        for (String c : cats) {

            cmbCat.addItem(c);
        }

        JTextField txtFec = campo(95);

        txtFec.setText(LocalDate.now().toString());

        JTextField txtCant = campo(55);

        JButton btnAdd = botonAccion(
            "+ AGREGAR",
            new Color(30,70,30),
            MenuPrincipal.WHITE
        );

        JButton btnDel = botonAccion(
            "ELIMINAR",
            new Color(30,30,30),
            MenuPrincipal.SILVER
        );

        panel.add(etiqueta("PRODUCTO"));
        panel.add(cmbProducto);

        panel.add(etiqueta("CATEGORÍA"));
        panel.add(cmbCat);

        panel.add(etiqueta("FECHA"));
        panel.add(txtFec);

        panel.add(etiqueta("CANTIDAD"));
        panel.add(txtCant);

        panel.add(btnAdd);
        panel.add(btnDel);

        // AGREGAR
        btnAdd.addActionListener(e -> {

            try {

                String seleccion =
                    (String) cmbProducto.getSelectedItem();

                if (seleccion == null ||
                    seleccion.startsWith("--")) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un producto."
                    );

                    return;
                }

                String[] partes =
                    seleccion.split(" - ", 2);

                String cod = partes[0].trim();

                String art =
                    partes.length > 1
                    ? partes[1].trim()
                    : "";

                String cat =
                    (String) cmbCat.getSelectedItem();

                if (cat == null ||
                    cat.startsWith("--")) {

                    cat = "";
                }

                String fec =
                    txtFec.getText().trim();

                int cant =
                    Integer.parseInt(
                        txtCant.getText().trim()
                    );

                modelo.addRow(new Object[] {
                    cod,
                    art,
                    cat,
                    fec,
                    cant
                });

                txtCant.setText("");

                cmbCat.setSelectedIndex(0);

                GestorArchivos.guardar(
                    "entradas.csv",
                    modelo
                );

                GestorArchivos.actualizarProducto(
                    cod,
                    art,
                    cant,
                    0
                );

                JOptionPane.showMessageDialog(
                    this,
                    "✓ Entrada registrada"
                );

            } catch(NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Cantidad debe ser número."
                );
            }
        });

        // ELIMINAR
        btnDel.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila >= 0) {

                String cod =
                    modelo.getValueAt(fila,0).toString();

                String art =
                    modelo.getValueAt(fila,1).toString();

                int cant =
                    Integer.parseInt(
                        modelo.getValueAt(fila,4).toString()
                    );

                modelo.removeRow(fila);

                GestorArchivos.guardar(
                    "entradas.csv",
                    modelo
                );

                GestorArchivos.actualizarProducto(
                    cod,
                    art,
                    -cant,
                    0
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

    private boolean mensajeMostrado = false;

    private void buscar(String texto, JTable tabla) {

        TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(modelo);

        tabla.setRowSorter(sorter);

        if (texto.trim().isEmpty()) {

            sorter.setRowFilter(null);

            tabla.clearSelection();

            // Reiniciar mensaje
            mensajeMostrado = false;

            return;
        }

        sorter.setRowFilter(RowFilter.regexFilter(
            "(?i)" + texto,
            1,
            2
        ));

        if (tabla.getRowCount() > 0) {

            tabla.setRowSelectionInterval(0,0);

            // Reiniciar mensaje
            mensajeMostrado = false;

        } else {

            // Mostrar solo una vez
            if (!mensajeMostrado) {

                JOptionPane.showMessageDialog(
                    this,
                    "✗ No existe: " + texto
                );

                mensajeMostrado = true;
            }

            sorter.setRowFilter(null);
        }
    }
    
    private void estilizarTabla(
        JTable tabla,
        Color accent,
        Color selBg,
        Color headerBg
    ) {

        tabla.setBackground(MenuPrincipal.DARK);

        tabla.setForeground(new Color(200,200,200));

        tabla.setFont(new Font("SansSerif",Font.PLAIN,12));

        tabla.setRowHeight(28);

        tabla.setShowGrid(false);

        tabla.setIntercellSpacing(
            new Dimension(0,1)
        );

        tabla.setSelectionBackground(selBg);

        tabla.setSelectionForeground(accent);

        tabla.getTableHeader().setBackground(headerBg);

        tabla.getTableHeader().setForeground(accent);

        tabla.getTableHeader().setFont(
            new Font("SansSerif",Font.BOLD,10)
        );

        tabla.getTableHeader().setPreferredSize(
            new Dimension(0,32)
        );

        tabla.setDefaultRenderer(
            Object.class,
            new DefaultTableCellRenderer(){

            @Override
            public Component getTableCellRendererComponent(
                JTable t,Object v,
                boolean sel,boolean foc,
                int r,int c){

                super.getTableCellRendererComponent(
                    t,v,sel,foc,r,c
                );

                setBackground(
                    sel
                    ? selBg
                    : (r%2==0
                        ? MenuPrincipal.DARK
                        : new Color(20,20,20))
                );

                setForeground(
                    sel
                    ? accent
                    : new Color(200,200,200)
                );

                setFont(
                    new Font("SansSerif",Font.PLAIN,12)
                );

                setBorder(
                    new EmptyBorder(0,8,0,8)
                );

                return this;
            }
        });
    }

    private void estilizarCombo(
        JComboBox<String> cmb,
        int ancho
    ) {

        cmb.setBackground(new Color(26,26,26));

        cmb.setForeground(MenuPrincipal.WHITE);

        cmb.setFont(
            new Font("SansSerif",Font.PLAIN,12)
        );

        cmb.setPreferredSize(
            new Dimension(ancho,28)
        );

        cmb.setBorder(
            BorderFactory.createLineBorder(
                new Color(50,50,50)
            )
        );
    }

    private JButton botonVolver() {

        JButton b =
            new JButton("◀  VOLVER AL MENÚ") {

            boolean h=false;

            {
                addMouseListener(
                    new java.awt.event.MouseAdapter(){

                    public void mouseEntered(
                        java.awt.event.MouseEvent e){

                        h=true;
                        repaint();
                    }

                    public void mouseExited(
                        java.awt.event.MouseEvent e){

                        h=false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g){

                Graphics2D g2 =
                    (Graphics2D)g;

                g2.setColor(
                    h
                    ? new Color(30,22,0)
                    : new Color(20,16,0)
                );

                g2.fillRect(
                    0,0,getWidth(),getHeight()
                );

                g2.setColor(
                    MenuPrincipal.GOLD_DARK
                );

                g2.drawRect(
                    0,0,getWidth()-1,getHeight()-1
                );

                g2.setFont(
                    new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                    )
                );

                g2.setColor(MenuPrincipal.GOLD);

                FontMetrics fm =
                    g2.getFontMetrics();

                g2.drawString(
                    getText(),
                    (getWidth()-fm.stringWidth(
                        getText()))/2,
                    (getHeight()
                    +fm.getAscent()
                    -fm.getDescent())/2
                );
            }
        };

        b.setPreferredSize(
            new Dimension(170,34)
        );

        b.setBorderPainted(false);

        b.setFocusPainted(false);

        b.setContentAreaFilled(false);

        b.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return b;
    }

    private JTextField campo(int w){

        JTextField f = new JTextField();

        f.setPreferredSize(
            new Dimension(w,28)
        );

        f.setBackground(new Color(26,26,26));

        f.setForeground(MenuPrincipal.WHITE);

        f.setCaretColor(MenuPrincipal.GOLD);

        f.setFont(
            new Font("SansSerif",Font.PLAIN,12)
        );

        f.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(50,50,50)
                ),
                new EmptyBorder(0,6,0,6)
            )
        );

        return f;
    }

    private JLabel etiqueta(String t){

        JLabel l = new JLabel(t);

        l.setFont(
            new Font("SansSerif",Font.BOLD,9)
        );

        l.setForeground(MenuPrincipal.SILVER);

        return l;
    }

    private JButton botonAccion(
        String t,
        Color bg,
        Color fg
    ){

        JButton b = new JButton(t);

        b.setBackground(bg);

        b.setForeground(fg);

        b.setFont(
            new Font("SansSerif",Font.BOLD,10)
        );

        b.setPreferredSize(
            new Dimension(95,28)
        );

        b.setFocusPainted(false);

        b.setBorder(
            BorderFactory.createLineBorder(
                new Color(60,60,60)
            )
        );

        b.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        return b;
    }
}