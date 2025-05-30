package Pedido;

import Cofres.Cofre;
import Colonia.Item;

public class PedidoSolicitado extends Pedido {
	public PedidoSolicitado(Cofre cofre, Item item, int cantidad) {
		super(cofre, item, cantidad);
	}
	
	public boolean ofrece() {
		return false;
	}
	
	public boolean solicita() {
		return true;
	}
}
