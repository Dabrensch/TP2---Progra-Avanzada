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
		this.id = contador++;

		this.ubicacion = ubicacion;
		this.alcance = alcance;
	}

	// ACCIONES

	public boolean estaConectado(Robopuerto otro) {
		return this.ubicacion.distanciaA(otro.ubicacion) <= this.alcance + otro.alcance;
	}

	public boolean estaEnAlcance(Cofre cofre) {
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
