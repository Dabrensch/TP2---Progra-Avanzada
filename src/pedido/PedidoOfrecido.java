package pedido;

import cofres.Cofre;

public class PedidoOfrecido extends Pedido {

	// CONSTRUCTOR

	public PedidoOfrecido(Cofre cofre, String item, int cantidad) {
		super(cofre, item, cantidad);
	}

	// TIPO

	public boolean ofrece() {
		return true;
	}

	public boolean solicita() {
		return false;
	}
}
