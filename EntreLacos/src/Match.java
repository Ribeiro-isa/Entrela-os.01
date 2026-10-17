import java.util.ArrayList;
import java.util.List;

public class Match {
    private final Perfil a;
    private final Perfil b;
    private final List<Mensagem> mensagens;

    public Match(Perfil a, Perfil b) {
        this.a = a;
        this.b = b;
        this.mensagens = new ArrayList<>();
    }

    public boolean envolve(Perfil p) {
        return p == a || p == b;
    }

    public boolean entre(Perfil x, Perfil y) {
        return (x == a && y == b) || (x == b && y == a);
    }

    public Perfil outro(Perfil p) {
        if (p == a) {
            return b;
        }
        if (p == b) {
            return a;
        }
        return null;
    }

    public void enviarMensagem(Perfil remetente, String texto) {
        if (!envolve(remetente)) {
            throw new NegocioException("Somente participantes do match enviam mensagens.");
        }
        mensagens.add(new Mensagem(remetente, texto));
    }

    public List<Mensagem> getMensagens() {
        return new ArrayList<>(mensagens);
    }

    public Perfil getA() {
        return a;
    }

    public Perfil getB() {
        return b;
    }
}