package main;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import sistema.Colonia;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		PrintStream archivoSalida = new PrintStream("salida.txt");
		System.setOut(archivoSalida);
		
		Colonia colonia = new Colonia();
		try {
			colonia.cargarArchivo();
		} catch (Exception e) {
			System.exit(1);
		}

		// Crear redes
		colonia.crearRedes();
		// Iniciar simulacion
		colonia.iniciarSimulacion();

	}

}
