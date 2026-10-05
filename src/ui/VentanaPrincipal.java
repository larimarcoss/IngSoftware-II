package ui;

import servicio.GestorAsistentes;
import servicio.GestorCaja;
import servicio.GestorPagos;
import servicio.GestorPromociones;
import servicio.GestorReservas;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(GestorAsistentes gestorAsistentes, GestorReservas gestorReservas,
                            GestorPromociones gestorPromociones, GestorPagos gestorPagos,
                            GestorCaja gestorCaja) {
        super("Sistema de Gestión de Eventos - Subsistema 3");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 230);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton botonReservas = new JButton("Gestión de Reservas");
        botonReservas.addActionListener(e -> new VentanaReservas(
                gestorReservas, gestorAsistentes, gestorPagos, gestorPromociones).setVisible(true));

        JButton botonPromociones = new JButton("Gestión de Promociones");
        botonPromociones.addActionListener(e -> new VentanaPromociones(gestorPromociones).setVisible(true));

        JButton botonCaja = new JButton("Caja");
        botonCaja.addActionListener(e -> new VentanaCaja(gestorCaja).setVisible(true));

        add(botonReservas);
        add(botonPromociones);
        add(botonCaja);
    }
}
