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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import util.Operacion;

/**
 *
 * @author Cristian
 */
public class Almacen extends Thread{
    
    private int chirimoyas;

    public Almacen() {
        Thread hiloConexion = new Thread(this,"hiloConexion");
        hiloConexion.start();
        chirimoyas = Integer.parseInt(JOptionPane.showInputDialog("Inserta el stock de chirimoyas"));
    }
    
    @Override
    public void run(){
        try {
            boolean salir = false;
            ServerSocket socket = new ServerSocket(5000);
            Socket skTienda = socket.accept();
            
            while(!salir){
                System.out.println("Entra en while");
                ObjectInputStream recibir = 
                        new ObjectInputStream(skTienda.getInputStream());
                
                ObjectOutputStream enviar = 
                        new ObjectOutputStream(skTienda.getOutputStream());
                
                Operacion op = null;
                
                try {
                    op = (Operacion) recibir.readObject();
                    System.out.println(op.toString());
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error al leer el objeto");;
                }
                
                String tipoOp = op.getOperacion();

                switch (tipoOp) {
                    case "insertar":
                        System.out.println("Recibido insertar\n");
                        System.out.println("Numero de unidades" + op.getCantidad() + "\n");
                        
                        break;
                    case "retirar":
                        System.out.println("Recibido retirar");
                        break;
                    case "consultar":
                        System.out.println("Recibido consultar");
                        break;
                    case "salir":
                        System.out.println("Recibido salir");
                        salir = true;
                        break;
                    default:
                        break;

                }
   
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    
    public void gestionOperacion(){
        
    }
    
    public int getChirimoyas() {
        return chirimoyas;
    }

    public void setChirimoyas(int chirimoyas) {
        this.chirimoyas = chirimoyas;
    }
    
    
        
    
}
