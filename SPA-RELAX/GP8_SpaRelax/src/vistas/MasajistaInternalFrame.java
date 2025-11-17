package vista;

import modelo.Masajista;
import persistencia.MasajistaData;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MasajistaInternalFrame extends JInternalFrame {
    private JTextField tfId, tfNombre, tfMatricula, tfTelefono, tfEspecialidad;
    private MasajistaData masajistaData;
    private JTable table;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnModificar, btnBaja, btnAlta, btnLimpiar, btnRefrescar;

    public MasajistaInternalFrame() {
        super("Gestión de Masajistas", true, true, true, true);
        setSize(850, 600);
        masajistaData = new MasajistaData();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Masajista"));
        panelForm.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfId.setBackground(Color.LIGHT_GRAY);
        tfNombre = new JTextField(20);
        tfMatricula = new JTextField(15);
        tfTelefono = new JTextField(15);
        tfEspecialidad = new JTextField(20);

        agregarCampo(panelForm, gbc, "ID:", tfId, 0, 0);
        agregarCampo(panelForm, gbc, "Nombre:", tfNombre, 0, 1);
        agregarCampo(panelForm, gbc, "Matrícula:", tfMatricula, 2, 0);
        agregarCampo(panelForm, gbc, "Teléfono:", tfTelefono, 2, 1);
        agregarCampo(panelForm, gbc, "Especialidad:", tfEspecialidad, 0, 2);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(240, 248, 255));

        btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        btnBaja = crearBoton("⬇️ Baja", new Color(231, 76, 60));
        btnAlta = crearBoton("⬆️ Alta", new Color(46, 204, 113));
        btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        btnLimpiar = crearBoton("🧹 Limpiar", new Color(149, 165, 166));

        btnAgregar.addActionListener(e -> agregarMasajista());
        btnModificar.addActionListener(e -> modificarMasajista());
        btnBaja.addActionListener(e -> bajaMasajista());
        btnAlta.addActionListener(e -> altaMasajista());
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnLimpiar.addActionListener(e -> limpiarForm());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnBaja);
        panelBotones.add(btnAlta);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Matrícula", "Teléfono", "Especialidad", "Estado"}, 0) {
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
                    tfNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    tfMatricula.setText(modeloTabla.getValueAt(fila, 2).toString());
                    tfTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
                    tfEspecialidad.setText(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Masajistas"));

        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta, JTextField campo, int x, int y) {
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

    private void agregarMasajista() {
        try {
            if (!validarCampos()) return;

            Masajista m = new Masajista();
            m.setNombre(tfNombre.getText().trim());
            m.setMatricula(tfMatricula.getText().trim());
            m.setTelefono(tfTelefono.getText().trim());
            m.setEspecialidad(tfEspecialidad.getText().trim());
            m.setEstado(true);

            masajistaData.insertar(m);
            JOptionPane.showMessageDialog(this, "✅ Masajista guardado con ID: " + m.getIdMasajista(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMasajista() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un masajista de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!validarCampos()) return;

            Masajista m = new Masajista();
            m.setIdMasajista(Integer.parseInt(tfId.getText()));
            m.setNombre(tfNombre.getText().trim());
            m.setMatricula(tfMatricula.getText().trim());
            m.setTelefono(tfTelefono.getText().trim());
            m.setEspecialidad(tfEspecialidad.getText().trim());
            m.setEstado(true);

            masajistaData.actualizar(m);
            JOptionPane.showMessageDialog(this, "✅ Masajista modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bajaMasajista() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un masajista de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Dar de baja este masajista?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                masajistaData.bajaLogica(Integer.parseInt(tfId.getText()));
                JOptionPane.showMessageDialog(this, "✅ Baja realizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarForm();
                cargarTabla();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error en baja: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void altaMasajista() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un masajista de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            masajistaData.altaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Alta realizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error en alta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (tfNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            tfNombre.requestFocus();
            return false;
        }
        if (tfMatricula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La matrícula no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            tfMatricula.requestFocus();
            return false;
        }
        if (tfEspecialidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ La especialidad no puede estar vacía.", "Validación", JOptionPane.WARNING_MESSAGE);
            tfEspecialidad.requestFocus();
            return false;
        }
        return true;
    }

    private void limpiarForm() {
        tfId.setText("");
        tfNombre.setText("");
        tfMatricula.setText("");
        tfTelefono.setText("");
        tfEspecialidad.setText("");
        table.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Masajista> lista = masajistaData.listarTodos();
        for (Masajista m : lista) {
            modeloTabla.addRow(new Object[]{
                m.getIdMasajista(),
                m.getNombre(),
                m.getMatricula(),
                m.getTelefono(),
                m.getEspecialidad(),
                m.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
}