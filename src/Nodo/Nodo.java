package Nodo;

import General.Coordenada;

public abstract class Nodo {

	public double distanciaA(Nodo otro) {
		return this.getUbicacion().distanciaA(otro.getUbicacion());
	}

	public abstract Coordenada getUbicacion();

	public abstract boolean esRobopuerto();
}
