import java.util.ArrayList;
import java.util.List;

public class Perfil {
    protected int id;
    protected String nome;
    protected String email;
    protected int idade;
    protected String cidade;
    protected Plano plano;
    protected String[] interesses;
    protected List<Perfil> curtidos;

    public Perfil(int id, String nome, String email, int idade, String cidade, Plano plano, String[] interesses) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new NegocioException("Nome e obrigatorio.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new NegocioException("E-mail e obrigatorio.");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new NegocioException("Cidade e obrigatoria.");
        }
        if (idade < 18) {
            throw new NegocioException("Idade minima e 18 anos.");
        }
        if (plano == null) {
            throw new NegocioException("Plano e obrigatorio.");
        }
        if (interesses == null || interesses.length > CatalogoInteresses.MAX_INTERESSES) {
            throw new NegocioException("No maximo " + CatalogoInteresses.MAX_INTERESSES + " interesses.");
        }
        if (temInteresseRepetido(interesses)) {
            throw new NegocioException("Interesses nao podem se repetir.");
        }
        this.id = id;
        this.nome = nome.trim();
        this.email = email.trim();
        this.idade = idade;
        this.cidade = cidade.trim();
        this.plano = plano;
        this.interesses = interesses.clone();
        this.curtidos = new ArrayList<>();
    }

    private boolean temInteresseRepetido(String[] valores) {
        for (int i = 0; i < valores.length; i++) {
            for (int j = i + 1; j < valores.length; j++) {
                if (valores[i].equalsIgnoreCase(valores[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getLimiteCurtidas() {
        return plano.getLimiteCurtidas();
    }

    public boolean podeCurtir() {
        return curtidos.size() < plano.getLimiteCurtidas();
    }

    public boolean jaCurtiu(Perfil outro) {
        return curtidos.contains(outro);
    }

    public void curtir(Perfil alvo) {
        if (this == alvo) {
            throw new NegocioException("Voce nao pode curtir o proprio perfil.");
        }
        if (jaCurtiu(alvo)) {
            throw new NegocioException("Voce ja curtiu este perfil.");
        }
        if (!podeCurtir()) {
            throw new NegocioException("Limite de curtidas do plano " + plano + " atingido (" + plano.getLimiteCurtidas() + ").");
        }
        curtidos.add(alvo);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public int getIdade() {
        return idade;
    }

    public String getCidade() {
        return cidade;
    }

    public Plano getPlano() {
        return plano;
    }

    public String[] getInteresses() {
        return interesses.clone();
    }
}