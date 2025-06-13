package General;

public abstract class Nodo {
	
	public double distanciaA(Nodo otro) {
		return this.getUbicacion().distanciaA(otro.getUbicacion());
	}
    
    public abstract Coordenada getUbicacion();

    // Método abstracto para saber si el nodo es robopuerto
    public abstract boolean esRobopuerto();
}



