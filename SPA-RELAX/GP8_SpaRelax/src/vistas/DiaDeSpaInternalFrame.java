package vista;

import modelo.DiaDeSpa;
import persistencia.DiaDeSpaData;
import persistencia.ClienteData;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DiaDeSpaInternalFrame extends JInternalFrame {
    private JTextField tfId, tfCodPack, tfFecha, tfHora, tfPreferencias;
    private JComboBox<String> cbCliente;
    private DiaDeSpaData diaSpaData;
    private ClienteData clienteData;
    private JTable table;
    private DefaultTableModel modeloTabla;
    private DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter fmtHora = DateTimeFormatter.ofPattern("HH:mm");

    public DiaDeSpaInternalFrame() {
        super("Gestión de Días de Spa", true, true, true, true);
        setSize(950, 650);
        diaSpaData = new DiaDeSpaData();
        clienteData = new ClienteData();
        initComponents();
        cargarClientes();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Día de Spa"));
        panelForm.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfId.setBackground(Color.LIGHT_GRAY);
        tfCodPack = new JTextField(15);
        cbCliente = new JComboBox<>();
        tfFecha = new JTextField(15);
        tfHora = new JTextField(10);
        tfPreferencias = new JTextField(30);

        JLabel lblFormatoFecha = new JLabel("Formato: YYYY-MM-DD");
        lblFormatoFecha.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFormatoFecha.setForeground(Color.GRAY);

        JLabel lblFormatoHora = new JLabel("Formato: HH:mm");
        lblFormatoHora.setFont(new Font("Arial", Font.ITALIC, 10));
        lblFormatoHora.setForeground(Color.GRAY);

        agregarCampo(panelForm, gbc, "ID:", tfId, 0, 0);
        agregarCampo(panelForm, gbc, "Código Pack:", tfCodPack, 0, 1);
        agregarCampo(panelForm, gbc, "Cliente:", cbCliente, 0, 2);
        agregarCampo(panelForm, gbc, "Fecha:", tfFecha, 2, 0);
        agregarCampo(panelForm, gbc, "Hora:", tfHora, 2, 1);
        agregarCampo(panelForm, gbc, "Preferencias:", tfPreferencias, 2, 2);

        gbc.gridx = 3;
        gbc.gridy = 0;
        panelForm.add(lblFormatoFecha, gbc);

        gbc.gridy = 1;
        panelForm.add(lblFormatoHora, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        JButton btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        JButton btnBaja = crearBoton("⬇️ Baja", new Color(231, 76, 60));
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        JButton btnLimpiar = crearBoton("🧹 Limpiar", new Color(149, 165, 166));

        btnAgregar.addActionListener(e -> agregarDiaSpa());
        btnModificar.addActionListener(e -> modificarDiaSpa());
        btnBaja.addActionListener(e -> bajaDiaSpa());
        btnRefrescar.addActionListener(e -> {
            cargarClientes();
            cargarTabla();
        });
        btnLimpiar.addActionListener(e -> limpiarForm());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnBaja);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        panelForm.add(panelBotones, gbc);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Código Pack", "Fecha", "Hora", "Cliente", "Preferencias", "Estado"}, 0) {
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
                    tfCodPack.setText(modeloTabla.getValueAt(fila, 1).toString());
                    tfFecha.setText(modeloTabla.getValueAt(fila, 2).toString());
                    tfHora.setText(modeloTabla.getValueAt(fila, 3).toString());
                    tfPreferencias.setText(modeloTabla.getValueAt(fila, 5) != null ? modeloTabla.getValueAt(fila, 5).toString() : "");
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

    private void cargarClientes() {
        cbCliente.removeAllItems();
        clienteData.listarTodos().forEach(c -> cbCliente.addItem(c.getIdCliente() + " - " + c.getNombre() + " " + c.getApellido()));
    }

    private void agregarDiaSpa() {
        try {
            if (!validarCampos()) return;

            DiaDeSpa d = new DiaDeSpa();
            d.setCodPack(tfCodPack.getText().trim());
            d.setFecha(LocalDate.parse(tfFecha.getText().trim(), fmtFecha));
            d.setHora(LocalTime.parse(tfHora.getText().trim(), fmtHora));
            d.setIdCliente(obtenerIdDeCombo(cbCliente));
            d.setPreferencias(tfPreferencias.getText().trim());
            d.setEstado(true);

            diaSpaData.insertar(d);
            JOptionPane.showMessageDialog(this, "✅ Día de Spa creado con ID: " + d.getIdDiaSpa());
            limpiarForm();
            cargarTabla();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Formato de fecha u hora incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarDiaSpa() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un día de spa.");
                return;
            }
            if (!validarCampos()) return;

            DiaDeSpa d = new DiaDeSpa();
            d.setIdDiaSpa(Integer.parseInt(tfId.getText()));
            d.setCodPack(tfCodPack.getText().trim());
            d.setFecha(LocalDate.parse(tfFecha.getText().trim(), fmtFecha));
            d.setHora(LocalTime.parse(tfHora.getText().trim(), fmtHora));
            d.setIdCliente(obtenerIdDeCombo(cbCliente));
            d.setPreferencias(tfPreferencias.getText().trim());
            d.setEstado(true);

            diaSpaData.actualizar(d);
            JOptionPane.showMessageDialog(this, "✅ Día de Spa modificado.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void bajaDiaSpa() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un día de spa.");
                return;
            }
            diaSpaData.bajaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Baja realizada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private boolean validarCampos() {
        if (tfCodPack.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El código de pack no puede estar vacío.");
            return false;
        }
        if (tfFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La fecha no puede estar vacía.");
            return false;
        }
        if (tfHora.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La hora no puede estar vacía.");
            return false;
        }
        if (cbCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione un cliente.");
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
        tfCodPack.setText("");
        tfFecha.setText("");
        tfHora.setText("");
        tfPreferencias.setText("");
        if (cbCliente.getItemCount() > 0) cbCliente.setSelectedIndex(0);
        table.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<DiaDeSpa> lista = diaSpaData.listarTodos();
        for (DiaDeSpa d : lista) {
            modeloTabla.addRow(new Object[]{
                d.getIdDiaSpa(),
                d.getCodPack(),
                d.getFecha().format(fmtFecha),
                d.getHora().format(fmtHora),
                "ID:" + d.getIdCliente(),
                d.getPreferencias(),
                d.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
}