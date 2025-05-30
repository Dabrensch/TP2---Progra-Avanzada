package Cofres;

import java.util.Map;
import java.util.Set;

import Colonia.*;
import General.*;

public abstract class Cofre {
	protected int id;
	protected Coordenada ubicacion;
	
	protected Map<Item, Integer> ofrece = null;
	protected Map<Item, Integer> solicita = null;
	protected Map<Item, Integer> inventario = null;
	
	public void quitarItem(Item item, int cantidad) {
		if(ofrece.containsKey(item)) {
			if(ofrece.get(item) > cantidad) {
				ofrece.replace(item, ofrece.get(item) - cantidad);
			} else if(ofrece.get(item) == cantidad) {
				ofrece.remove(item);
			} 
		} 
	}
	
	public void guardarItem(Item item, int cantidad) {
		if(inventario.containsKey(item)) {
			inventario.replace(item, inventario.get(item) + cantidad);
		} else {
			inventario.put(item, cantidad);
		}
	}
	
	public Coordenada getUbicacion() {
		return ubicacion;
	}
	
	
	public int getPrioridad() {
		return 0;
	}
	
	public String getTipo() {
		return "Cofre";
	}
	
	
	public boolean ofrece() {
		return !ofrece.isEmpty();
	}

	public boolean solicita() {
		return !solicita.isEmpty();
	}

	public Map<Item, Integer> getOfrece() {
		return ofrece;
	}

	public Map<Item, Integer> getSolicita() {
		return solicita;
	}

	public Map<Item, Integer> getInventario() {
		return inventario;
	}


	
	
}
