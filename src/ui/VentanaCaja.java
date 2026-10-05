package ui;

import dominio.MovimientoDeCaja;
import servicio.GestorCaja;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaCaja extends JFrame {
    private final GestorCaja gestorCaja;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JLabel etiquetaSaldo = new JLabel();

    public VentanaCaja(GestorCaja gestorCaja) {
        super("Caja - Movimientos y Saldo");
        this.gestorCaja = gestorCaja;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        etiquetaSaldo.setFont(etiquetaSaldo.getFont().deriveFont(Font.BOLD, 16f));
        JPanel panelSaldo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSaldo.add(etiquetaSaldo);
        add(panelSaldo, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Tipo", "Fecha", "Monto", "Descripción"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(construirPanelBotones(), BorderLayout.SOUTH);

        actualizar();
    }

    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton botonIngreso = new JButton("+ Ingreso (CU12)");
        botonIngreso.addActionListener(e -> registrarIngreso());

        JButton botonEgreso = new JButton("+ Egreso (CU12)");
        botonEgreso.addActionListener(e -> registrarEgreso());

        panel.add(botonIngreso);
        panel.add(botonEgreso);
        return panel;
    }

    private void registrarIngreso() {
        JTextField campoMonto = new JTextField();
        JTextField campoDescripcion = new JTextField();
        JTextField campoMedioPago = new JTextField();
        Object[] mensaje = {
                "Monto:", campoMonto,
                "Descripción:", campoDescripcion,
                "Medio de pago:", campoMedioPago
        };
        int resultado = JOptionPane.showConfirmDialog(this, mensaje, "Registrar ingreso", JOptionPane.OK_CANCEL_OPTION);
        if (resultado != JOptionPane.OK_OPTION) return;
        try {
            double monto = Double.parseDouble(campoMonto.getText().trim());
            gestorCaja.registrarIngreso(monto, campoDescripcion.getText().trim(), campoMedioPago.getText().trim());
            actualizar();
        } catch (NumberFormatException ex) {
            mostrarError(new IllegalArgumentException("El monto debe ser un número válido"));
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void registrarEgreso() {
        JTextField campoMonto = new JTextField();
        JTextField campoDescripcion = new JTextField();
        JTextField campoMotivo = new JTextField();
        Object[] mensaje = {
                "Monto:", campoMonto,
                "Descripción:", campoDescripcion,
                "Motivo:", campoMotivo
        };
        int resultado = JOptionPane.showConfirmDialog(this, mensaje, "Registrar egreso", JOptionPane.OK_CANCEL_OPTION);
        if (resultado != JOptionPane.OK_OPTION) return;
        try {
            double monto = Double.parseDouble(campoMonto.getText().trim());
            gestorCaja.registrarEgreso(monto, campoDescripcion.getText().trim(), campoMotivo.getText().trim());
            actualizar();
        } catch (NumberFormatException ex) {
            mostrarError(new IllegalArgumentException("El monto debe ser un número válido"));
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void actualizar() {
        etiquetaSaldo.setText("Saldo actual: $" + gestorCaja.obtenerSaldoActual());
        modeloTabla.setRowCount(0);
        for (MovimientoDeCaja m : gestorCaja.listarMovimientos()) {
            modeloTabla.addRow(new Object[]{
                    m.getCodigoMovimiento(), m.getTipo(), m.getFecha(), m.getMonto(), m.getDescripcion()
            });
        }
    }

    private void mostrarError(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
