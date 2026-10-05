package ui;

import dominio.Asistente;
import servicio.GestorAsistentes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaAsistentes extends JFrame {
    private final GestorAsistentes gestorAsistentes;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private final JTextField campoCodigoSeleccionado = new JTextField(10);
    private final JTextField campoNombre = new JTextField(20);
    private final JTextField campoEmail = new JTextField(20);
    private final JTextField campoTelefono = new JTextField(20);

    public VentanaAsistentes(GestorAsistentes gestorAsistentes) {
        super("Gestión de Asistentes");
        this.gestorAsistentes = gestorAsistentes;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre y apellido", "Email", "Teléfono", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccionEnFormulario());
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(construirPanelFormulario(), BorderLayout.SOUTH);

        actualizarTabla();
    }

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Código (para modificar/dar de baja):"), gbc);
        gbc.gridx = 1; panel.add(campoCodigoSeleccionado, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nombre y apellido:"), gbc);
        gbc.gridx = 1; panel.add(campoNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; panel.add(campoTelefono, gbc);

        JButton botonRegistrar = new JButton("Registrar (CU01)");
        botonRegistrar.addActionListener(e -> registrar());

        JButton botonModificar = new JButton("Modificar (CU02)");
        botonModificar.addActionListener(e -> modificar());

        JButton botonBaja = new JButton("Dar de baja (CU03)");
        botonBaja.addActionListener(e -> darDeBaja());

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonRegistrar);
        panelBotones.add(botonModificar);
        panelBotones.add(botonBaja);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private void registrar() {
        try {
            gestorAsistentes.registrar(campoNombre.getText(), campoEmail.getText(), campoTelefono.getText());
            limpiarFormulario();
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Asistente registrado correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void modificar() {
        try {
            gestorAsistentes.modificarDatos(campoCodigoSeleccionado.getText(),
                    campoNombre.getText(), campoEmail.getText(), campoTelefono.getText());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void darDeBaja() {
        String motivo = JOptionPane.showInputDialog(this, "Motivo de la baja:");
        if (motivo == null || motivo.isBlank()) return;
        try {
            gestorAsistentes.darDeBaja(campoCodigoSeleccionado.getText(), motivo);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Asistente dado de baja.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        campoCodigoSeleccionado.setText((String) modeloTabla.getValueAt(fila, 0));
        campoNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        campoEmail.setText((String) modeloTabla.getValueAt(fila, 2));
        campoTelefono.setText((String) modeloTabla.getValueAt(fila, 3));
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoEmail.setText("");
        campoTelefono.setText("");
        campoCodigoSeleccionado.setText("");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Asistente a : gestorAsistentes.listarTodos()) {
            modeloTabla.addRow(new Object[]{
                    a.getCodigoAsistente(), a.getNombreApellido(), a.getEmail(), a.getTelefono(), a.getEstado()
            });
        }
    }

    private void mostrarError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
