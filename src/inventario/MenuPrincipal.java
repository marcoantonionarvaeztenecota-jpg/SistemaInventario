package inventario;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    static final Color CARBON    = new Color(10, 10, 10);
    static final Color DARK      = new Color(17, 17, 17);
    static final Color DARK2     = new Color(26, 26, 26);
    static final Color GOLD      = new Color(201, 168, 76);
    static final Color GOLD_DARK = new Color(139, 105, 20);
    static final Color SILVER    = new Color(160, 160, 160);
    static final Color WHITE     = new Color(245, 240, 232);
    static final Color ORANGE    = new Color(232, 118, 26);
    static final Color GREEN     = new Color(58, 138, 58);
    static final Color YELLOW    = new Color(201, 168, 76);
    static final Color PURPLE    = new Color(120, 80, 180);

    public MenuPrincipal() {
        setTitle("AutoLujos Guaman — Sistema de Inventario");
        setSize(520, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(CARBON);
        setLayout(new BorderLayout());

        add(crearHeader(), BorderLayout.NORTH);
        add(crearBotones(), BorderLayout.CENTER);
        add(crearFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel crearHeader() {
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(20,16,0),
                    getWidth(), getHeight(), new Color(10,10,10));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                g2.setColor(new Color(201,168,76,12));
                g2.setStroke(new BasicStroke(0.5f));
                for (int i = -getHeight(); i < getWidth(); i += 25)
                    g2.drawLine(i, 0, i+getHeight(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(520, 180));
        panel.setOpaque(false);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 30));
        topRow.setOpaque(false);

        // Logo
        JPanel logoPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GOLD); g2.setStroke(new BasicStroke(2f));
                g2.drawOval(5, 5, 60, 60);
                g2.setColor(new Color(30,22,0)); g2.fillOval(6, 6, 58, 58);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 28));
                g2.setColor(GOLD);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("🚗", 35-fm.stringWidth("🚗")/2, 42);
            }
        };
        logoPanel.setPreferredSize(new Dimension(80, 80));
        logoPanel.setOpaque(false);

        JPanel textoPanel = new JPanel();
        textoPanel.setLayout(new BoxLayout(textoPanel, BoxLayout.Y_AXIS));
        textoPanel.setOpaque(false);

        JLabel nombre = new JLabel("AutoLujos Guaman");
        nombre.setFont(new Font("Georgia", Font.BOLD, 28));
        nombre.setForeground(GOLD);

        JLabel sub = new JLabel("SISTEMA DE GESTIÓN DE INVENTARIO");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        sub.setForeground(SILVER);

        textoPanel.add(nombre);
        textoPanel.add(Box.createVerticalStrut(4));
        textoPanel.add(sub);

        topRow.add(logoPanel);
        topRow.add(textoPanel);
        panel.add(topRow, BorderLayout.CENTER);

        JPanel lineaPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(160,160,160,80));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                String txt = "— SELECCIONE UN MÓDULO —";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(txt, (getWidth()-fm.stringWidth(txt))/2, 20);
            }
        };
        lineaPanel.setPreferredSize(new Dimension(520, 28));
        lineaPanel.setOpaque(false);
        panel.add(lineaPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearBotones() {
        // 2x2 grid: Productos, Entradas, Salidas, Categoría
        JPanel panel = new JPanel(new GridLayout(2, 2, 1, 1));
        panel.setBackground(GOLD_DARK);

        panel.add(crearMenuBtn("📦", "PRODUCTOS",  "Catálogo de artículos",  ORANGE, e -> abrirVentana(new VentanaProductos(this))));
        panel.add(crearMenuBtn("⬇",  "ENTRADAS",   "Registro de ingresos",   GREEN,  e -> abrirVentana(new VentanaEntradas(this))));
        panel.add(crearMenuBtn("⬆",  "SALIDAS",    "Registro de egresos",    YELLOW, e -> abrirVentana(new VentanaSalidas(this))));
        panel.add(crearMenuBtn("🏷", "CATEGORÍAS", "Gestión de categorías",  PURPLE, e -> abrirVentana(new VentanaCategorias(this))));

        return panel;
    }

    private JButton crearMenuBtn(String icon, String titulo, String desc,
                                  Color accentColor, java.awt.event.ActionListener action) {
        JButton btn = new JButton() {
            boolean hover = false;
            { addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { hover=true; repaint(); }
                public void mouseExited(java.awt.event.MouseEvent e)  { hover=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? DARK2 : DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(accentColor);
                g2.fillRect(0, getHeight()-3, getWidth(), 3);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 30));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(icon, (getWidth()-fm.stringWidth(icon))/2, 65);
                g2.setFont(new Font("Georgia", Font.BOLD, 14));
                g2.setColor(accentColor);
                fm = g2.getFontMetrics();
                g2.drawString(titulo, (getWidth()-fm.stringWidth(titulo))/2, 92);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.setColor(SILVER);
                fm = g2.getFontMetrics();
                g2.drawString(desc, (getWidth()-fm.stringWidth(desc))/2, 110);
            }
        };
        btn.setPreferredSize(new Dimension(150, 130));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private JPanel crearFooter() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(new Color(13,13,13)); g.fillRect(0,0,getWidth(),getHeight());
                g.setColor(new Color(30,30,30)); g.drawLine(0,0,getWidth(),0);
            }
        };
        panel.setPreferredSize(new Dimension(520, 36));
        panel.setOpaque(false);

        JLabel izq = new JLabel("  AutoLujos Guaman © 2026");
        izq.setFont(new Font("SansSerif", Font.PLAIN, 10));
        izq.setForeground(new Color(70,70,70));

        JLabel der = new JLabel("● Sistema activo  ");
        der.setFont(new Font("SansSerif", Font.PLAIN, 10));
        der.setForeground(new Color(58,138,58));

        panel.add(izq, BorderLayout.WEST);
        panel.add(der, BorderLayout.EAST);
        return panel;
    }

    private void abrirVentana(JFrame ventana) {
        this.setVisible(false);
        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }
}