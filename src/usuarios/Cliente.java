package usuarios;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

	// Objeto mensaje de cierre, booleano. Se volver� verdadero cuando se escriba el texto de cierre de la comunicaci�n.
	private static boolean mensajeDeCierre = false;
	
	// Objeto Host. Se inicializa vac�o y se rellenar� con la informaci�n obtenida del scanner
	private static String Host = "";
	
	// Objeto String, con el valor del puerto
	private static int Puerto = 6000;
	
	/**
	 * M�todo main
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		intentarConexion();
	}
	
	
	
	/**
	 * M�todo que solicita una direcci�n IP al usuario. Si la direcci�n IP
	 * coincide con el otro usuario, realizar� la conexi�n.
	 * @throws IOException
	 */
	public static void intentarConexion() throws IOException {
		
		// Cabecera de bienvenida
		System.out.println("INTRODUZCA LA DIRECCI�N IP PARA INTENTAR LA CONEXI�N O ESCRIBA CANCELAR PARA SALIR:\n");
				
		// Creamos un scanner y leemos la entrada del cliente
		Scanner scanner = new Scanner(System.in);
		Host = scanner.nextLine().toLowerCase();
		
		// Comprobamos la conexi�n seg�n el host indicado
		conectar(Host);
		
		// Cerramos el scanner
		scanner.close();
	}
	
	
	
	/**
	 * Discriminamos la acci�n del programa seg�n la direcci�n del host que nos haya
	 * escrito el cliente. De momento, s�lo permitir� conexi�n local.
	 * @param Host
	 * @throws IOException
	 */
	public static void conectar (String Host) throws IOException {
       
		switch (Host) {

			// En caso de escribir localhost, accede al programa walkie-talkie
	        case "localhost":
	        	System.out.println("\nPROGRAMA CLIENTE INICIANDOSE....\nPROGRAMA CLIENTE INICIADO.\nESCRIBA MENSAJE PARA EL SERVIDOR:");
	        	comunicar();
	            break;
	            
	        // En caso de escribir cancelar, cierra el programa
	        case "cancelar":
	        	System.out.println("\nGRACIAS, VUELVA PRONTO!");
	        	break;
	        
	        // Si escribe cualquier otra cosa, lanza error y vuelve a pedir escritura
	        default:
	        	System.out.println("\nSELECCI�N INCORRECTA.\n");
	    		intentarConexion();
	    		break;
        }
	}
	
	  
	
	/**
	 * M�todo para intercambiar mensajes con el servidor
	 * @throws IOException 
	 */
	public static void comunicar() throws IOException {
		
		//Objeto scanner, que leer� el texto escrito en la consola
		Scanner scanner = new Scanner(System.in);

		//Inicio del programa cliente y mensaje de confirmaci�n
		Socket Cliente = new Socket(Host, Puerto);

		// Flujo de entrada y salida al servidor
		DataOutputStream textoSalida = new DataOutputStream(Cliente.getOutputStream());
		DataInputStream textoEntrada = new DataInputStream(Cliente.getInputStream());
		
		//Comprobaci�n que no se ha escrito el mensaje de cierre
		while (!mensajeDeCierre) {

			//Inicio del mensaje vac�o
			String mensaje = "";
			
			//M�todo para enviar un mensaje y prepararse para recibir el siguiente
			//siempre que no se escriba el mensaje de cambio de usuario
			while (!(mensaje = scanner.nextLine()).contains("CAMBIO")) {

				//Env�o al servidor del mensaje recibido por consola
				textoSalida.writeUTF(mensaje);
				System.out.println("MENSAJE ENVIADO. ESCRIBA SIGUIENTE MENSAJE:");

				//Condici�n del mensaje de cierre de comunicaci�n
				if (mensaje.contains("FRANCIS")) {
					//Cambio de valor del booleano, mensaje de confirmaci�n y cierre del programa
					mensajeDeCierre = true;
					System.out.println("Vaya! Francis ha hackeado la app, asi que vamos a tener que... no, no es posible... NOOOOOOO AAAAAAAAGGG");
					System.out.println("PROGRAMA CERRADO.");
					break;
				}
			}
						
			//Si se reconoce el mensaje de cierre de comunicaci�n, cerramos el programa
			if(mensajeDeCierre) break;
			
			//Si se reconoce el mensaje de cambio de usuario, se env�a el mensaje y se cambia el usuario
			if(mensaje.contains("CAMBIO")) {
				textoSalida.writeUTF(mensaje);
				System.out.println("ESPERANDO MENSAJE DEL SERVIDOR.");
			}
			
			//Si no se reconoce el mensaje de cambio de usuario
			while (!(mensaje = textoEntrada.readUTF()).contains("CAMBIO")){
				
				//Se env�a el mensaje
				System.out.println(mensaje);
			
				//Si se reconoce el mensaje de cierre de conversaci�n, se cierra el programa
				if (mensaje.contains("FRANCIS")) {
					mensajeDeCierre = true;
					System.out.println("Vaya! Francis ha hackeado la app, asi que vamos a tener que... no, no es posible... NOOOOOOO AAAAAAAAGGG");
					System.out.println("PROGRAMA CERRADO.");
					break;
				}
			}	
		}
		
		//Cierre de todos los objetos de conexi�n y esc�ner
		textoEntrada.close();
		textoSalida.close();
		Cliente.close();
		scanner.close();

	}

}