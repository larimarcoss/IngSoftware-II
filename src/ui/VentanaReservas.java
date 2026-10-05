package ui;

import dominio.Asistente;
import dominio.Factura;
import dominio.Reserva;
import servicio.GestorAsistentes;
import servicio.GestorPagos;
import servicio.GestorPromociones;
import servicio.GestorReservas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaReservas extends JFrame {
    private final GestorReservas gestorReservas;
    private final GestorAsistentes gestorAsistentes;
    private final GestorPagos gestorPagos;
    private final GestorPromociones gestorPromociones;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    private final DefaultTableModel modeloReservas;
    private final JTable tablaReservas;

    private final DefaultTableModel modeloAsistentes;
    private final JTable tablaAsistentes;

    private final JTextField campoObservaciones = new JTextField(25);
    private final JTextField campoCodigoAsistenteAAgregar = new JTextField(10);

    private String codigoReservaSeleccionada;

    public VentanaReservas(GestorReservas gestorReservas, GestorAsistentes gestorAsistentes,
                           GestorPagos gestorPagos, GestorPromociones gestorPromociones) {
        super("Gestión de Reservas");
        this.gestorReservas = gestorReservas;
        this.gestorAsistentes = gestorAsistentes;
        this.gestorPagos = gestorPagos;
        this.gestorPromociones = gestorPromociones;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloReservas = new DefaultTableModel(new Object[]{
                "Código", "Cód. solicitud", "Fecha evento", "Estado", "Asistentes", "Importe abonado"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaReservas = new JTable(modeloReservas);
        tablaReservas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarReservaSeleccionada();
        });

        modeloAsistentes = new DefaultTableModel(new Object[]{"Código", "Nombre y apellido", "Email"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaAsistentes = new JTable(modeloAsistentes);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tablaReservas), construirPanelDetalle());
        splitPane.setResizeWeight(0.45);
        add(splitPane, BorderLayout.CENTER);
        add(construirPanelSuperior(), BorderLayout.NORTH);

        actualizarTablaReservas();
    }

    private JPanel construirPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botonNuevaReserva = new JButton("+ Nueva reserva");
        botonNuevaReserva.addActionListener(e -> crearReserva());

        JButton botonGestionarAsistentes = new JButton("Gestionar asistentes (CU01, CU02, CU03)");
        botonGestionarAsistentes.addActionListener(e -> new VentanaAsistentes(gestorAsistentes).setVisible(true));

        panel.add(botonNuevaReserva);
        panel.add(botonGestionarAsistentes);
        return panel;
    }

    private JPanel construirPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Detalle de la reserva seleccionada"));

        panel.add(new JScrollPane(tablaAsistentes), BorderLayout.CENTER);

        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAgregar.add(new JLabel("Código asistente:"));
        panelAgregar.add(campoCodigoAsistenteAAgregar);
        JButton botonAgregar = new JButton("Agregar asistente (CU04)");
        botonAgregar.addActionListener(e -> agregarAsistente());
        JButton botonQuitar = new JButton("Quitar asistente seleccionado");
        botonQuitar.addActionListener(e -> quitarAsistente());
        panelAgregar.add(botonAgregar);
        panelAgregar.add(botonQuitar);

        JPanel panelObservaciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelObservaciones.add(new JLabel("Observaciones:"));
        panelObservaciones.add(campoObservaciones);
        JButton botonModificar = new JButton("Modificar (CU05)");
        botonModificar.addActionListener(e -> modificarObservaciones());
        panelObservaciones.add(botonModificar);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botonPago = new JButton("Registrar pago (CU09 + CU10)");
        botonPago.addActionListener(e -> abrirDialogoPago());
        JButton botonCancelar = new JButton("Cancelar reserva (CU06)");
        botonCancelar.addActionListener(e -> cancelarReserva());
        panelAcciones.add(botonPago);
        panelAcciones.add(botonCancelar);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.add(panelAgregar);
        panelInferior.add(panelObservaciones);
        panelInferior.add(panelAcciones);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    private void crearReserva() {
        JTextField campoSolicitud = new JTextField();
        JTextField campoFecha = new JTextField();
        JTextField campoObs = new JTextField();
        Object[] mensaje = {
                "Código de solicitud (del Grupo 1):", campoSolicitud,
                "Fecha del evento (aaaa-mm-dd):", campoFecha,
                "Observaciones:", campoObs
        };
        int resultado = JOptionPane.showConfirmDialog(this, mensaje, "Nueva reserva", JOptionPane.OK_CANCEL_OPTION);
        if (resultado != JOptionPane.OK_OPTION) return;
        try {
            LocalDate fecha = LocalDate.parse(campoFecha.getText().trim(), FORMATO_FECHA);
            gestorReservas.crear(campoSolicitud.getText().trim(), fecha, campoObs.getText().trim());
            actualizarTablaReservas();
        } catch (DateTimeParseException ex) {
            mostrarError(new IllegalArgumentException("La fecha debe tener formato aaaa-mm-dd"));
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void agregarAsistente() {
        if (codigoReservaSeleccionada == null) { avisarSinSeleccion(); return; }
        try {
            gestorReservas.agregarAsistente(codigoReservaSeleccionada, campoCodigoAsistenteAAgregar.getText().trim());
            campoCodigoAsistenteAAgregar.setText("");
            cargarReservaSeleccionada();
            actualizarTablaReservas();
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void quitarAsistente() {
        if (codigoReservaSeleccionada == null) { avisarSinSeleccion(); return; }
        int fila = tablaAsistentes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná un asistente de la lista para quitar.");
            return;
        }
        String codigoAsistente = (String) modeloAsistentes.getValueAt(fila, 0);
        try {
            gestorReservas.quitarAsistente(codigoReservaSeleccionada, codigoAsistente);
            cargarReservaSeleccionada();
            actualizarTablaReservas();
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void modificarObservaciones() {
        if (codigoReservaSeleccionada == null) { avisarSinSeleccion(); return; }
        try {
            gestorReservas.modificarObservaciones(codigoReservaSeleccionada, campoObservaciones.getText());
            actualizarTablaReservas();
            JOptionPane.showMessageDialog(this, "Observaciones actualizadas.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cancelarReserva() {
        if (codigoReservaSeleccionada == null) { avisarSinSeleccion(); return; }
        String motivo = JOptionPane.showInputDialog(this, "Motivo de la cancelación:");
        if (motivo == null || motivo.isBlank()) return;
        try {
            gestorReservas.cancelar(codigoReservaSeleccionada, motivo);
            actualizarTablaReservas();
            JOptionPane.showMessageDialog(this, "Reserva cancelada.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void abrirDialogoPago() {
        if (codigoReservaSeleccionada == null) { avisarSinSeleccion(); return; }

        JTextField campoImporte = new JTextField();
        JTextField campoMedioPago = new JTextField();
        JComboBox<String> comboPromo = new JComboBox<>();
        comboPromo.addItem("(sin promoción)");
        gestorPromociones.listarTodas().forEach(p -> comboPromo.addItem(p.getCodigoPromocion() + " - " + p.getNombre()));

        Object[] mensaje = {
                "Importe total:", campoImporte,
                "Medio de pago:", campoMedioPago,
                "Promoción:", comboPromo
        };
        int resultado = JOptionPane.showConfirmDialog(this, mensaje, "Registrar pago", JOptionPane.OK_CANCEL_OPTION);
        if (resultado != JOptionPane.OK_OPTION) return;

        try {
            double importe = Double.parseDouble(campoImporte.getText().trim());
            String seleccionPromo = (String) comboPromo.getSelectedItem();
            String codigoPromocion = (seleccionPromo == null || seleccionPromo.startsWith("("))
                    ? null : seleccionPromo.split(" - ")[0];

            Factura factura = gestorPagos.registrarPago(codigoReservaSeleccionada, importe,
                    campoMedioPago.getText().trim(), codigoPromocion);

            actualizarTablaReservas();
            JOptionPane.showMessageDialog(this,
                    "Pago registrado.\nFactura: " + factura.getCodigoFactura()
                            + "\nImporte total: $" + factura.getImporteTotal()
                            + "\nImporte final: $" + factura.getImporteFinal());
        } catch (NumberFormatException ex) {
            mostrarError(new IllegalArgumentException("El importe debe ser un número válido"));
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarReservaSeleccionada() {
        int fila = tablaReservas.getSelectedRow();
        if (fila < 0) {
            codigoReservaSeleccionada = null;
            modeloAsistentes.setRowCount(0);
            campoObservaciones.setText("");
            return;
        }
        codigoReservaSeleccionada = (String) modeloReservas.getValueAt(fila, 0);
        Reserva reserva = gestorReservas.obtener(codigoReservaSeleccionada);

        campoObservaciones.setText(reserva.getObservaciones());

        modeloAsistentes.setRowCount(0);
        for (Asistente a : reserva.getAsistentes()) {
            modeloAsistentes.addRow(new Object[]{a.getCodigoAsistente(), a.getNombreApellido(), a.getEmail()});
        }
    }

    private void actualizarTablaReservas() {
        modeloReservas.setRowCount(0);
        for (Reserva r : gestorReservas.listarTodas()) {
            modeloReservas.addRow(new Object[]{
                    r.getCodigoReserva(), r.getCodigoSolicitud(), r.getFechaEvento(),
                    r.getEstado(), r.getCantidadCupos(), r.getImporteAbonado()
            });
        }
        if (codigoReservaSeleccionada != null) {
            cargarReservaSeleccionada();
        }
    }

    private void avisarSinSeleccion() {
        JOptionPane.showMessageDialog(this, "Primero seleccioná una reserva de la lista.");
    }

    private void mostrarError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
