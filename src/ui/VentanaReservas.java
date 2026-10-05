package ui;

import dominio.Reserva;
import servicio.GestorReservas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaReservas extends JFrame {
    private final GestorReservas gestorReservas;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private final JTextField campoCodigoReservaSeleccionada = new JTextField(10);
    private final JTextField campoCodigoAsistente = new JTextField(10);
    private final JTextField campoCodigoSolicitud = new JTextField(10);
    private final JTextField campoFechaEvento = new JTextField(10);
    private final JTextField campoCantidadCupos = new JTextField(5);
    private final JTextField campoObservaciones = new JTextField(20);

    public VentanaReservas(GestorReservas gestorReservas) {
        super("Gestión de Reservas");
        this.gestorReservas = gestorReservas;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(new Object[]{
                "Código", "Asistente", "Fecha evento", "Estado", "Cupos", "Importe abonado"
        }, 0) {
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

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Código reserva (para modificar/cancelar):"), gbc);
        gbc.gridx = 1; panel.add(campoCodigoReservaSeleccionada, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Código asistente:"), gbc);
        gbc.gridx = 1; panel.add(campoCodigoAsistente, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Código solicitud (del Grupo 1):"), gbc);
        gbc.gridx = 1; panel.add(campoCodigoSolicitud, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Fecha evento (aaaa-mm-dd):"), gbc);
        gbc.gridx = 1; panel.add(campoFechaEvento, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Cantidad de cupos:"), gbc);
        gbc.gridx = 1; panel.add(campoCantidadCupos, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; panel.add(campoObservaciones, gbc);

        JButton botonInscribir = new JButton("Inscribir (CU04)");
        botonInscribir.addActionListener(e -> inscribir());

        JButton botonModificar = new JButton("Modificar (CU05)");
        botonModificar.addActionListener(e -> modificar());

        JButton botonCancelar = new JButton("Cancelar (CU06)");
        botonCancelar.addActionListener(e -> cancelar());

        JPanel panelBotones = new JPanel();
        panelBotones.add(botonInscribir);
        panelBotones.add(botonModificar);
        panelBotones.add(botonCancelar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private void inscribir() {
        try {
            LocalDate fecha = parsearFecha(campoFechaEvento.getText());
            int cupos = parsearCupos(campoCantidadCupos.getText());
            gestorReservas.inscribir(campoCodigoAsistente.getText(), campoCodigoSolicitud.getText(),
                    fecha, cupos, campoObservaciones.getText());
            limpiarFormulario();
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Reserva creada en estado Pendiente de Pago.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void modificar() {
        try {
            int cupos = parsearCupos(campoCantidadCupos.getText());
            gestorReservas.modificar(campoCodigoReservaSeleccionada.getText(), cupos, campoObservaciones.getText());
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Reserva modificada.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cancelar() {
        String motivo = JOptionPane.showInputDialog(this, "Motivo de la cancelación:");
        if (motivo == null || motivo.isBlank()) return;
        try {
            gestorReservas.cancelar(campoCodigoReservaSeleccionada.getText(), motivo);
            actualizarTabla();
            JOptionPane.showMessageDialog(this, "Reserva cancelada.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        campoCodigoReservaSeleccionada.setText((String) modeloTabla.getValueAt(fila, 0));
        campoFechaEvento.setText(modeloTabla.getValueAt(fila, 2).toString());
        campoCantidadCupos.setText(modeloTabla.getValueAt(fila, 4).toString());
    }

    private void limpiarFormulario() {
        campoCodigoAsistente.setText("");
        campoCodigoSolicitud.setText("");
        campoFechaEvento.setText("");
        campoCantidadCupos.setText("");
        campoObservaciones.setText("");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Reserva r : gestorReservas.listarTodas()) {
            modeloTabla.addRow(new Object[]{
                    r.getCodigoReserva(),
                    r.getAsistente().getNombreApellido(),
                    r.getFechaEvento(),
                    r.getEstado(),
                    r.getCantidadCupos(),
                    r.getImporteAbonado()
            });
        }
    }

    private LocalDate parsearFecha(String texto) {
        try {
            return LocalDate.parse(texto.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("La fecha debe tener formato aaaa-mm-dd");
        }
    }

    private int parsearCupos(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La cantidad de cupos debe ser un número entero");
        }
    }

    private void mostrarError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
