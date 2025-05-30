package Pedido;

import Cofres.Cofre;
import Colonia.Item;

public class PedidoOfrecido extends Pedido {
	public PedidoOfrecido(Cofre cofre, Item item, int cantidad) {
		super(cofre, item, cantidad);
	}
	
	public boolean ofrece() {
		return true;
	}
	
	public boolean solicita() {
		return false;
	}
}
