package Cofres;

public class CofreBuffer extends Cofre {
	@Override
    public String toString() {
        return "CofreBuffer: " + id + super.toString() + ", ofrece: " + ofrece + ", solicita:" + solicita;
    }
}
