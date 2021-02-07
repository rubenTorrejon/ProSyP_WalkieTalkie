package usuarios;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {
	
	//Objeto mensaje de cierre, booleano. Se volverá verdadero
	//cuando se escriba el texto de cierre de la comunicación.
	private static boolean mensajeDeCierre = false;
	
    public static void main(String[] arg) throws IOException {
        
    	//Datos de conexión
        int numeroPuerto = 6000;
        ServerSocket servidor = new ServerSocket(numeroPuerto);
        
        //Objeto cliente
        Socket clienteConectado = null;
        System.out.println("SERVIDOR CONECTADO. ESPERANDO MENSAJE.");
        clienteConectado = servidor.accept();
        
		//Objeto scanner, que leerá el texto escrito en la consola      
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
       
        //Flujo de entrada del cliente  
        InputStream entrada = null;
        entrada = clienteConectado.getInputStream();
        DataInputStream textoEntrada = new DataInputStream(entrada);
              
        //Flujo de salita al cliente
        OutputStream salida = null;
        salida = clienteConectado.getOutputStream();
        DataOutputStream textoSalida = new DataOutputStream(salida);
       
        //Comprobación que no se ha escrito el mensaje de cierre
		while (!mensajeDeCierre) {

			//Inicio del mensaje vacío
			String mensaje = "";
			
			//Si no se reconoce el mensaje de cambio de usuario
			while (!(mensaje = textoEntrada.readUTF()).contains("CAMBIO")){
				
				//Se envía el mensaje
				System.out.println(mensaje);
			
				//Si se reconoce el mensaje de cierre de conversación, se cierra el programa
				if (mensaje.contains("FRANCIS")) {
					//Cambio de valor del booleano, mensaje de confirmación y cierre del programa
					mensajeDeCierre = true;
					System.out.println("Vaya! Francis ha hackeado la app, asi que vamos a tener que... no, no es posible... NOOOOOOO AAAAAAAAGGG");
					System.out.println("PROGRAMA CERRADO.");
					break;
				}
			}
			
			//Si se reconoce el mensaje de cierre de comunicación, cerramos el programa
			if(mensajeDeCierre) break;
			
			//Método para enviar un mensaje y prepararse para recibir el siguiente
			//siempre que no se escriba el mensaje de cambio de usuario
			while (!(mensaje = scanner.nextLine()).contains("CAMBIO")) {

				//Envío al cliente del mensaje recibido por consola
				textoSalida.writeUTF(mensaje);
				System.out.println("MENSAJE ENVIADO. SIGUIENTE MENSAJE:");

				//Condición del mensaje de cierre de comunicación
				if (mensaje.contains("FRANCIS")) {
					//Cambio de valor del booleano, mensaje de confirmación y cierre del programa
					mensajeDeCierre = true;
					System.out.println("Vaya! Francis ha hackeado la app, asi que vamos a tener que... no, no es posible... NOOOOOOO AAAAAAAAGGG");
					System.out.println("PROGRAMA CERRADO.");
					break;
				}
			}		
			
			//Si se reconoce el mensaje de cambio de usuario, se envía el mensaje y se cambia el usuario
			if(mensaje.contains("CAMBIO")) {
				textoSalida.writeUTF(mensaje);
				System.out.println("ESPERANDO MENSAJE DEL CLIENTE.");
			}
		}
        
		//Cierre de todos los objetos de conexión y escáner
        entrada.close();
        textoEntrada.close();
        salida.close();
        textoSalida.close();
        clienteConectado.close();
        servidor.close();
        scanner.close();
       
    }

}