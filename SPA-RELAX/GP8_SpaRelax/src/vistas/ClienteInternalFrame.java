package vista;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import persistencia.ClienteData;

public class ClienteInternalFrame extends JInternalFrame {

    private ClienteData clienteData;
    private DefaultTableModel modeloTabla;
    private JTable tablaClientes;
    private JTextField txtId, txtNombre, txtApellido, txtDni, txtTelefono, txtCorreo;
    private JButton btnAgregar, btnModificar, btnEliminar, btnBaja, btnAlta, btnLimpiar, btnBuscar;

    public ClienteInternalFrame() {
        super("Gestión de Clientes", true, true, true, true);
        clienteData = new ClienteData();
        initComponents();
        cargarTabla();
        setSize(950, 700);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = crearPanelFormulario();
        JPanel panelCentral = crearPanelTabla();
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);

        configurarEventosTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Datos del Cliente"));
        panel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(10);
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtDni = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtCorreo = new JTextField(20);

        agregarCampo(panel, gbc, "ID:", txtId, 0, 0);
        agregarCampo(panel, gbc, "Nombre:", txtNombre, 0, 1);
        agregarCampo(panel, gbc, "Apellido:", txtApellido, 0, 2);
        agregarCampo(panel, gbc, "DNI:", txtDni, 2, 0);
        agregarCampo(panel, gbc, "Teléfono:", txtTelefono, 2, 1);
        agregarCampo(panel, gbc, "Correo:", txtCorreo, 2, 2);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(new Color(240, 248, 255));

        btnAgregar = crearBoton("➕ Agregar", new Color(46, 204, 113));
        btnModificar = crearBoton("✏️ Modificar", new Color(52, 152, 219));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        btnBaja = crearBoton("⬇️ Baja", new Color(230, 126, 34));
        btnAlta = crearBoton("⬆️ Alta", new Color(46, 204, 113));
        btnBuscar = crearBoton("🔍 Buscar", new Color(155, 89, 182));
        btnLimpiar = crearBoton("🧹 Limpiar", new Color(149, 165, 166));

        btnAgregar.addActionListener(e -> agregarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnBaja.addActionListener(e -> bajaCliente());
        btnAlta.addActionListener(e -> altaCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBaja);
        panelBotones.add(btnAlta);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panel.add(panelBotones, gbc);

        return panel;
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
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(texto);
        return btn;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Estado");

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        tablaClientes.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void configurarEventosTabla() {
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaClientes.getSelectedRow();
                if (fila != -1) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtApellido.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtDni.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 4).toString());
                    txtCorreo.setText(modeloTabla.getValueAt(fila, 5).toString());
                }
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Cliente> lista = clienteData.listarTodos();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{
                c.getIdCliente(), c.getNombre(), c.getApellido(),
                c.getDni(), c.getTelefono(), c.getCorreo(),
                c.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }

    private void agregarCliente() {
        try {
            if (!validarCamposCompletos()) {
                return;
            }

            Cliente c = new Cliente();
            c.setNombre(txtNombre.getText().trim());
            c.setApellido(txtApellido.getText().trim());
            c.setDni(Integer.parseInt(txtDni.getText().trim()));
            c.setTelefono(txtTelefono.getText().trim());
            c.setCorreo(txtCorreo.getText().trim());
            c.setEstado(true);

            clienteData.agregarCliente(c);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Cliente agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ El DNI debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "⚠️ Ya existe un cliente con ese DNI.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificarCliente() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un cliente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!validarCamposCompletos()) {
                return;
            }

            Cliente c = new Cliente();
            c.setIdCliente(Integer.parseInt(txtId.getText()));
            c.setNombre(txtNombre.getText().trim());
            c.setApellido(txtApellido.getText().trim());
            c.setDni(Integer.parseInt(txtDni.getText().trim()));
            c.setTelefono(txtTelefono.getText().trim());
            c.setCorreo(txtCorreo.getText().trim());
            c.setEstado(true);

            clienteData.modificarCliente(c);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Cliente modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ El DNI debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un cliente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar permanentemente este cliente?\n" +
                "Esta acción no se puede deshacer.", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText());
                clienteData.eliminarCliente(id);
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "✅ Cliente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("foreign key constraint")) {
                JOptionPane.showMessageDialog(this, 
                    "❌ No se puede eliminar el cliente porque tiene registros asociados.\n" +
                    "Use la opción 'Baja' en su lugar.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void bajaCliente() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un cliente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Dar de baja este cliente?", 
                "Confirmar Baja", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmacion == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText());
                clienteData.bajaLogica(id);
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "✅ Baja realizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error en baja: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void altaCliente() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Seleccione un cliente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(txtId.getText());
            clienteData.altaLogica(id);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Alta realizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error en alta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        String dni = JOptionPane.showInputDialog(this, "Ingrese el DNI del cliente:");
        if (dni != null && !dni.trim().isEmpty()) {
            try {
                Cliente c = clienteData.buscarClientePorDni(Integer.parseInt(dni.trim()));
                if (c != null) {
                    txtId.setText(String.valueOf(c.getIdCliente()));
                    txtNombre.setText(c.getNombre());
                    txtApellido.setText(c.getApellido());
                    txtDni.setText(String.valueOf(c.getDni()));
                    txtTelefono.setText(c.getTelefono());
                    txtCorreo.setText(c.getCorreo());
                    JOptionPane.showMessageDialog(this, "✅ Cliente encontrado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Cliente no encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ DNI inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCamposCompletos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El nombre no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        if (txtNombre.getText().trim().length() < 2) {
            JOptionPane.showMessageDialog(this, "⚠️ El nombre debe tener al menos 2 caracteres.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        if (txtApellido.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El apellido no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtApellido.requestFocus();
            return false;
        }
        
        if (txtApellido.getText().trim().length() < 2) {
            JOptionPane.showMessageDialog(this, "⚠️ El apellido debe tener al menos 2 caracteres.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtApellido.requestFocus();
            return false;
        }
        
        if (txtDni.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El DNI no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtDni.requestFocus();
            return false;
        }
        
        try {
            int dni = Integer.parseInt(txtDni.getText().trim());
            if (dni <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ El DNI debe ser un número positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtDni.requestFocus();
                return false;
            }
            if (String.valueOf(dni).length() < 7 || String.valueOf(dni).length() > 8) {
                JOptionPane.showMessageDialog(this, "⚠️ El DNI debe tener 7 u 8 dígitos.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtDni.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ El DNI debe ser un número válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtDni.requestFocus();
            return false;
        }
        
        if (txtTelefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El teléfono no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return false;
        }
        
        if (txtCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ El correo no puede estar vacío.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus();
            return false;
        }
        
        if (!validarEmail(txtCorreo.getText().trim())) {
            JOptionPane.showMessageDialog(this, "⚠️ El formato del correo electrónico no es válido.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus();
            return false;
        }
        
        return true;
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tablaClientes.clearSelection();
        txtNombre.requestFocus();
    }
}