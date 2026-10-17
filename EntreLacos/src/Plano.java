public abstract class Plano {
    public abstract int getLimiteCurtidas();

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}