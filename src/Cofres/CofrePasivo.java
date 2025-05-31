package Cofres;

public class CofrePasivo extends Cofre {
	@Override
    public String toString() {
        return "CofrePasivo: " + id + super.toString() + ", ofrece: " + ofrece;
    }
}
