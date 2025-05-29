package Clases;

public class Robopuerto {
	Coordenada ubicacion;
	int alcance;
	private Red red;
	
	
	boolean cubre(Coordenada destino) {
		return (Math.sqrt(Math.pow(this.ubicacion.getX() - destino.getX(), 2) + Math.pow(this.ubicacion.getY() - destino.getY(), 2))) < alcance;
	}
	
	
	public boolean seSuperponeCon(Robopuerto otro) {
		otro.ubicacion.getX();
		
	    double dx = ubicacion.getX() - otro.ubicacion.getX();
	    double dy = ubicacion.getY() - otro.ubicacion.getY();
	    double distancia = Math.sqrt(dx * dx + dy * dy);

	    return distancia <= (this.alcance + otro.alcance);
	}
	
	
	public Red getRed() {
		return red;
	}

	public void setRed(Red red) {
		this.red = red;
	}
}
