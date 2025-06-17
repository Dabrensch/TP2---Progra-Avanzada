package Cofres;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CofreBuffer extends Cofre {
	protected void validar() {
        if (almacenamiento != null && !almacenamiento.isEmpty())
            throw new IllegalArgumentException("Un cofre buffer no puede almacenar");
    }

		
	@Override
    public String toString() {
        return "CofreBuffer: " + id + super.toString() + ", ofrece: " + ofrece + ", solicita:" + solicita;
    }
}
