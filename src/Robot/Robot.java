package Robot;

import java.util.*;
import Cofres.*;
import General.*;
import Robopuerto.*;

public class Robot {
	private static int contador = 1;
	private final int id;

	private Coordenada ubicacion;
	private Robopuerto robopuertoInicial;
	private int capacidad;
	private Bateria bateria;

	private Robopuerto robopuerto;

	private Map<String, Integer> inventario = new HashMap<>();

	// CONSTRUCTOR

	public Robot() {
		this.id = contador++;
	}

	public Robot(Coordenada ubicacion, Robopuerto robopuertoInicial, int capacidad, Bateria bateria) {
		if (robopuertoInicial == null || capacidad <= 0 || bateria == null)
			throw new IllegalArgumentException("Argumentos inválidos en constructor de Robot");

		this.id = contador++;
		this.ubicacion = ubicacion;
		this.robopuertoInicial = robopuertoInicial;
		this.capacidad = capacidad;
		this.bateria = bateria;
	}

	// ACCIONES

	public void moverA(Cofre cofre) {
		if (cofre == null)
			throw new IllegalArgumentException("Cofre destino nulo");
		
		int celulas = bateria.celulasNecesarias(ubicacion.distanciaA(cofre.getUbicacion()));
		if (celulas > bateria.getCelulas())
			throw new IllegalArgumentException("El robot no puede llegar al cofre por falta de batería");

		ubicacion = cofre.getUbicacion();
		bateria.descargar(celulas);
	}

	public void moverA(Robopuerto robopuerto) {
		if (robopuerto == null)
			throw new IllegalArgumentException("Robopuerto destino nulo");
		
		int celulas = bateria.celulasNecesarias(ubicacion.distanciaA(robopuerto.getUbicacion()));
		if (celulas > bateria.getCelulas())
			throw new IllegalArgumentException("El robot no puede llegar al robopuerto por falta de batería");

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
		if (cofre == null || item == null || item.isEmpty() || cantidad <= 0)
			throw new IllegalArgumentException("Parámetros inválidos al cargar item");

		if (!ubicacion.equals(cofre.getUbicacion()))
			throw new IllegalArgumentException("El robot no está en la ubicación del cofre");
		
		int totalActual = inventario.getOrDefault(item, 0);
		int totalNuevo = totalActual + cantidad;

		if (!inventario.isEmpty() && !inventario.containsKey(item))
			throw new IllegalArgumentException("El robot ya lleva otro tipo de item");

		if (totalNuevo > capacidad)
			throw new IllegalArgumentException("El robot no puede llevar tanta cantidad (excede capacidad)");

		
		cofre.quitarItem(item, cantidad);
		inventario.put(item, totalNuevo);
	}

	public void descargarItem(Cofre cofre, String item, int cantidad) {
		if (cofre == null || item == null || item.isEmpty() || cantidad <= 0)
			throw new IllegalArgumentException("Argumentos inválidos al descargar item");

		if (!ubicacion.equals(cofre.getUbicacion()))
			throw new IllegalArgumentException("El robot no está en la ubicación del cofre");

		if (!inventario.containsKey(item))
			throw new IllegalArgumentException("El robot no tiene ese item en su inventario");

		int cantidadActual = inventario.get(item);
		if (cantidad > cantidadActual)
			throw new IllegalArgumentException("No hay suficiente cantidad del item para descargar");

		cofre.guardarItem(item, cantidad);

		if (cantidadActual == cantidad)
			inventario.remove(item);
		else
			inventario.put(item, cantidadActual - cantidad);
	}

	// SETTERS

	public void setRobopuertoInicial(Robopuerto robopuerto) {
		if (robopuerto == null)
			throw new IllegalArgumentException("Robopuerto inicial no puede ser null");
		
		robopuertoInicial = robopuerto;
	}

	public void setRobopuerto(Robopuerto robopuerto) {
		if (robopuerto == null)
			throw new IllegalArgumentException("Robopuerto no puede ser null");
		
		this.robopuerto = robopuerto;
	}

	// GETTERS

	public Robopuerto getRobopuerto() {
		return robopuerto;
	}

	public Robopuerto getRobopuertoInicial() {
		return robopuertoInicial;
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

	// TO STRING

	@Override
	public String toString() {
		return "Robot: " + id + ", ubicacion: " + ubicacion + ", capacidad: " + capacidad + ", bateria: " + bateria
				+ ", robopuertoInicial: " + robopuertoInicial.getId();
	}

}
