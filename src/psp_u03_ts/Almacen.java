/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psp_u03_ts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import util.Operacion;

/**
 *
 * @author Cristian
 */
public class Almacen extends Thread {

    private int chirimoyas;
    ServerSocket socket;

    public Almacen() {
        Thread hiloConexion = new Thread(this, "hiloConexion");
        try {
            socket = new ServerSocket(Integer.parseInt(JOptionPane.showInputDialog("Indica el nº de puerto")));
        } catch (IOException ex) {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
        }
        hiloConexion.start();
        chirimoyas = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduce stock de chirimoyas"));
        consultaUsuario();
    }

    @Override
    public void run() {
        try {
            boolean salir = false;
            Socket skTienda = socket.accept();
            JOptionPane.showMessageDialog(null, "Se ha conectado una tienda");

            while (!salir) {

                ObjectInputStream recibir
                        = new ObjectInputStream(skTienda.getInputStream());

                ObjectOutputStream enviar
                        = new ObjectOutputStream(skTienda.getOutputStream());

                Operacion op = null;

                try {
                    op = (Operacion) recibir.readObject();
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error al leer el objeto");
                }

                String tipoOp = op.getOperacion();

                switch (tipoOp) {
                    case "insertar":
                        int nuevas = op.getCantidad();
                        chirimoyas = chirimoyas + nuevas;
                        JOptionPane.showMessageDialog(null, "Se ha recibido orden de insertar " + nuevas + ".\nTotal chirimoyas en almacen: " + chirimoyas + ".");
                        break;
                    case "retirar":
                        int retirar = op.getCantidad();
                        chirimoyas = chirimoyas + retirar;
                        JOptionPane.showMessageDialog(null, "Se ha recibido orden de retirar: " + retirar + ". Total chirimoyas en almacen: " + chirimoyas + ".");
                        break;
                    case "consultar":
                        JOptionPane.showMessageDialog(null, "Se ha recibido orden de consultar el stock. Total chirimoyas en almacen: " + chirimoyas + ".");
                        enviar.writeObject(consultaTienda());
                        break;
                    case "salir":
                        salir = true;
                        JOptionPane.showMessageDialog(null, "Recibido salir. Adiós");
                        salir = true;
                        System.exit(0);
                        break;
                    default:
                        break;

                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void consultaUsuario() {
        while (true) {
            String consulta = "";
            try {
                consulta = JOptionPane.showInputDialog(null, "Para consultar stock escribir 'c + Enter' en cualquier momento");
            } catch (Exception e) {
                System.out.println(e);
            }
            if (consulta.equalsIgnoreCase(consulta)) {
                JOptionPane.showMessageDialog(null, "Total chirimoyas en almacen" + chirimoyas + ".\n");
            }
        }
    }

    private Operacion consultaTienda() {
        Operacion op = new Operacion("Consulta", chirimoyas);
        return op;
    }

    public int getChirimoyas() {
        return chirimoyas;
    }

    public void setChirimoyas(int chirimoyas) {
        this.chirimoyas = chirimoyas;
    }
}
