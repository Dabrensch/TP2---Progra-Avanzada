package Robot;

import Cofres.*;
import General.*;
import Sistema.*;

public class Robot {
    private static int contador = 1;
    private final int id;
    
	private Coordenada ubicacion;
	private Robopuerto robopuertoInicial;
	public int capacidad;
	public Bateria bateria;
	
	
	public Robot() {
		this.id = contador++;
	}
	
	public Robot(Coordenada ubicacion, Robopuerto robopuertoInicial, int capacidad, Bateria bateria) {
		this.id = contador++;
		this.ubicacion = ubicacion;
		this.robopuertoInicial = robopuertoInicial;
		this.capacidad = capacidad;
		this.bateria = bateria;
	}




	public void setRobopuertoInicial(Robopuerto robopuerto) {
		robopuertoInicial = robopuerto;
	}
	
	public Robopuerto getRobopuertoInicial() {
		return robopuertoInicial;
	}
	
	public void moverA(Cofre cofreOfrecido) {
		// TODO Auto-generated method stub
		
	}

	
	public void cargarItem(Cofre cofre, String item, int cantidad) {
		if(ubicacion == cofre.getUbicacion()) {
			cofre.quitarItem(item, cantidad);
		}
	}
	
	public void descargarItem(Cofre cofre, String item, int cantidad) {
		if(ubicacion == cofre.getUbicacion()) {
			cofre.guardarItem(item, cantidad);
		}
	}

	public Coordenada getUbicacion() {
		return ubicacion;
	}

	
	@Override
	public String toString() {
	    return "Robot: "+ id +
	            ", ubicacion:" + ubicacion +
	            ", capacidad:" + capacidad +
	            ", bateria:" + bateria +
	            ", robopuertoInicial: " + robopuertoInicial.getId();
	}

}
