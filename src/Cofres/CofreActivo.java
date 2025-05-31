package Cofres;

public class CofreActivo extends Cofre {
	@Override
    public String toString() {
        return "CofreActivo: " + id + super.toString() + ", ofrece: " + ofrece;
    }
}
