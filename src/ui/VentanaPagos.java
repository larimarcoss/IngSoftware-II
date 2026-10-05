package ui;

import dominio.Factura;
import servicio.GestorPagos;

import javax.swing.*;
import java.awt.*;

public class VentanaPagos extends JFrame {
    private final GestorPagos gestorPagos;

    private final JTextField campoCodigoReserva = new JTextField(10);
    private final JTextField campoImporteTotal = new JTextField(10);
    private final JTextField campoMedioDePago = new JTextField(15);
    private final JTextField campoCodigoPromocion = new JTextField(10);

    public VentanaPagos(GestorPagos gestorPagos) {
        super("Registrar Pago de Reserva");
        this.gestorPagos = gestorPagos;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Código de reserva:"), gbc);
        gbc.gridx = 1; add(campoCodigoReserva, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Importe total:"), gbc);
        gbc.gridx = 1; add(campoImporteTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Medio de pago:"), gbc);
        gbc.gridx = 1; add(campoMedioDePago, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Código promoción (opcional):"), gbc);
        gbc.gridx = 1; add(campoCodigoPromocion, gbc);

        JButton botonRegistrar = new JButton("Registrar Pago (CU09 + CU10)");
        botonRegistrar.addActionListener(e -> registrarPago());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(botonRegistrar, gbc);
    }

    private void registrarPago() {
        try {
            double importe = Double.parseDouble(campoImporteTotal.getText().trim());
            String promocion = campoCodigoPromocion.getText().trim();
            Factura factura = gestorPagos.registrarPago(
                    campoCodigoReserva.getText().trim(),
                    importe,
                    campoMedioDePago.getText().trim(),
                    promocion.isEmpty() ? null : promocion
            );
            JOptionPane.showMessageDialog(this,
                    "Pago registrado.\nFactura: " + factura.getCodigoFactura()
                            + "\nImporte total: $" + factura.getImporteTotal()
                            + "\nImporte final: $" + factura.getImporteFinal());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El importe debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
