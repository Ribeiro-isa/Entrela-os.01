public final class CatalogoInteresses {
    public static final int MAX_INTERESSES = 3;
    public static final String[] OPCOES = {
            "MUSICA", "CINEMA", "ESPORTES", "LEITURA", "GASTRONOMIA",
            "VIAGENS", "JOGOS", "FOTOGRAFIA", "ANIMAIS", "ARTE"
    };

    private CatalogoInteresses() {
    }

    public static boolean contem(String interesse) {
        if (interesse == null) {
            return false;
        }
        String valor = interesse.trim().toUpperCase();
        for (String opcao : OPCOES) {
            if (opcao.equals(valor)) {
                return true;
            }
        }
        return false;
    }
}