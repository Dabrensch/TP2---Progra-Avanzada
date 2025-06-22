package Nodo;

import Cofres.*;
import General.Coordenada;

public class NodoCofre extends Nodo {
	private Cofre contenido;
	 
    public NodoCofre(Cofre contenido) {
        this.contenido = contenido;
    }

    @Override
    public boolean esRobopuerto() {
        return false;
    }

	@Override
	public Coordenada getUbicacion() {
		return contenido.getUbicacion();
	}
	
	public Cofre getContenido() {
		return contenido;
	}
	
	@Override
	public String toString() {
		return "Cofre " + contenido.getId();
	}
}