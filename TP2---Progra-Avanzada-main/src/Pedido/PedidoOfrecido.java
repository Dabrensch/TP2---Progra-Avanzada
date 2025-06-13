package Pedido;

import Cofres.Cofre;

public class PedidoOfrecido extends Pedido {
	public PedidoOfrecido(Cofre cofre, String item, int cantidad) {
		super(cofre, item, cantidad);
	}
	
	public boolean ofrece() {
		return true;
	}
	
	public boolean solicita() {
		return false;
	}
}
