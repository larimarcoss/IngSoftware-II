package ui;

import servicio.GestorAsistentes;
import servicio.GestorPagos;
import servicio.GestorPromociones;
import servicio.GestorReservas;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(GestorAsistentes gestorAsistentes, GestorReservas gestorReservas,
                            GestorPromociones gestorPromociones, GestorPagos gestorPagos) {
        super("Sistema de Gestión de Eventos - Subsistema 3");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton botonAsistentes = new JButton("Gestión de Asistentes");
        botonAsistentes.addActionListener(e -> new VentanaAsistentes(gestorAsistentes).setVisible(true));

        JButton botonReservas = new JButton("Gestión de Reservas");
        botonReservas.addActionListener(e -> new VentanaReservas(gestorReservas).setVisible(true));

        JButton botonPromociones = new JButton("Gestión de Promociones");
        botonPromociones.addActionListener(e -> new VentanaPromociones(gestorPromociones).setVisible(true));

        JButton botonPagos = new JButton("Registrar Pago");
        botonPagos.addActionListener(e -> new VentanaPagos(gestorPagos).setVisible(true));

        add(botonAsistentes);
        add(botonReservas);
        add(botonPromociones);
        add(botonPagos);
    }
}
