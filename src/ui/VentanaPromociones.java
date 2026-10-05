package ui;

import dominio.Promocion;
import dominio.TipoDescuento;
import servicio.GestorPromociones;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VentanaPromociones extends JFrame {
    private final GestorPromociones gestorPromociones;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;

    private final JTextField campoNombre = new JTextField(15);
    private final JComboBox<TipoDescuento> comboTipoDescuento = new JComboBox<>(TipoDescuento.values());
    private final JTextField campoValorDescuento = new JTextField(8);
    private final JTextField campoFechaDesde = new JTextField(10);
    private final JTextField campoFechaHasta = new JTextField(10);
    private final JTextField campoCondicion = new JTextField(15);

    public VentanaPromociones(GestorPromociones gestorPromociones) {
        super("Gestión de Promociones");
        this.gestorPromociones = gestorPromociones;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        modeloTabla = new DefaultTableModel(new Object[]{
                "Código", "Nombre", "Tipo", "Valor", "Desde", "Hasta", "Activa"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(construirPanelFormulario(), BorderLayout.SOUTH);

        actualizarTabla();
    }

    private JPanel construirPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panel.add(campoNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Tipo de descuento:"), gbc);
        gbc.gridx = 1; panel.add(comboTipoDescuento, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Valor (% o monto fijo):"), gbc);
        gbc.gridx = 1; panel.add(campoValorDescuento, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Desde (aaaa-mm-dd):"), gbc);
        gbc.gridx = 1; panel.add(campoFechaDesde, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Hasta (aaaa-mm-dd):"), gbc);
        gbc.gridx = 1; panel.add(campoFechaHasta, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Condición de aplicación:"), gbc);
        gbc.gridx = 1; panel.add(campoCondicion, gbc);

        JButton botonCrear = new JButton("Crear (CU11)");
        botonCrear.addActionListener(e -> crear());

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(botonCrear, gbc);

        return panel;
    }

    private void crear() {
        try {
            double valor = Double.parseDouble(campoValorDescuento.getText().trim());
            LocalDate desde = LocalDate.parse(campoFechaDesde.getText().trim(), FORMATO_FECHA);
            LocalDate hasta = LocalDate.parse(campoFechaHasta.getText().trim(), FORMATO_FECHA);
            TipoDescuento tipo = (TipoDescuento) comboTipoDescuento.getSelectedItem();

            gestorPromociones.crear(campoNombre.getText().trim(), tipo, valor, desde, hasta,
                    campoCondicion.getText().trim());
            limpiarFormulario();
            actualizarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El valor debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Las fechas deben tener formato aaaa-mm-dd", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        campoNombre.setText("");
        campoValorDescuento.setText("");
        campoFechaDesde.setText("");
        campoFechaHasta.setText("");
        campoCondicion.setText("");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Promocion p : gestorPromociones.listarTodas()) {
            modeloTabla.addRow(new Object[]{
                    p.getCodigoPromocion(), p.getNombre(), p.getTipoDescuento(),
                    p.getValorDescuento(), p.getFechaDesde(), p.getFechaHasta(), p.isActiva()
            });
        }
    }
}
