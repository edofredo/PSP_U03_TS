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
public class Almacen extends Thread{
    
    private int chirimoyas;
    ServerSocket socket;
    
    public Almacen() {
        Thread hiloConexion = new Thread(this,"hiloConexion");
        try {
            socket = new ServerSocket(5000);
        } catch (IOException ex) {
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
        }
        hiloConexion.start();
        chirimoyas = 100;
        consultarStock();
    }
    
    @Override
    public void run(){
        try {
            boolean salir = false;
            Socket skTienda = socket.accept();
            System.out.println("Tienda conectada\n");
            
            while(!salir){
               
                ObjectInputStream recibir = 
                        new ObjectInputStream(skTienda.getInputStream());
                
                ObjectOutputStream enviar = 
                        new ObjectOutputStream(skTienda.getOutputStream());
                
                Operacion op = null;
                
                try {
                    op = (Operacion) recibir.readObject();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error al leer el objeto");
                }
                
                String tipoOp = op.getOperacion();

                switch (tipoOp) {
                    case "insertar":
                        int nuevas = op.getCantidad();
                        System.out.println("Se ha recibido orden de insertar "+ nuevas+".\n");
                        chirimoyas = chirimoyas + nuevas;
                        System.out.println("Total chirimoyas en almacen"+ chirimoyas +".\n");
                        break;
                    case "retirar":
                        int retirar = op.getCantidad();
                        System.out.println("Se ha recibido orden de retirar "+ retirar+".\n");
                        chirimoyas = chirimoyas + retirar;
                        System.out.println("Total chirimoyas en almacen"+ chirimoyas +".\n");
                        break;
                    case "consultar":
                        System.out.println("Se ha recibido orden de consultar el stock.\n");
                        System.out.println("Total chirimoyas en almacen"+ chirimoyas +".\n");
                        break;
                    case "salir":
                        salir = true;
                        System.out.println("Recibido salir. Adiós");
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
    
    private void consultarStock(){
       while(true){
           System.out.println("Para consultar stock escribir 'c + Enter' en cualquier momento");
           Scanner sc = new Scanner(System.in);
           if(sc.nextLine().equalsIgnoreCase("c")){
              System.out.println("Total chirimoyas en almacen"+ chirimoyas +".\n"); 
           }
       }
    }
    
    public int getChirimoyas() {
        return chirimoyas;
    }

    public void setChirimoyas(int chirimoyas) {
        this.chirimoyas = chirimoyas;
    }
    
    
        
    
}
