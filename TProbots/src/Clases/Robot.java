package Clases;

import java.util.*;

import Cofre.*;

public class Robot {
	Coordenada ubicacion;
	Bateria bateria;
	Robopuerto robopuertoInicial;
	int capacidadCarga;
	private Red red;

	public Red getRed() {
		return red;
	}

	public void setRed(Red red) {
		this.red = red;
	}
	// metodos
	//// Retirar item --> antes de realizar la accion debe corrobar si tiene la
	// bateria suficiente o debe pasar por una estacion de carga en el camino
	//// Entregar item --> antes de realizar la accion debe corrobar si tiene la
	// bateria suficiente o debe pasar por una estacion de carga en el camino

	public void entregar(Cofre origen, Cofre destino, Item item, int cantidad) {
		// este metodo lo llama los cofres de solicitud o el intermedio, por medio de la
		// red

		// Orden para retirar items
		// activos con items ofrecidos
		// intermedio
		// pasivos
		// recibe los parametros del cofre que tiene que retirar y el que tiene que
		// entregar

		this.cargarItem(origen, item, cantidad);
		this.descargarItem(destino, item, cantidad);

	}

	public void almacenarExcedente() {
		// este metodo se activa al final de todo, el robot retira los items de todos
		// los profes y los guarda en los cofres de almacenamiento

	}

	public void recargar() {
		// hacer un if (su ubicacion debe ser igual a la de algun robopuerto (estacion
		// de carga))
		bateria.recargar();
	}

	public boolean moverA(Coordenada destino) {

		List<Coordenada> ruta = red.calcularRuta(ubicacion, destino);

		// si la lista esta vacia significa que no se puede mover porque no se encuentra
		// en esa red, retornamos false

		// primero se fija si se puede mover con su bateria actual al destino y despues
		// se mueve

		return true;
	}

	private int calcularDistancia(Coordenada destino) {
		// calcular distancia a destino desde la ubicacion actual

		return 0;
	}

	public boolean cargarItem(Cofre cofre, Item item, int cantidad) {
		// se fija si el cofre tiene el item y esa cantidad y que sea de un tipo que se
		// pueda descargar

		this.moverA(cofre.getUbicacion());
		cofre.agregarItem(item, cantidad);

		return true;
	}

	public boolean descargarItem(Cofre cofre, Item item, int cantidad) {

		// se fija si el cofre tiene el item y esa cantidad y que sea de un tipo que se
		// pueda descargar
		
		this.moverA(cofre.getUbicacion());
		cofre.quitarItem(item, cantidad);

		return true;
	}

	/*
	 * Los robots deben: Buscar cofres fuente y destino válidos dentro del alcance.
	 * Seleccionar el mejor proveedor según prioridad y distancia. Planificar
	 * movimientos dentro de su capacidad de carga.
	 */
}
