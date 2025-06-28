package Pedido;

import Cofres.*;

public abstract class Pedido {
	protected Cofre cofre;
	protected String item;
	protected int cantidad;

	// CONSTRUCTOR

	public Pedido(Cofre cofre, String item, int cantidad) {
		if (cofre == null || item == null || item.trim().isEmpty() || cantidad <= 0)
			throw new IllegalArgumentException("Argumentos invalidos en constructor");
		
		this.cofre = cofre;
		this.item = item;
		this.cantidad = cantidad;
	}

	// TIPO

	public abstract boolean ofrece();

	public abstract boolean solicita();

	// SETTERS

	public void setCofre(Cofre cofre) {
		if (cofre == null)
			throw new IllegalArgumentException("El cofre no puede ser nulo");
		
		this.cofre = cofre;
	}

	public void setItem(String item) {
		if (item == null || item.trim().isEmpty())
			throw new IllegalArgumentException("El nombre del item no puede ser nulo o vacío");
		
		this.item = item;
	}

	public void setCantidad(int cantidad) {
		//if (cantidad <= 0)
			//throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
		// Lo comento porque cuando se entregan todos ofrecidos tira illegal
		//Habria que ver si queremos que lo tire cuando la cantidad sea < 0, o mismo hacer override del metodo en PedidoOfrecido
		
		this.cantidad = cantidad;
	}

	// GETTERS

	public Cofre getCofre() {
		return cofre;
	}

	public String getItem() {
		return item;
	}

	public int getCantidad() {
		return cantidad;
	}

	// TO STRING

	@Override
	public String toString() {
		return "Cofre: " + cofre.getId() + ", Item: " + item + ", Cantidad: " + cantidad;
	}
}
