public class PlanoEssencial extends Plano {
    public static final int LIMITE_CURTIDAS = 2;

    @Override
    public int getLimiteCurtidas() {
        return LIMITE_CURTIDAS;
    }

    @Override
    public String toString() {
        return "Essencial";
    }
}