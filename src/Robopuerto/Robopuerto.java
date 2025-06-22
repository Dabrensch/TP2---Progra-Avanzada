package Robopuerto;

import Cofres.Cofre;
import General.Coordenada;

public class Robopuerto {
	private static int contador = 1;
	private final int id;

	public Coordenada ubicacion;
	public int alcance;

	// CONSTRUCTOR

	public Robopuerto() {
		this.id = contador++;
	}

	public Robopuerto(Coordenada ubicacion, int alcance) {
		if (ubicacion == null || alcance < 0)
			throw new IllegalArgumentException("Argumentos invalidos en el constructor");

		this.id = contador++;

		this.ubicacion = ubicacion;
		this.alcance = alcance;
	}

	// ACCIONES

	public boolean estaConectado(Robopuerto otro) {
		if (otro == null)
			throw new IllegalArgumentException("Robopuerto destino nulo");
		
		return this.ubicacion.distanciaA(otro.ubicacion) <= this.alcance + otro.alcance;
	}

	public boolean estaEnAlcance(Cofre cofre) {
		if (cofre == null)
			throw new IllegalArgumentException("Cofre destino nulo en conexión");
		
		return this.ubicacion.distanciaA(cofre.getUbicacion()) <= this.alcance;
	}

	// GETTERS

	public Coordenada getUbicacion() {
		return ubicacion;
	}

	public int getId() {
		return id;
	}

	// TO STRING

	@Override
	public String toString() {
		return "Robopuerto: " + id + ", ubicacion: " + ubicacion + ", alcance: " + alcance;
	}

}
