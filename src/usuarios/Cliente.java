package usuarios;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

	// Objeto mensaje de cierre, booleano. Se volverá verdadero cuando se escriba el texto de cierre de la comunicación.
	private static boolean mensajeDeCierre = false;
	
	// Objeto Host. Se inicializa vacío y se rellenará con la información obtenida del scanner
	private static String Host = "";
	
	// Objeto String, con el valor del puerto
	private static int Puerto = 6000;
	
	/**
	 * Método main
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		intentarConexion();
	}
	
	
	
	/**
	 * Método que solicita una dirección IP al usuario. Si la dirección IP
	 * coincide con el otro usuario, realizará la conexión.
	 * @throws IOException
	 */
	public static void intentarConexion() throws IOException {
		
		// Cabecera de bienvenida
		System.out.println("INTRODUZCA LA DIRECCIÓN IP PARA INTENTAR LA CONEXIÓN O ESCRIBA CANCELAR PARA SALIR:\n");
				
		// Creamos un scanner y leemos la entrada del cliente
		Scanner scanner = new Scanner(System.in);
		Host = scanner.nextLine().toLowerCase();
		
		// Comprobamos la conexión según el host indicado
		conectar(Host);
		
		// Cerramos el scanner
		scanner.close();
	}
	
	
	
	/**
	 * Discriminamos la acción del programa según la dirección del host que nos haya
	 * escrito el cliente. De momento, sólo permitirá conexión local.
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
	        	System.out.println("\nSELECCIÓN INCORRECTA.\n");
	    		intentarConexion();
	    		break;
        }
	}
	
	  
	
	/**
	 * Método para intercambiar mensajes con el servidor
	 * @throws IOException 
	 */
	public static void comunicar() throws IOException {
		
		//Objeto scanner, que leerá el texto escrito en la consola
		Scanner scanner = new Scanner(System.in);

		//Inicio del programa cliente y mensaje de confirmación
		Socket Cliente = new Socket(Host, Puerto);

		// Flujo de entrada y salida al servidor
		DataOutputStream textoSalida = new DataOutputStream(Cliente.getOutputStream());
		DataInputStream textoEntrada = new DataInputStream(Cliente.getInputStream());
		
		//Comprobación que no se ha escrito el mensaje de cierre
		while (!mensajeDeCierre) {

			//Inicio del mensaje vacío
			String mensaje = "";
			
			//Método para enviar un mensaje y prepararse para recibir el siguiente
			//siempre que no se escriba el mensaje de cambio de usuario
			while (!(mensaje = scanner.nextLine()).contains("CAMBIO")) {

				//Envío al servidor del mensaje recibido por consola
				textoSalida.writeUTF(mensaje);
				System.out.println("MENSAJE ENVIADO. ESCRIBA SIGUIENTE MENSAJE:");

				//Condición del mensaje de cierre de comunicación
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
			
			//Si se reconoce el mensaje de cambio de usuario, se envía el mensaje y se cambia el usuario
			if(mensaje.contains("CAMBIO")) {
				textoSalida.writeUTF(mensaje);
				System.out.println("ESPERANDO MENSAJE DEL SERVIDOR.");
			}
			
			//Si no se reconoce el mensaje de cambio de usuario
			while (!(mensaje = textoEntrada.readUTF()).contains("CAMBIO")){
				
				//Se envía el mensaje
				System.out.println(mensaje);
			
				//Si se reconoce el mensaje de cierre de conversación, se cierra el programa
				if (mensaje.contains("FRANCIS")) {
					mensajeDeCierre = true;
					System.out.println("Vaya! Francis ha hackeado la app, asi que vamos a tener que... no, no es posible... NOOOOOOO AAAAAAAAGGG");
					System.out.println("PROGRAMA CERRADO.");
					break;
				}
			}	
		}
		
		//Cierre de todos los objetos de conexión y escáner
		textoEntrada.close();
		textoSalida.close();
		Cliente.close();
		scanner.close();

	}

}