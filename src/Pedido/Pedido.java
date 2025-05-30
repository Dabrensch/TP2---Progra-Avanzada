package Pedido;

import Cofres.*;
import Colonia.Item;

public abstract class Pedido {
	protected Cofre cofre;
	protected Item item;
	protected int cantidad;
	
	public Pedido(Cofre cofre, Item item, int cantidad) {
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
