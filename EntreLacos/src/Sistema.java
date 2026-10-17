import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sistema {
    private final List<Perfil> perfis;
    private final List<Match> matches;
    private int proximoId;

    public Sistema() {
        this.perfis = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.proximoId = 1;
    }

    public Perfil cadastrarPerfil(String nome, String email, int idade, String cidade, Plano plano, String[] interesses) {
        if (emailCadastrado(email)) {
            throw new NegocioException("E-mail ja cadastrado. Escolha outro e-mail.");
        }
        if (interesses != null) {
            for (String interesse : interesses) {
                if (!CatalogoInteresses.contem(interesse)) {
                    throw new NegocioException("Interesse invalido: " + interesse);
                }
            }
        }
        Perfil novo = new Perfil(proximoId++, nome, email, idade, cidade, plano, interesses);
        perfis.add(novo);
        return novo;
    }

    private boolean emailCadastrado(String email) {
        for (Perfil p : perfis) {
            if (p.getEmail().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    public List<Perfil> getPerfis() {
        return new ArrayList<>(perfis);
    }

    public Perfil buscarPorId(int id) {
        for (Perfil p : perfis) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Perfil> listarPerfis(Perfil excluido) {
        List<Perfil> lista = new ArrayList<>();
        for (Perfil p : perfis) {
            if (p != excluido) {
                lista.add(p);
            }
        }
        lista.sort(Comparator.comparing(Perfil::getNome, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }

    public List<Perfil> pesquisar(Perfil excluido, String nome) {
        List<Perfil> lista = new ArrayList<>();
        String padrao = nome.trim().toLowerCase();
        for (Perfil p : perfis) {
            if (p == excluido) {
                continue;
            }
            if (p.getNome().toLowerCase().contains(padrao)) {
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Perfil> pesquisar(Perfil excluido, String nome, String cidade) {
        List<Perfil> lista = new ArrayList<>();
        String padrao = nome.trim().toLowerCase();
        String alvoCidade = cidade.trim();
        for (Perfil p : perfis) {
            if (p == excluido) {
                continue;
            }
            boolean nomeOk = p.getNome().toLowerCase().contains(padrao);
            boolean cidadeOk = p.getCidade().equalsIgnoreCase(alvoCidade);
            if (nomeOk && cidadeOk) {
                lista.add(p);
            }
        }
        return lista;
    }

    public Match curtir(Perfil autor, Perfil alvo) {
        autor.curtir(alvo);
        if (alvo.jaCurtiu(autor)) {
            Match match = new Match(autor, alvo);
            matches.add(match);
            return match;
        }
        return null;
    }

    public List<Match> getMatchesDe(Perfil perfil) {
        List<Match> lista = new ArrayList<>();
        for (Match m : matches) {
            if (m.envolve(perfil)) {
                lista.add(m);
            }
        }
        return lista;
    }

    public Match getMatchEntre(Perfil a, Perfil b) {
        for (Match m : matches) {
            if (m.entre(a, b)) {
                return m;
            }
        }
        return null;
    }

    public void enviarMensagem(Perfil remetente, Perfil destino, String texto) {
        Match match = getMatchEntre(remetente, destino);
        if (match == null) {
            throw new NegocioException("So e possivel conversar com quem voce deu match.");
        }
        match.enviarMensagem(remetente, texto);
    }
}