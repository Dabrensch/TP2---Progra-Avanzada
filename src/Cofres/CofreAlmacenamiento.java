package Cofres;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CofreAlmacenamiento extends Cofre {
	
	private int capacidadMaxima;
	
	protected void validar() {
        if (solicita != null && !solicita.isEmpty())
            throw new IllegalArgumentException("Un cofre de almacenamiento no puede solicitar");

        if (ofrece != null && !ofrece.isEmpty())
            throw new IllegalArgumentException("Un cofre de almacenamiento no puede ofrecer");
    }
	
	public void setCapacidadMax(int capacidad)
	{
		this.capacidadMaxima = capacidad;
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
        return "CofreAlmacenamiento: "+ id + super.toString() + ", capacidad máxima de almacenamiento: " + this.capacidadMaxima;
    }
}
