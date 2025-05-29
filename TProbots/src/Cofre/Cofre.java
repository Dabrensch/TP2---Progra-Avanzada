package Cofre;

import Clases.*;

import java.util.*;

// hacer validaciones de input en cada metodo

public abstract class Cofre {
	protected int id;
	protected Coordenada ubicacion;
	protected Red red; // si el cofre no tiene red esta fuera de una zona de cobertura y no se puede
						// operar

	Map<Item, Integer> itemsSolicitados = new LinkedHashMap<>();
	
	Map<Item, Integer> itemsSolicitadosNoResueltos = new LinkedHashMap<>(); // despues veo si lo dejo o no hace falta

	Map<Item, Integer> itemsOfrecidos = new HashMap<>();

	Map<Item, Integer> inventario = new HashMap<>();

	// estos metodos se van a sobre escribir en sus sub clases dependiento el tipo
	// de cofre

	public Cofre(int id, Coordenada ubicacion, Red red) { // agregar los map
		this.id = id;
		this.ubicacion = ubicacion;
		this.red = red;
	}

	public int getId() {
		return id;
	}

	public Coordenada getUbicacion() {
		return ubicacion;
	}

	public int getCantidad(Item item) {
		return inventario.getOrDefault(item, 0);
	}

	public Red getRed() {
		return red;
	}

	public void setRed(Red red) {
		this.red = red;
	}

	// solo para solicitud e intermedio
	public void solicitarItem() {
		// obtener item
		Map.Entry<Item, Integer> siguienteItem = itemsSolicitados.entrySet().iterator().next();
		Item item = siguienteItem.getKey();
		int cantidad = siguienteItem.getValue();

		System.out.println("El cofre X solicita X unidades del item X");
		// solicitar a robot
		int cantidadObtenida = red.solicitarItem(this, item, cantidad);
		int cantidadFaltante = cantidad - cantidadObtenida;

		// sacar del map ya que ya fue solicitado
		itemsSolicitados.remove(item);
		
		// si obtuvo algo lo agrego en el inventario
		if(cantidadObtenida!=0)
			inventario.put(item, cantidadObtenida);
		

		// si le falto obtener algo lo agrego en solicitudes no resueltas
		if (cantidadFaltante!=0) {
			System.out.println("No se puede resolver la solicitud del cofre X de X unidades del item X"); // en otro lado explicar mejor porque no se pudo
			itemsSolicitadosNoResueltos.put(item, cantidadFaltante);
		}
	}
	
	public boolean puedeSolicitar() {
		return !itemsSolicitados.isEmpty();
	}
	
	public boolean puedeOfrecer() {
		return !itemsOfrecidos.isEmpty();
	}
	
	public int puedeOfrecerItem(Item item) {
		return itemsOfrecidos.getOrDefault(item, 0);
	}
	
	
	

	// solo para cofres de provision (el de pasivo va a hacer diferente, no va a
	// llamar a la red)
	public void ofrecerItem(Item item, int cantidad) {
		// red.ofrecerItem();
	}

	public void agregarItem(Item item, int cantidad) {
		inventario.put(item, getCantidad(item) + cantidad);
	}

	public void quitarItem(Item item, int cantidad) {
		int actual = getCantidad(item);
		
		if(actual < cantidad) {
			// no puede
			return;
		}
		
		inventario.put(item, Math.max(0, actual - cantidad)); // se deberia sacar de items ofrecidos
	}



	public abstract boolean esAlmacenamiento();

	public abstract String getTipo();
}
