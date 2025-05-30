package Robot;

import Cofres.*;
import Colonia.*;
import General.*;

public class Robot {
	private int id;
	private Coordenada ubicacion;
	private Robopuerto robopuertoInicial;
	private Bateria bateria;
	
	
	
	
	public Robopuerto getRobopuertoInicial() {
		return robopuertoInicial;
	}
	
	
	public void moverA(Cofre cofreOfrecido) {
		// TODO Auto-generated method stub
		
	}

	
	public void cargarItem(Cofre cofre, Item item, int cantidad) {
		if(ubicacion == cofre.getUbicacion()) {
			cofre.quitarItem(item, cantidad);
		}
	}
	
	public void descargarItem(Cofre cofre, Item item, int cantidad) {
		if(ubicacion == cofre.getUbicacion()) {
			cofre.guardarItem(item, cantidad);
		}
	}


}
