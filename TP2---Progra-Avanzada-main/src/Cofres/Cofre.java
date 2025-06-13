package Cofres;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.*;

import General.*;
import Sistema.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({ @JsonSubTypes.Type(value = CofreActivo.class, name = "activo"),
		@JsonSubTypes.Type(value = CofreAlmacenamiento.class, name = "almacenamiento"),
		@JsonSubTypes.Type(value = CofreBuffer.class, name = "buffer"),
		@JsonSubTypes.Type(value = CofrePasivo.class, name = "pasivo"),
		@JsonSubTypes.Type(value = CofreSolicitud.class, name = "solicitud") })

public abstract class Cofre {
    private static int contador = 1;
    protected final int id;
    
	protected Coordenada ubicacion;

	protected Map<String, Integer> ofrece = null;
	protected Map<String, Integer> solicita = null;
	protected Map<String, Integer> almacenamiento = new HashMap<>();

	public Cofre() {
		this.id = contador++;
	}

	public Cofre(Coordenada ubicacion, Map<String, Integer> ofrece, Map<String, Integer> solicita,
			Map<String, Integer> almacenamiento) {
		this.id = contador++;

		this.ubicacion = ubicacion;
		this.ofrece = ofrece;
		this.solicita = solicita;
		this.almacenamiento = almacenamiento;
		
		validar();
	}
	
	protected abstract void validar();

	public void quitarItem(String item, int cantidad) {
		if (ofrece.containsKey(item)) {
			if (ofrece.get(item) > cantidad) {
				ofrece.put(item, ofrece.get(item) - cantidad);
			} else if (ofrece.get(item) == cantidad) {
				ofrece.remove(item);
			}
		}
	}

	public void guardarItem(String item, int cantidad) {
		if (almacenamiento.containsKey(item)) {
			almacenamiento.put(item, almacenamiento.get(item) + cantidad);
		} else {
			almacenamiento.put(item, cantidad);
		}
	}

	public Coordenada getUbicacion() {
		return ubicacion;
	}

	public int getPrioridad() {
		return 0;
	}

	public String getTipo() {
		return "Cofre";
	}

	public boolean ofrece() {
		return !ofrece.isEmpty();
	}

	public boolean solicita() {
		return !solicita.isEmpty();
	}

	public Map<String, Integer> getOfrece() {
		return ofrece;
	}

	public Map<String, Integer> getSolicita() {
		return solicita;
	}

	public Map<String, Integer> getAlmacenamiento() {
		return almacenamiento;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return ", ubicacion " + ubicacion;
	}

}
