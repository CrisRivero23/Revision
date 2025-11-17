package vista;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    
    private JDesktopPane desktopPane;
    private JMenuBar menuBar;

    public MainFrame() {
        initComponents();
        setTitle("SPA Entre Dedos - Sistema de Gestión");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(230, 240, 250));
        
        setJMenuBar(crearMenuBar());
        add(desktopPane, BorderLayout.CENTER);
        
        agregarPanelBienvenida();
    }

    private JMenuBar crearMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(52, 152, 219));
        
        JMenu menuClientes = crearMenu("👤 Clientes");
        JMenuItem itemGestionClientes = crearMenuItem("Gestionar Clientes");
        itemGestionClientes.addActionListener(e -> abrirVentana(new ClienteInternalFrame()));
        menuClientes.add(itemGestionClientes);
        
        JMenu menuMasajistas = crearMenu("💆 Masajistas");
        JMenuItem itemGestionMasajistas = crearMenuItem("Gestionar Masajistas");
        itemGestionMasajistas.addActionListener(e -> abrirVentana(new MasajistaInternalFrame()));
        menuMasajistas.add(itemGestionMasajistas);
        
        JMenu menuTratamientos = crearMenu("💅 Tratamientos");
        JMenuItem itemGestionTratamientos = crearMenuItem("Gestionar Tratamientos");
        itemGestionTratamientos.addActionListener(e -> abrirVentana(new TratamientoInternalFrame()));
        menuTratamientos.add(itemGestionTratamientos);
        
        JMenu menuConsultorios = crearMenu("🏢 Consultorios");
        JMenuItem itemGestionConsultorios = crearMenuItem("Gestionar Consultorios");
        itemGestionConsultorios.addActionListener(e -> abrirVentana(new ConsultorioInternalFrame()));
        menuConsultorios.add(itemGestionConsultorios);
        
        JMenu menuDiasSpa = crearMenu("🌺 Días de Spa");
        JMenuItem itemGestionDiasSpa = crearMenuItem("Gestionar Días de Spa");
        itemGestionDiasSpa.addActionListener(e -> abrirVentana(new DiaDeSpaInternalFrame()));
        menuDiasSpa.add(itemGestionDiasSpa);
        
        JMenu menuSesiones = crearMenu("📅 Sesiones/Turnos");
        JMenuItem itemGestionSesiones = crearMenuItem("Gestionar Sesiones");
        itemGestionSesiones.addActionListener(e -> abrirVentana(new SesionTurnoInternalFrame()));
        menuSesiones.add(itemGestionSesiones);
        
        JMenu menuVentanas = crearMenu("🪟 Ventanas");
        JMenuItem itemCascada = crearMenuItem("Cascada");
        itemCascada.addActionListener(e -> organizarVentanasCascada());
        JMenuItem itemMosaico = crearMenuItem("Mosaico");
        itemMosaico.addActionListener(e -> organizarVentanasMosaico());
        JMenuItem itemCerrarTodas = crearMenuItem("Cerrar Todas");
        itemCerrarTodas.addActionListener(e -> cerrarTodasLasVentanas());
        menuVentanas.add(itemCascada);
        menuVentanas.add(itemMosaico);
        menuVentanas.addSeparator();
        menuVentanas.add(itemCerrarTodas);
        
        JMenu menuAyuda = crearMenu("❓ Ayuda");
        JMenuItem itemAcercaDe = crearMenuItem("Acerca de");
        itemAcercaDe.addActionListener(e -> mostrarAcercaDe());
        menuAyuda.add(itemAcercaDe);
        
        menuBar.add(menuClientes);
        menuBar.add(menuMasajistas);
        menuBar.add(menuTratamientos);
        menuBar.add(menuConsultorios);
        menuBar.add(menuDiasSpa);
        menuBar.add(menuSesiones);
        menuBar.add(menuVentanas);
        menuBar.add(menuAyuda);
        
        return menuBar;
    }

    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.BOLD, 13));
        return menu;
    }

    private JMenuItem crearMenuItem(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("Arial", Font.PLAIN, 12));
        return item;
    }

    private void abrirVentana(JInternalFrame frame) {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (JInternalFrame f : frames) {
            if (f.getClass().equals(frame.getClass())) {
                try {
                    f.setSelected(true);
                    f.toFront();
                    return;
                } catch (java.beans.PropertyVetoException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        desktopPane.add(frame);
        frame.setVisible(true);
        try {
            frame.setMaximum(true);
        } catch (java.beans.PropertyVetoException ex) {
            ex.printStackTrace();
        }
    }

    private void agregarPanelBienvenida() {
        JPanel panelBienvenida = new JPanel();
        panelBienvenida.setLayout(new BorderLayout());
        panelBienvenida.setBackground(new Color(230, 240, 250));
        
        JLabel lblTitulo = new JLabel("SPA Entre Dedos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(new Color(52, 152, 219));
        
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Integral", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 24));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        
        JLabel lblVersion = new JLabel("Versión 1.0 - Grupo 8 (2025)", SwingConstants.CENTER);
        lblVersion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblVersion.setForeground(new Color(150, 150, 150));
        
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBackground(new Color(230, 240, 250));
        panelCentro.add(Box.createVerticalGlue());
        panelCentro.add(lblTitulo);
        panelCentro.add(Box.createVerticalStrut(20));
        panelCentro.add(lblSubtitulo);
        panelCentro.add(Box.createVerticalStrut(40));
        panelCentro.add(lblVersion);
        panelCentro.add(Box.createVerticalGlue());
        
        panelBienvenida.add(panelCentro, BorderLayout.CENTER);
        
        desktopPane.add(panelBienvenida);
        panelBienvenida.setBounds(0, 0, 1200, 700);
    }

    private void organizarVentanasCascada() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        int x = 0;
        int y = 0;
        
        for (JInternalFrame frame : frames) {
            if (!frame.isIcon()) {
                try {
                    frame.setMaximum(false);
                    frame.reshape(x, y, 800, 600);
                    x += 30;
                    y += 30;
                } catch (java.beans.PropertyVetoException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void organizarVentanasMosaico() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        int count = 0;
        
        for (JInternalFrame frame : frames) {
            if (!frame.isIcon()) {
                count++;
            }
        }
        
        if (count == 0) return;
        
        int rows = (int) Math.sqrt(count);
        int cols = (count + rows - 1) / rows;
        
        Dimension desktopSize = desktopPane.getSize();
        int w = desktopSize.width / cols;
        int h = desktopSize.height / rows;
        
        int x = 0;
        int y = 0;
        int frameIndex = 0;
        
        for (JInternalFrame frame : frames) {
            if (!frame.isIcon()) {
                try {
                    frame.setMaximum(false);
                    frame.reshape(x, y, w, h);
                    x += w;
                    if (x + w > desktopSize.width) {
                        x = 0;
                        y += h;
                    }
                    frameIndex++;
                } catch (java.beans.PropertyVetoException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void cerrarTodasLasVentanas() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (JInternalFrame frame : frames) {
            frame.dispose();
        }
    }

    private void mostrarAcercaDe() {
        String mensaje = "SPA Entre Dedos - Sistema de Gestión\n\n" +
                        "Versión: 1.0\n" +
                        "Año: 2025\n\n" +
                        "Desarrollado por:\n" +
                        "Grupo 8\n" +
                        "- Pablo Mango\n" +
                        "- Milagros Alaniz\n" +
                        "- Alejo Mango\n" +
                        "- Cristian Rivero\n\n" +
                        "Instituto de Ciencia y Tecnología (ICT)";
        
        JOptionPane.showMessageDialog(this, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
}