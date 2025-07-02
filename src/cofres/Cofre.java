package cofres;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

import general.*;

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

	// CONSTRUCTOR

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

		if (ofrece != null) {
			almacenamiento.putAll(ofrece);
		}

		validar();
	}

	protected abstract void validar();

	// ACCIONES

	public void quitarItem(String item, int cantidad) {
		if (ofrece.containsKey(item)) {
			if (ofrece.get(item) > cantidad) {
				ofrece.put(item, ofrece.get(item) - cantidad);
				almacenamiento.put(item, almacenamiento.get(item) - cantidad);
			} else if (ofrece.get(item) == cantidad) {
				ofrece.remove(item);
				almacenamiento.remove(item);
			} else
				throw new UnsupportedOperationException("No hay esa cantidad para quitar");
		} else
			throw new UnsupportedOperationException("El cofre " + id + " no tiene ese item");
	}

	public void guardarItem(String item, int cantidad) {
		if (solicita.containsKey(item)) {
			if (solicita.get(item) > cantidad) {
				solicita.put(item, solicita.get(item) - cantidad);
			} else if (solicita.get(item) == cantidad) {
				solicita.remove(item);
			} else
				throw new UnsupportedOperationException("No solicita tanta cantidad");
		} else
			throw new UnsupportedOperationException("No solicita ese item");

		almacenamiento.merge(item, cantidad, Integer::sum);
	}

	public void desofrecer(String item) {
		if (ofrece.containsKey(item)) {
			ofrece.remove(item);
		}
	}

	// TIPO

	public boolean ofrece() {
		return !ofrece.isEmpty();
	}

	public boolean solicita() {
		return !solicita.isEmpty();
	}

	public boolean almacena() {
		return false;
	}

	public boolean esActivo() {
		return false;
	}

	// SETTERS

	public void setAlmacenamiento(Map<String, Integer> almacenamiento) {
		this.almacenamiento = almacenamiento;
	}

	public void setOfrece(Map<String, Integer> ofrece) {
		this.ofrece = ofrece;
		this.almacenamiento.putAll(ofrece);
	}

	public void setSolicita(Map<String, Integer> solicita) {
		this.solicita = solicita;
	}

	// GETTERS

	public Coordenada getUbicacion() {
		return ubicacion;
	}

	public abstract int getPrioridad();

	public abstract String getTipo();

	public int getId() {
		return id;
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

	// TO STRING

	@Override
	public String toString() {
		return ", ubicacion " + ubicacion + ", almacenamiento: " + almacenamiento;
	}

	// OTRAS
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Cofre other = (Cofre) obj;
		return this.getId() == other.getId() && this.getUbicacion().equals(other.getUbicacion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getUbicacion());
	}
}
