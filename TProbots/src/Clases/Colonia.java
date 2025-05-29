package Clases;

import java.util.*;

import Cofre.Cofre;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Colonia {

	List<Robopuerto> robopuertos = new ArrayList<>();
	List<Cofre> cofres = new ArrayList<>();
	List<Robot> robots = new ArrayList<>(); // esto viene como entrada

	List<Red> redes = new ArrayList<>(); // la arma despues

	// una colonia tiene varias redes
	// crea las redes, identificando que robopuertos, cofres y robots pertenecen a
	// esa red
	// le asigna a cada robopuertos, cofres y robots la red a la que pertenecen
	// tiene todos los datos de la carga de archivo

	public void armarRedes() {

		Map<Robopuerto, Set<Robopuerto>> grafo = new HashMap<>();

		// armar matriz de adyacencias
		for (Robopuerto robopuerto1 : robopuertos) {
			grafo.putIfAbsent(robopuerto1, new HashSet<>());
			for (Robopuerto robopuerto2 : robopuertos) {
				if (robopuerto1 != robopuerto2 && robopuerto1.seSuperponeCon(robopuerto2)) {
					grafo.get(robopuerto1).add(robopuerto2);
					grafo.putIfAbsent(robopuerto2, new HashSet<>());
					grafo.get(robopuerto2).add(robopuerto1);
				}
			}
		}

		redes.add(new Red(grafo, robopuertos, cofres, robots));

		// esto funciona si es una sola red pero pueden ser varias
	}

	public void iniciarSimulacion() {
		// va a llamar al metodo de operar de cada cofre, aca se haria una simulacion

		boolean solicitar = true;

		while (solicitar) {
			solicitar = false;

			for (Cofre cofre : cofres) {
				if (cofre.puedeSolicitar()) {
					cofre.solicitarItem();
					solicitar = true;
				}

			}
		}
		
		System.out.println("Los cofres ya no tienen solicitudes");

		boolean ofrecer = true;

		while (ofrecer) {
			ofrecer = false;

			for (Cofre cofre : cofres) {
				if (cofre.puedeOfrecer()) {
					// hacer metodo para llevar a cofre de almacenamiento

					solicitar = true;
				}
			}
		}
		
		System.out.println("Los items excedentes se encuentran en los almacenamientos");
	}

	// a partir de la colonia se va a hacer toda la simulacion
	// las llamadas del main siempre van a ser realizadas por los cofres

	public static void main(String[] args) throws StreamReadException, DatabindException, IOException {
		// cargar archivo
		ObjectMapper mapper = new ObjectMapper();
		Colonia colonia = mapper.readValue(new File("config.json"), Colonia.class);

		colonia.armarRedes();
		colonia.iniciarSimulacion();
	}

}



// archivo JSON formato
/*
{
  "robopuertos": [
    {
      "id": "___",
      "ubicacion": { "x": ___, "y": ___ },
      "alcance": ___
    }
  ],
  "cofres": [
    {
      "id": "___",
      "tipo": "ACTIVO | PASIVO | SOLICITUD | INTERMEDIO | ALMACENAMIENTO",
      "ubicacion": { "x": ___, "y": ___ },
      "ofrece": { "___": ___ },
      "solicita": {"___": ___},
      "almacena": {"___": ___}
    }
  ],
  "robots": [
    {
      "id": "___",
      "robopuertoInicial": "___",
      "bateriaMaxima": ___,
      "capacidadTraslado": ___
    }
  ]
}
*/
