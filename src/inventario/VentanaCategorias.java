package inventario;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class VentanaCategorias extends JFrame {

    private DefaultTableModel modelo;
    private JTable tablaGlobal;

    public VentanaCategorias(JFrame menu) {
        setTitle("AutoLujos Guaman — Categorías");
        setSize(1100, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(MenuPrincipal.CARBON);
        setLayout(new BorderLayout());
        add(crearTopBar(menu), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
    }

    private JPanel crearTopBar(JFrame menu) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(13,13,13)); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(new Color(35,35,35)); g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };
        panel.setPreferredSize(new Dimension(780, 50));
        panel.setOpaque(false);

        JButton btnVolver = botonVolver();
        btnVolver.addActionListener(e -> { menu.setVisible(true); dispose(); });

        JLabel marca = new JLabel("AutoLujos Guaman");
        marca.setFont(new Font("Georgia", Font.BOLD, 13));
        marca.setForeground(MenuPrincipal.GOLD);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        left.setOpaque(false); left.add(btnVolver);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 14));
        right.setOpaque(false); right.add(marca);

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

                g.setColor(MenuPrincipal.PURPLE);
                g.fillRect(0,0,3,getHeight());

                g.setColor(new Color(35,35,35));
                g.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };

        banner.setOpaque(false);

        JLabel titulo = new JLabel("Categorías");

        titulo.setFont(new Font("Georgia", Font.BOLD, 18));
        titulo.setForeground(MenuPrincipal.PURPLE);

        JLabel sub = new JLabel("— Gestión de categorías de productos");

        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(MenuPrincipal.SILVER);

        // BUSCADOR
        JLabel lblBuscar = new JLabel("BUSCAR:");

        lblBuscar.setForeground(MenuPrincipal.WHITE);
        lblBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));

        JTextField txtBuscar = new JTextField();

        txtBuscar.setPreferredSize(new Dimension(150, 26));
        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.setForeground(Color.BLACK);
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // BUSCAR AUTOMÁTICO
        txtBuscar.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                buscar(txtBuscar.getText().trim(), tablaGlobal);
            }
        });

        // PANEL IZQUIERDA + DERECHA
        JPanel contenido = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 16, 10)
        );

        contenido.setOpaque(false);

        contenido.add(titulo);
        contenido.add(sub);

        // MOVER HACIA IZQUIERDA O DERECHA
        contenido.add(Box.createHorizontalStrut(500));

        contenido.add(lblBuscar);
        contenido.add(txtBuscar);

        banner.add(contenido, BorderLayout.CENTER);

        // TABLA
        String[] cols = {
            "CÓDIGO CAT.",
            "NOMBRE",
            "DESCRIPCIÓN"
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
            MenuPrincipal.PURPLE,
            new Color(40,0,60),
            new Color(20,0,40)
        );

        JScrollPane scroll = new JScrollPane(tablaGlobal);

        scroll.setBackground(MenuPrincipal.DARK);

        scroll.getViewport().setBackground(
            MenuPrincipal.DARK
        );

        scroll.setBorder(
            BorderFactory.createEmptyBorder()
        );

        GestorArchivos.cargar(
            "categorias.csv",
            modelo
        );

        panel.add(banner, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(crearForm(tablaGlobal), BorderLayout.SOUTH);

        return panel;
    }
    private JPanel crearForm(JTable tabla) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10)) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(13,13,13)); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(new Color(30,30,30)); g.drawLine(0,0,getWidth(),0);
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(780, 54));

        JTextField txtCod  = campo(80);
        JTextField txtNom  = campo(130);
        JTextField txtDesc = campo(180);

        // Búsqueda
        JTextField txtBuscar = campo(100);
        JButton btnBuscar = botonAccion("🔍 BUSCAR", new Color(30,30,60), MenuPrincipal.SILVER);

        panel.add(etiqueta("CÓD CAT.")); panel.add(txtCod);
        panel.add(etiqueta("NOMBRE"));   panel.add(txtNom);
        panel.add(etiqueta("DESC."));    panel.add(txtDesc);

        JButton btnAdd = botonAccion("+ AGREGAR", new Color(50,20,80), MenuPrincipal.WHITE);
        JButton btnDel = botonAccion("ELIMINAR",  new Color(30,30,30), MenuPrincipal.SILVER);

        panel.add(btnAdd); panel.add(btnDel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(etiqueta("BUSCAR ID")); panel.add(txtBuscar); panel.add(btnBuscar);

        btnAdd.addActionListener(e -> {
            String cod  = txtCod.getText().trim();
            String nom  = txtNom.getText().trim();
            String desc = txtDesc.getText().trim();
            if (!cod.isEmpty() && !nom.isEmpty()) {
                modelo.addRow(new Object[]{cod, nom, desc});
                txtCod.setText(""); txtNom.setText(""); txtDesc.setText("");
                GestorArchivos.guardar("categorias.csv", modelo);
            } else {
                JOptionPane.showMessageDialog(this, "Código y Nombre son obligatorios.");
            }
        });

        btnDel.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                modelo.removeRow(fila);
                GestorArchivos.guardar("categorias.csv", modelo);
            } else JOptionPane.showMessageDialog(this, "Seleccione una fila.");
        });

        btnBuscar.addActionListener(e -> buscar(txtBuscar.getText().trim(), tabla));
        txtBuscar.addActionListener(e -> buscar(txtBuscar.getText().trim(), tabla));

        return panel;
    }

    private boolean mensajeMostrado = false;

    private void buscar(String texto, JTable tabla) {

        TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(modelo);

        tabla.setRowSorter(sorter);

        // Mostrar todo si está vacío
        if (texto.trim().isEmpty()) {

            sorter.setRowFilter(null);
            tabla.clearSelection();

            // Reinicia el mensaje
            mensajeMostrado = false;

            return;
        }

        // Buscar en código y nombre
        sorter.setRowFilter(RowFilter.regexFilter(
            "(?i)" + texto,
            0,
            1
        ));

        // Si encuentra resultados
        if (tabla.getRowCount() > 0) {

            tabla.setRowSelectionInterval(0, 0);

            // Reinicia para futuras búsquedas
            mensajeMostrado = false;

        } else {

            // Mostrar solo una vez
            if (!mensajeMostrado) {

                JOptionPane.showMessageDialog(this,
                    "✗ No existe: " + texto);

                mensajeMostrado = true;
            }

            sorter.setRowFilter(null);
        }
    }
    
    private void estilizarTabla(JTable tabla, Color accent, Color selBg, Color headerBg) {
        tabla.setBackground(MenuPrincipal.DARK);
        tabla.setForeground(new Color(200,200,200));
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setRowHeight(28); tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0,1));
        tabla.setSelectionBackground(selBg);
        tabla.setSelectionForeground(accent);
        tabla.getTableHeader().setBackground(headerBg);
        tabla.getTableHeader().setForeground(accent);
        tabla.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,10));
        tabla.getTableHeader().setPreferredSize(new Dimension(0,32));
        tabla.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(80,40,120)));
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable t,Object v,
                boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                setBackground(sel?selBg:(r%2==0?MenuPrincipal.DARK:new Color(20,20,20)));
                setForeground(sel?accent:new Color(200,200,200));
                setFont(new Font("SansSerif",Font.PLAIN,12));
                setBorder(new EmptyBorder(0,8,0,8));
                return this;
            }
        });
    }

    private JButton botonVolver() {
        JButton b = new JButton("◀  VOLVER AL MENÚ") {
            boolean h=false;
            { addMouseListener(new java.awt.event.MouseAdapter(){
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

    private JTextField campo(int w) {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(w,28));
        f.setBackground(new Color(26,26,26));
        f.setForeground(MenuPrincipal.WHITE);
        f.setCaretColor(MenuPrincipal.GOLD);
        f.setFont(new Font("SansSerif",Font.PLAIN,12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50,50,50)),new EmptyBorder(0,6,0,6)));
        return f;
    }

    private JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif",Font.BOLD,9));
        l.setForeground(MenuPrincipal.SILVER);
        return l;
    }

    private JButton botonAccion(String t, Color bg, Color fg) {
        JButton b = new JButton(t);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("SansSerif",Font.BOLD,10));
        b.setPreferredSize(new Dimension(100,28));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(new Color(60,60,60)));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}