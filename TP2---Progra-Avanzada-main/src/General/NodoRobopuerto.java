package General;

import Sistema.*;

public class NodoRobopuerto extends Nodo {
	private Robopuerto contenido; 

    public NodoRobopuerto(Robopuerto contenido) {
        this.contenido = contenido;
    }

    @Override
    public boolean esRobopuerto() {
        return true;
    }

	@Override
	public Coordenada getUbicacion() {
		return contenido.getUbicacion();
	}
	
	public Robopuerto getContenido() {
		return contenido;
	}
	
	@Override
	public String toString() {
		return "Robopuerto " + contenido.getId();
	}
}