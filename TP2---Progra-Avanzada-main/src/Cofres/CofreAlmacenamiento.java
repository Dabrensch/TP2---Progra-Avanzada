package Cofres;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

public class CofreAlmacenamiento extends Cofre {
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre de almacenamiento no puede solicitar");

        if (ofrece != null && !ofrece.isEmpty())
            throw new IllegalArgumentException("Un cofre de almacenamiento no puede ofrecer");
    }
	
	@Override
	public boolean almacena() {
		return true;
	}
	
	@Override
	public void guardarItem(String item, int cantidad) {
		almacenamiento.merge(item, cantidad, Integer::sum);
	}
	

	@Override
	@JsonProperty("ofrece")
	public void setOfrece(Map<String, Integer> ofrece) {
		throw new UnsupportedOperationException("Un cofre de almacenamiento no puede ofrecer");
	}
	
	@Override
	@JsonProperty("solicita")
	public void setSolicita(Map<String, Integer> solicita) {
		throw new UnsupportedOperationException("Un cofre de almacenamiento no puede solicitar.");
	}
	
	
	@Override
    public String toString() {
        return "CofreAlmacenamiento: "+ id + super.toString() + ", almacenamiento: " + almacenamiento;
    }
}
