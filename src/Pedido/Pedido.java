package Pedido;

import Cofres.*;

public abstract class Pedido {
	protected Cofre cofre;
	protected String item;
	protected int cantidad;
	
	public Pedido(Cofre cofre, String item, int cantidad) {
		this.cofre = cofre;
		this.item = item;
		this.cantidad = cantidad;
	}
	
	public abstract boolean ofrece();
	public abstract boolean solicita();
	

	public Cofre getCofre() {
		return cofre;
	}

	public void setCofre(Cofre cofre) {
		this.cofre = cofre;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	
	@Override
	public String toString() {
		return "Cofre: " + cofre.getId() + ", Item: " + item + ", Cantidad: " + cantidad;
	}
}
