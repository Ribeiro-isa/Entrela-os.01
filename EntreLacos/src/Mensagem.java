public class Mensagem {
    private final Perfil remetente;
    private final String texto;

    public Mensagem(Perfil remetente, String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new NegocioException("A mensagem nao pode estar vazia.");
        }
        this.remetente = remetente;
        this.texto = texto;
    }

    public Perfil getRemetente() {
        return remetente;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public String toString() {
        return remetente.getNome() + ": " + texto;
    }
}