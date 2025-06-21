package Pedido;

import Cofres.Cofre;

public class PedidoSolicitado extends Pedido {
	
	// CONSTRUCTOR

	public PedidoSolicitado(Cofre cofre, String item, int cantidad) {
		super(cofre, item, cantidad);
	}

	// TIPO

	public boolean ofrece() {
		return false;
	}

	public boolean solicita() {
		return true;
	}
}
