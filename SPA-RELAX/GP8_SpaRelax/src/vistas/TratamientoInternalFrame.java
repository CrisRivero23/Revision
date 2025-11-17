package vista;

import modelo.Tratamiento;
import persistencia.TratamientoData;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TratamientoInternalFrame extends JInternalFrame {
    private JTextField tfId, tfNombre, tfTipo, tfDuracion, tfCosto;
    private TratamientoData tratamientoData;
    private JTable table;
    private DefaultTableModel modeloTabla;

    public TratamientoInternalFrame() {
        super("Gestión de Tratamientos", true, true, true, true);
        setSize(850, 600);
        tratamientoData = new TratamientoData();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Tratamiento"));
        panelForm.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfId.setBackground(Color.LIGHT_GRAY);
        tfNombre = new JTextField(20);
        tfTipo = new JTextField(20);
        tfDuracion = new JTextField(10);
        tfCosto = new JTextField(10);

        agregarCampo(panelForm, gbc, "ID:", tfId, 0, 0);
        agregarCampo(panelForm, gbc, "Nombre:", tfNombre, 0, 1);
        agregarCampo(panelForm, gbc, "Tipo:", tfTipo, 0, 2);
        agregarCampo(panelForm, gbc, "Duración (min):", tfDuracion, 2, 0);
        agregarCampo(panelForm, gbc, "Costo ($):", tfCosto, 2, 1);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        JButton btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        JButton btnBaja = crearBoton("⬇️ Baja", new Color(231, 76, 60));
        JButton btnAlta = crearBoton("⬆️ Alta", new Color(46, 204, 113));
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        JButton btnLimpiar = crearBoton("🧹 Limpiar", new Color(149, 165, 166));

        btnAgregar.addActionListener(e -> agregarTratamiento());
        btnModificar.addActionListener(e -> modificarTratamiento());
        btnBaja.addActionListener(e -> bajaTratamiento());
        btnAlta.addActionListener(e -> altaTratamiento());
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

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Tipo", "Duración", "Costo", "Estado"}, 0) {
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
                    tfTipo.setText(modeloTabla.getValueAt(fila, 2).toString());
                    tfDuracion.setText(modeloTabla.getValueAt(fila, 3).toString());
                    tfCosto.setText(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
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

    private void agregarTratamiento() {
        try {
            if (!validarCampos()) return;

            Tratamiento t = new Tratamiento();
            t.setNombre(tfNombre.getText().trim());
            t.setTipo(tfTipo.getText().trim());
            t.setDuracionMin(Integer.parseInt(tfDuracion.getText().trim()));
            t.setCosto(Double.parseDouble(tfCosto.getText().trim()));
            t.setEstado(true);

            tratamientoData.insertar(t);
            JOptionPane.showMessageDialog(this, "✅ Tratamiento guardado con ID: " + t.getIdTratamiento());
            limpiarForm();
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Duración o costo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarTratamiento() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un tratamiento.");
                return;
            }
            if (!validarCampos()) return;

            Tratamiento t = new Tratamiento();
            t.setIdTratamiento(Integer.parseInt(tfId.getText()));
            t.setNombre(tfNombre.getText().trim());
            t.setTipo(tfTipo.getText().trim());
            t.setDuracionMin(Integer.parseInt(tfDuracion.getText().trim()));
            t.setCosto(Double.parseDouble(tfCosto.getText().trim()));
            t.setEstado(true);

            tratamientoData.actualizar(t);
            JOptionPane.showMessageDialog(this, "✅ Tratamiento modificado.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void bajaTratamiento() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un tratamiento.");
                return;
            }
            tratamientoData.bajaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Baja realizada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void altaTratamiento() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un tratamiento.");
                return;
            }
            tratamientoData.altaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Alta realizada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private boolean validarCampos() {
        if (tfNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El nombre no puede estar vacío.");
            return false;
        }
        try {
            int duracion = Integer.parseInt(tfDuracion.getText().trim());
            if (duracion <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ La duración debe ser un número válido mayor a 0.");
            return false;
        }
        try {
            double costo = Double.parseDouble(tfCosto.getText().trim());
            if (costo <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ El costo debe ser un número válido mayor a 0.");
            return false;
        }
        return true;
    }

    private void limpiarForm() {
        tfId.setText("");
        tfNombre.setText("");
        tfTipo.setText("");
        tfDuracion.setText("");
        tfCosto.setText("");
        table.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Tratamiento> lista = tratamientoData.listarTodos();
        for (Tratamiento t : lista) {
            modeloTabla.addRow(new Object[]{
                t.getIdTratamiento(), t.getNombre(), t.getTipo(), 
                t.getDuracionMin(), t.getCosto(), 
                t.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
}