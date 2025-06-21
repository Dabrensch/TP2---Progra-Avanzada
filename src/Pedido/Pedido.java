package Pedido;

import Cofres.*;

public abstract class Pedido {
	protected Cofre cofre;
	protected String item;
	protected int cantidad;

	// CONSTRUCTOR

	public Pedido(Cofre cofre, String item, int cantidad) {
		this.cofre = cofre;
		this.item = item;
		this.cantidad = cantidad;
	}

	// TIPO

	public abstract boolean ofrece();

	public abstract boolean solicita();

	// SETTERS

	public void setCofre(Cofre cofre) {
		this.cofre = cofre;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setCantidad(int cantidad) {
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
