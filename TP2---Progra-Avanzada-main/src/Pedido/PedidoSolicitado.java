package Pedido;

import Cofres.Cofre;

public class PedidoSolicitado extends Pedido {
	public PedidoSolicitado(Cofre cofre, String item, int cantidad) {
		super(cofre, item, cantidad);
	}
	
	public boolean ofrece() {
		return false;
	}
	
	public boolean solicita() {
		return true;
	}
}
