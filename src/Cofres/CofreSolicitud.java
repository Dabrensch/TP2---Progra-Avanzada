package Cofres;

public class CofreSolicitud extends Cofre {
	@Override
    public String toString() {
        return "CofreSolicitud: " + id + super.toString() + ", solicita: " + solicita;
    }
}
