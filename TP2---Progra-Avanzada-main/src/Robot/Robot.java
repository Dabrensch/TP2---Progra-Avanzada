package Robot;

import java.util.*;

import Cofres.*;
import General.*;
import Sistema.*;

public class Robot {
    private static int contador = 1;
    private final int id;
    
	private Coordenada ubicacion;
	private Robopuerto robopuertoInicial;
	private int capacidad;
	private Bateria bateria;
	
	private Map<String, Integer> inventario = new HashMap<>();
	
	
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
	
	public void moverA(Cofre cofre) {
		int celulas = bateria.celulasNecesarias(ubicacion.distanciaA(cofre.getUbicacion()));
		if(celulas > bateria.getCelulas())
			throw new IllegalArgumentException("No llega con su bateria");	
		

		ubicacion = cofre.getUbicacion();
		bateria.descargar(celulas);
	}
	
	public void moverA(Robopuerto robopuerto) {
		int celulas = bateria.celulasNecesarias(ubicacion.distanciaA(robopuerto.getUbicacion()));
		if(celulas > bateria.getCelulas())
			throw new IllegalArgumentException("No llega con su bateria");			
		
		bateria.descargar(celulas);
		ubicacion = robopuerto.getUbicacion();


		System.out.print("Cargando... " + bateria.getCelulas() + "celulas -> ");
		this.recargar();
		System.out.println(bateria.getCelulas() + "celulas.");
	}

	
	
	public void recargar() {
		bateria.recargar();
	}
	
	
	public void cargarItem(Cofre cofre, String item, int cantidad) {
		if(!inventario.isEmpty() && !inventario.containsKey(item))
			throw new IllegalArgumentException("No puede almacenar mas de un tipo de item");
		
		if(!inventario.isEmpty()) {
			if(inventario.get(item) + cantidad > capacidad) 
				throw new IllegalArgumentException("Esa cantidad supera la capacidad del robot");
		} else if(cantidad > capacidad)
			throw new IllegalArgumentException("Esa cantidad supera la capacidad del robot");
		
		
		if(ubicacion == cofre.getUbicacion()) {
			cofre.quitarItem(item, cantidad);
		}
		
		
		if (!inventario.isEmpty() && inventario.containsKey(item)) {
		    inventario.put(item, inventario.get(item) + cantidad);
		} else {
		    inventario.put(item, cantidad);
		}
	}
	
	public void descargarItem(Cofre cofre, String item, int cantidad) {
		if(inventario.isEmpty() || !inventario.containsKey(item))
			throw new IllegalArgumentException("No tiene ese item");
		
		if(inventario.get(item) < cantidad)
			new IllegalArgumentException("No tiene esa cantidad");
		
		
		if(ubicacion == cofre.getUbicacion()) {
			cofre.guardarItem(item, cantidad);
		}
		
		
	    if ((inventario.get(item) - cantidad) > 0) {
	        inventario.put(item, inventario.get(item) - cantidad);
	    } else {
	        inventario.remove(item);
	    }
		
		
	}

	public Coordenada getUbicacion() {
		return ubicacion;
	}
	
	public int getId() {
		return id;
	}
	
	public Bateria getBateria() {
		return bateria;
	}
	
	public int getCapacidad() {
		return capacidad;
	}

	
	@Override
	public String toString() {
	    return "Robot: "+ id +
	            ", ubicacion: " + ubicacion +
	            ", capacidad: " + capacidad +
	            ", bateria: " + bateria +
	            ", robopuertoInicial: " + robopuertoInicial.getId();
	}

}
