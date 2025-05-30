package Colonia;

import Cofres.Cofre;
import General.Coordenada;

public class Robopuerto {
	private int id;
	private Coordenada ubicacion;
	private int alcance;
	
	public boolean estaConectado(Robopuerto otro) {
		return this.ubicacion.distanciaA(otro.ubicacion) <= this.alcance + otro.alcance; 
	}

	public boolean estaEnAlcance(Cofre cofre) {
		return this.ubicacion.distanciaA(cofre.getUbicacion()) <= this.alcance;
	}
}
