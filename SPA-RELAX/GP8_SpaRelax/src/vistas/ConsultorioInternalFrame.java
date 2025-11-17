package vista;

import modelo.Consultorio;
import persistencia.ConsultorioData;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ConsultorioInternalFrame extends JInternalFrame {
    private JTextField tfId, tfUsos, tfEquipamiento;
    private JCheckBox chkApto;
    private ConsultorioData consultorioData;
    private JTable table;
    private DefaultTableModel modeloTabla;

    public ConsultorioInternalFrame() {
        super("Gestión de Consultorios", true, true, true, true);
        setSize(800, 550);
        consultorioData = new ConsultorioData();
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Consultorio"));
        panelForm.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfId = new JTextField(10);
        tfId.setEditable(false);
        tfId.setBackground(Color.LIGHT_GRAY);
        tfUsos = new JTextField(30);
        tfEquipamiento = new JTextField(30);
        chkApto = new JCheckBox("Apto");
        chkApto.setSelected(true);

        agregarCampo(panelForm, gbc, "ID:", tfId, 0, 0);
        agregarCampo(panelForm, gbc, "Usos:", tfUsos, 0, 1);
        agregarCampo(panelForm, gbc, "Equipamiento:", tfEquipamiento, 0, 2);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        panelForm.add(chkApto, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(240, 248, 255));

        JButton btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        JButton btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        JButton btnBaja = crearBoton("⬇️ Baja", new Color(231, 76, 60));
        JButton btnAlta = crearBoton("⬆️ Alta", new Color(46, 204, 113));
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        JButton btnLimpiar = crearBoton("🧹 Limpiar", new Color(149, 165, 166));

        btnAgregar.addActionListener(e -> agregarConsultorio());
        btnModificar.addActionListener(e -> modificarConsultorio());
        btnBaja.addActionListener(e -> bajaConsultorio());
        btnAlta.addActionListener(e -> altaConsultorio());
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

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Usos", "Equipamiento", "Apto", "Estado"}, 0) {
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
                    tfUsos.setText(modeloTabla.getValueAt(fila, 1).toString());
                    tfEquipamiento.setText(modeloTabla.getValueAt(fila, 2).toString());
                    chkApto.setSelected(modeloTabla.getValueAt(fila, 3).toString().equals("Sí"));
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

    private void agregarConsultorio() {
        try {
            if (tfUsos.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Los usos no pueden estar vacíos.");
                return;
            }

            Consultorio c = new Consultorio();
            c.setUsos(tfUsos.getText().trim());
            c.setEquipamiento(tfEquipamiento.getText().trim());
            c.setApto(chkApto.isSelected());
            c.setEstado(true);

            consultorioData.insertar(c);
            JOptionPane.showMessageDialog(this, "✅ Consultorio guardado con ID: " + c.getIdConsultorio());
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void modificarConsultorio() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un consultorio.");
                return;
            }

            Consultorio c = new Consultorio();
            c.setIdConsultorio(Integer.parseInt(tfId.getText()));
            c.setUsos(tfUsos.getText().trim());
            c.setEquipamiento(tfEquipamiento.getText().trim());
            c.setApto(chkApto.isSelected());
            c.setEstado(true);

            consultorioData.actualizar(c);
            JOptionPane.showMessageDialog(this, "✅ Consultorio modificado.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void bajaConsultorio() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un consultorio.");
                return;
            }
            consultorioData.bajaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Baja realizada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void altaConsultorio() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un consultorio.");
                return;
            }
            consultorioData.altaLogica(Integer.parseInt(tfId.getText()));
            JOptionPane.showMessageDialog(this, "✅ Alta realizada.");
            limpiarForm();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    private void limpiarForm() {
        tfId.setText("");
        tfUsos.setText("");
        tfEquipamiento.setText("");
        chkApto.setSelected(true);
        table.clearSelection();
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Consultorio> lista = consultorioData.listarTodos();
        for (Consultorio c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getIdConsultorio(), c.getUsos(), c.getEquipamiento(),
                c.isApto() ? "Sí" : "No", c.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
}