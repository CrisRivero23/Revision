package vista;

import modelo.SesionTurno;
import persistencia.SesionTurnoData;
import persistencia.ClienteData;
import persistencia.MasajistaData;
import persistencia.TratamientoData;
import persistencia.ConsultorioData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class SesionTurnoInternalFrame extends JInternalFrame {
    private JTextField tfId, tfFechaInicio, tfFechaFin, tfObservaciones;
    private JComboBox<String> cbCliente, cbMasajista, cbTratamiento, cbConsultorio;
    private SesionTurnoData sesionData;
    private JTable table;
    private DefaultTableModel modeloTabla;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SesionTurnoInternalFrame() {
        super("Gestión de Sesiones/Turnos", true, true, true, true);
        setSize(1000, 700);
        sesionData = new SesionTurnoData();
        initComponents();
        cargarCombos();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Sesión"));
        panelForm.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfId.setBackground(Color.LIGHT_GRAY);
        cbCliente = new JComboBox<>();
        cbMasajista = new JComboBox<>();
        cbTratamiento = new JComboBox<>();
        cbConsultorio = new JComboBox<>();
        tfFechaInicio = new JTextField(15);
        tfFechaFin = new JTextField(15);
        tfObservaciones = new JTextField(30);

        JLabel lblFormato = new JLabel("Formato: YYYY-MM-DD HH:mm");
        lblFormato.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFormato.setForeground(Color.GRAY);

        agregarCampo(panelForm, gbc, "ID:", tfId, 0, 0);
        agregarCampo(panelForm, gbc, "Cliente:", cbCliente, 0, 1);
        agregarCampo(panelForm, gbc, "Masajista:", cbMasajista, 0, 2);
        agregarCampo(panelForm, gbc, "Tratamiento:", cbTratamiento, 0, 3);
        agregarCampo(panelForm, gbc, "Consultorio:", cbConsultorio, 2, 0);
        agregarCampo(panelForm, gbc, "Inicio:", tfFechaInicio, 2, 1);
        agregarCampo(panelForm, gbc, "Fin:", tfFechaFin, 2, 2);
        agregarCampo(panelForm, gbc, "Observaciones:", tfObservaciones, 2, 3);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelForm.add(lblFormato, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        JButton btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        JButton btnBaja = crearBoton("⬇️ Baja", new Color(231, 76, 60));
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        JButton btnLimpiar = crearBoton("🧹 Limpiar", new Color(149, 165, 166));

        btnAgregar.addActionListener(e -> agregarSesion());
        btnModificar.addActionListener(e -> modificarSesion());
        btnBaja.addActionListener(e -> bajaSesion());
        btnRefrescar.addActionListener(e -> {
            cargarCombos();
            cargarTabla();
        });
        btnLimpiar.addActionListener(e -> limpiarForm());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnBaja);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        panelForm.add(panelBotones, gbc);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Cliente", "Masajista", "Tratamiento", "Consultorio", "Inicio", "Fin", "Observaciones", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(modeloTabla);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = table.getSelectedRow();
                if (fila != -1) {
                    tfId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    tfObservaciones.setText(modeloTabla.getValueAt(fila, 7) != null ? modeloTabla.getValueAt(fila, 7).toString() : "");
                }
            }
        });

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JComponent campo, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lbl, gbc);
        gbc.gridx = x + 1;
        panel.add(campo, gbc);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        return btn;
    }

    private void cargarCombos() {
        cbCliente.removeAllItems();
        cbMasajista.removeAllItems();
        cbTratamiento.removeAllItems();
        cbConsultorio.removeAllItems();

        ClienteData cd = new ClienteData();
        cd.listarTodos().forEach(c -> cbCliente.addItem(c.getIdCliente() + " - " + c.getNombre() + " " + c.getApellido()));

        MasajistaData md = new MasajistaData();
        md.listarActivos().forEach(m -> cbMasajista.addItem(m.getIdMasajista() + " - " + m.getNombre()));

        TratamientoData td = new TratamientoData();
        td.listarActivos().forEach(t -> cbTratamiento.addItem(t.getIdTratamiento() + " - " + t.getNombre()));

        ConsultorioData cod = new ConsultorioData();
        cod.listarActivos().forEach(c -> cbConsultorio.addItem(c.getIdConsultorio() + " - " + c.getUsos()));
    }

    private void agregarSesion() {
        try {
            if (!validarCampos()) return;

            SesionTurno s = new SesionTurno();
            s.setIdCliente(obtenerIdDeCombo(cbCliente));
            s.setIdMasajista(obtenerIdDeCombo(cbMasajista));
            s.setIdTratamiento(obtenerIdDeCombo(cbTratamiento));
            s.setIdConsultorio(obtenerIdDeCombo(cbConsultorio));
            s.setFechaHoraInicio(LocalDateTime.parse(tfFechaInicio.getText().trim(), fmt));
            
            if (!tfFechaFin.getText().trim().isEmpty()) {
                s.setFechaHoraFin(LocalDateTime.parse(tfFechaFin.getText().trim(), fmt));
            }
            
            s.setObservaciones(tfObservaciones.getText().trim());
            s.setEstado(true);

            sesionData.insertar(s);
            JOptionPane.showMessageDialog(this, "✅ Sesión creada con ID: " + s.getIdSesion());
            limpiarForm();
            cargarTabla();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Formato de fecha incorrecto. Use: YYYY-MM-DD HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarSesion() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione una sesión.");
                return;
            }
            if (!validarCampos()) return;

            SesionTurno s = new SesionTurno();
            s.setIdSesion(Integer.parseInt(tfId.getText()));
            s.setIdCliente(obtenerIdDeCombo(cbCliente));
            s.setIdMasajista(obtenerIdDeCombo(cbMasajista));
            s.setIdTratamiento(obtenerIdDeCombo(cbTratamiento));
            s.setIdConsultorio(obtenerIdDeCombo(cbConsultorio));
            s.setFechaHoraInicio(LocalDateTime.parse(tfFechaInicio.getText().trim(), fmt));
            
            if (!tfFechaFin.getText().trim().isEmpty()) {
                s.setFechaHoraFin(LocalDateTime.parse(tfFechaFin.getText().trim(), fmt));
            }
            
            s.setObservaciones(tfObservaciones.getText().trim());
            s.setEstado(true);

            sesionData.actualizar(s);
            JOptionPane.showMessageDialog(this, "✅ Sesión modificada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void bajaSesion() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione una sesión.");
                return;
            }
            sesionData.bajaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Baja realizada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private boolean validarCampos() {
        if (cbCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un cliente.");
            return false;
        }
        if (cbMasajista.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un masajista.");
            return false;
        }
        if (cbTratamiento.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un tratamiento.");
            return false;
        }
        if (cbConsultorio.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un consultorio.");
            return false;
        }
        if (tfFechaInicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La fecha de inicio no puede estar vacía.");
            return false;
        }
        return true;
    }

    private int obtenerIdDeCombo(JComboBox<String> combo) {
        String seleccion = (String) combo.getSelectedItem();
        return Integer.parseInt(seleccion.split(" - ")[0]);
    }

    private void limpiarForm() {
        tfId.setText("");
        tfFechaInicio.setText("");
        tfFechaFin.setText("");
        tfObservaciones.setText("");
        if (cbCliente.getItemCount() > 0) cbCliente.setSelectedIndex(0);
        if (cbMasajista.getItemCount() > 0) cbMasajista.setSelectedIndex(0);
        if (cbTratamiento.getItemCount() > 0) cbTratamiento.setSelectedIndex(0);
        if (cbConsultorio.getItemCount() > 0) cbConsultorio.setSelectedIndex(0);
        table.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<SesionTurno> lista = sesionData.listarTodas();
        for (SesionTurno s : lista) {
            modeloTabla.addRow(new Object[]{
                s.getIdSesion(),
                "ID:" + s.getIdCliente(),
                "ID:" + s.getIdMasajista(),
                "ID:" + s.getIdTratamiento(),
                "ID:" + s.getIdConsultorio(),
                s.getFechaHoraInicio() != null ? s.getFechaHoraInicio().format(fmt) : "",
                s.getFechaHoraFin() != null ? s.getFechaHoraFin().format(fmt) : "",
                s.getObservaciones(),
                s.isEstado() ? "Activa" : "Inactiva"
            });
        }
    }
}