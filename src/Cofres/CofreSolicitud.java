package Cofres;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CofreSolicitud extends Cofre {

	// CONSTRUCTOR

	protected void validar() {
		if (ofrece != null && !ofrece.isEmpty())
			throw new IllegalArgumentException("Un cofre de solicitud no puede ofrecer");

		if (almacenamiento != null && !almacenamiento.isEmpty())
			throw new IllegalArgumentException("Un cofre de solicitud no puede almacenar");
	}

	// SETTERS

	@Override
	@JsonProperty("ofrece")
	public void setOfrece(Map<String, Integer> ofrece) {
		throw new UnsupportedOperationException("Un cofre de solicitud no puede ofrecer.");
	}

	// GETTERS

	@Override
	public int getPrioridad() {
		return 0;
	}

	@Override
	public String getTipo() {
		return "CofreSolicitud";
	}

	// TO STRING

	@Override
	public String toString() {
		return "CofreSolicitud: " + id + super.toString() + ", solicita: " + solicita;
	}

}
