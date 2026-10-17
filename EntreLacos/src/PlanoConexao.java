public class PlanoConexao extends Plano {
    public static final int LIMITE_CURTIDAS = 5;

    @Override
    public int getLimiteCurtidas() {
        return LIMITE_CURTIDAS;
    }

    @Override
    public String toString() {
        return "Conexao";
    }
}