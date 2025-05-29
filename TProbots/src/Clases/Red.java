package Clases;

import java.util.*;

import Cofre.*;

public class Red {
	List<Robopuerto> robopuertos = new ArrayList<>();
	List<Cofre> cofres = new ArrayList<>();
	List<Robot> robots = new ArrayList<>();
	Map<Robopuerto, Set<Robopuerto>> grafo = new HashMap<>();

	public Red(Map<Robopuerto, Set<Robopuerto>> grafo, List<Robopuerto> robopuertos, List<Cofre> cofres,
			List<Robot> robots) {
		this.grafo = grafo;
		this.robopuertos = robopuertos;
		this.cofres = cofres;
		this.robots = robots;

		// despues veo cuales necesitan la red y cuales no, creo que robot no la
		// necesita
		for (Robopuerto robopuerto : robopuertos) {
			robopuerto.setRed(this);
		}

		for (Cofre cofre : cofres) {
			cofre.setRed(this);
		}

		for (Robot robot : robots) {
			robot.setRed(this);
		}
	}

	public List<Coordenada> calcularRuta(Coordenada origen, Coordenada destino) {
		List<Coordenada> ruta = new ArrayList<Coordenada>();

		// calcula ruta entre origen y destino por dijkstra
		// primero deberia identificar el robopuerto del origen y del destino, origen ->
		// robopuertoOrigen -> robopuertos -> robopuertoDestino -> destino

		return ruta;
	}

	public int solicitarItem(Cofre cofre, Item item, int cantidad) {
		Cofre origen = null;

		int cantidadObtenida = 0;
		int aux;

		// solicitar a los cofres activos
		for (Cofre activo : cofres) {
			// validar que sea activo
			if (cantidadObtenida < cantidad) {
				aux = Math.min(activo.puedeOfrecerItem(item), cantidad);

				robots.getFirst().cargarItem(activo, item, aux);
				cantidadObtenida += aux;
			} else
				break;
		}
		
		// si los cofres activos no tienen buscar en cada intermedio, si no hay buscar
				// en cada pasivo
				// esto implicaria que el robot tiene que moverse por todos los cofres hasta
				// encontrar los items solicitados y la cantidad necesaria
		if (cantidadObtenida < cantidad) {
			// recorrer cada cofre y buscar items
		}
		

		// si no hay items suficientes entonces se registra la falla y no se sigue, se entrega lo que consiguio
		// si se obtuvieron algunos pero no la totalidad entonces se entrega lo que obtuvo
		if(cantidadObtenida == 0)
			return cantidadObtenida;

		// se tiene que aplicar alguna logica para saber a que robot hay que solicitar
		robots.getFirst().descargarItem(cofre, item, cantidadObtenida);

		return cantidadObtenida; // retornar cantidad obtenida
	}

	/*
	 * La red logística debe ser capaz de: Registrar qué cofres están cubiertos por
	 * qué robopuertos. Validar si un movimiento es posible según la cobertura.
	 * Priorizar solicitudes más urgentes o accesibles. Permitir ejecutar ciclos de
	 * simulación con cambios visibles en la red.
	 */
}
