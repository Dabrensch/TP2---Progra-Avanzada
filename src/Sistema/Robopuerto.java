package Sistema;

import Cofres.Cofre;
import General.Coordenada;

public class Robopuerto {
    private static int contador = 1;
    private final int id;
    
	public Coordenada ubicacion;
	public int alcance;
	
	public Robopuerto() {
		this.id = contador++;
	}
	
	public Robopuerto(Coordenada ubicacion, int alcance) {
		this.id = contador++;

		this.ubicacion = ubicacion;
		this.alcance = alcance;
	}
	
	public boolean estaConectado(Robopuerto otro) {
		return this.ubicacion.distanciaA(otro.ubicacion) <= this.alcance + otro.alcance; 
	}

	public boolean estaEnAlcance(Cofre cofre) {
		return this.ubicacion.distanciaA(cofre.getUbicacion()) <= this.alcance;
	}
	
	
	@Override
	public String toString() {
		return "Robopuerto: " + id + 
				", ubicacion: " + ubicacion + 
				", alcance: " + alcance;
	}

	public Coordenada getUbicacion() {
		return ubicacion;
	}
	
	public int getId() {
		return id;
	}
}
