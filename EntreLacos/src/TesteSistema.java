import java.util.List;

public class TesteSistema {
    private static int total = 0;
    private static int falhas = 0;

    public static void main(String[] args) {
        testarCadastro();
        testarListagemEPesquisa();
        testarCurtidasEMatches();
        testarMensagens();

        System.out.println();
        System.out.println("Resultado: " + (total - falhas) + "/" + total + " testes passaram.");
        if (falhas > 0) {
            System.out.println("HOUVE FALHAS!");
            System.exit(1);
        }
        System.out.println("Todos os testes passaram.");
    }

    private static void testarCadastro() {
        Sistema s = novoAmbiente();

        igual("Cadastro Ana", s.cadastrarPerfil("Ana", "ana@teste.com", 25, "SP", new PlanoEssencial(), new String[]{}).getNome(), "Ana");
        igual("Cadastro Bruno", s.cadastrarPerfil("Bruno", "bruno@teste.com", 30, "RJ", new PlanoConexao(), new String[]{"ESPORTES"}).getNome(), "Bruno");
        igual("Cadastro Carla", s.cadastrarPerfil("Carla", "carla@teste.com", 22, "SP", new PlanoEssencial(), new String[]{"MUSICA", "CINEMA"}).getNome(), "Carla");
        igual("Cadastro Duda", s.cadastrarPerfil("Duda", "duda@teste.com", 19, "MG", new PlanoEssencial(), new String[]{"VIAGENS", "JOGOS", "ARTE"}).getNome(), "Duda");

        esperaNegocio("E-mail duplicado (case insensitive)", () -> s.cadastrarPerfil("Zeca", "ANA@TESTE.COM", 20, "SP", new PlanoEssencial(), new String[]{}));
        esperaNegocio("Idade abaixo de 18", () -> s.cadastrarPerfil("Leo", "leo@teste.com", 17, "SP", new PlanoEssencial(), new String[]{}));
        esperaNegocio("Nome vazio", () -> s.cadastrarPerfil("", "x@teste.com", 20, "SP", new PlanoEssencial(), new String[]{}));
        esperaNegocio("Interesse repetido", () -> s.cadastrarPerfil("Rui", "rui@teste.com", 20, "SP", new PlanoEssencial(), new String[]{"MUSICA", "MUSICA"}));
        esperaNegocio("Interesse fora do catalogo", () -> s.cadastrarPerfil("Tati", "tati@teste.com", 20, "SP", new PlanoEssencial(), new String[]{"OUTDOOR"}));

        igual("Limite Essencial via polimorfismo", new PlanoEssencial().getLimiteCurtidas(), 2);
        igual("Limite Conexao via polimorfismo", new PlanoConexao().getLimiteCurtidas(), 5);
    }

    private static void testarListagemEPesquisa() {
        Sistema s = novoAmbiente();
        Perfil ana = s.cadastrarPerfil("Ana", "ana@teste.com", 25, "SP", new PlanoEssencial(), new String[]{});
        s.cadastrarPerfil("Bruno", "bruno@teste.com", 30, "RJ", new PlanoConexao(), new String[]{});
        s.cadastrarPerfil("Carla", "carla@teste.com", 22, "SP", new PlanoEssencial(), new String[]{});
        s.cadastrarPerfil("Duda", "duda@teste.com", 19, "MG", new PlanoEssencial(), new String[]{});

        List<Perfil> lista = s.listarPerfis(ana);
        igual("Lista sem o proprio usuario tem 3", lista.size(), 3);
        igual("Ordem alfabetica (1)", lista.get(0).getNome(), "Bruno");
        igual("Ordem alfabetica (2)", lista.get(1).getNome(), "Carla");
        igual("Ordem alfabetica (3)", lista.get(2).getNome(), "Duda");

        List<Perfil> todos = s.listarPerfis(null);
        igual("Lista completa tem 4", todos.size(), 4);

        List<Perfil> porNome = s.pesquisar(ana, "br");
        igual("Pesquisa por nome acha Bruno", porNome.size(), 1);
        igual("Pesquisa por nome retorna Bruno", porNome.get(0).getNome(), "Bruno");

        List<Perfil> porNomeCidade = s.pesquisar(ana, "a", "SP");
        igual("Pesquisa nome+cidade acha Carla (exclui Ana)", porNomeCidade.size(), 1);
        igual("Pesquisa nome+cidade retorna Carla", porNomeCidade.get(0).getNome(), "Carla");

        List<Perfil> vazia = s.pesquisar(ana, "zzz");
        igual("Pesquisa sem resultado fica vazia", vazia.size(), 0);
    }

    private static void testarCurtidasEMatches() {
        Sistema s = novoAmbiente();
        Perfil ana = s.cadastrarPerfil("Ana", "ana@teste.com", 25, "SP", new PlanoEssencial(), new String[]{});
        Perfil bruno = s.cadastrarPerfil("Bruno", "bruno@teste.com", 30, "RJ", new PlanoConexao(), new String[]{});
        Perfil carla = s.cadastrarPerfil("Carla", "carla@teste.com", 22, "SP", new PlanoEssencial(), new String[]{});
        Perfil duda = s.cadastrarPerfil("Duda", "duda@teste.com", 19, "MG", new PlanoEssencial(), new String[]{});

        esperaAceitar("Ana curte Bruno", () -> s.curtir(ana, bruno));
        esperaAceitar("Ana curte Carla", () -> s.curtir(ana, carla));
        esperaNegocio("Ana nao pode curtir a si mesma", () -> s.curtir(ana, ana));
        esperaNegocio("Ana nao pode curtir Bruno 2x", () -> s.curtir(ana, bruno));
        esperaNegocio("Ana atinge limite do plano Essencial", () -> s.curtir(ana, duda));

        igual("Ana usou as 2 curtidas", ana.podeCurtir(), false);
        igual("Tentativa recusada nao criou match", s.getMatchEntre(ana, duda) == null, true);

        esperaAceitar("Bruno curte Ana (gera match)", () -> s.curtir(bruno, ana));
        igual("Ana e Bruno tem 1 match", s.getMatchEntre(ana, bruno) != null, true);
        igual("Continua sem match com a Duda", s.getMatchEntre(ana, duda) == null, true);

        Perfil bia = s.cadastrarPerfil("Bia", "bia@teste.com", 21, "PE", new PlanoConexao(), new String[]{});
        esperaAceitar("Carla curte Bruno (1a curtida)", () -> s.curtir(carla, bruno));
        esperaAceitar("Carla curte Bia (2a curtida)", () -> s.curtir(carla, bia));
        esperaNegocio("Carla atinge limite do plano Essencial", () -> s.curtir(carla, duda));
    }

    private static void testarMensagens() {
        Sistema s = novoAmbiente();
        Perfil ana = s.cadastrarPerfil("Ana", "ana@teste.com", 25, "SP", new PlanoEssencial(), new String[]{});
        Perfil bruno = s.cadastrarPerfil("Bruno", "bruno@teste.com", 30, "RJ", new PlanoConexao(), new String[]{});
        Perfil carla = s.cadastrarPerfil("Carla", "carla@teste.com", 22, "SP", new PlanoEssencial(), new String[]{});

        esperaNegocio("Sem match nao pode conversar", () -> s.enviarMensagem(ana, carla, "oi"));

        s.curtir(ana, bruno);
        s.curtir(bruno, ana);

        esperaAceitar("Ana envia mensagem para Bruno", () -> s.enviarMensagem(ana, bruno, "Oi, tudo bem?"));
        esperaAceitar("Bruno responde para Ana", () -> s.enviarMensagem(bruno, ana, "Tudo! E voce?"));
        esperaNegocio("Mensagem vazia", () -> s.enviarMensagem(ana, bruno, "  "));

        List<Mensagem> conversa = s.getMatchEntre(ana, bruno).getMensagens();
        igual("Duas mensagens na conversa", conversa.size(), 2);
        igual("Ordem e de envio (1)", conversa.get(0).toString(), "Ana: Oi, tudo bem?");
        igual("Ordem e de envio (2)", conversa.get(1).toString(), "Bruno: Tudo! E voce?");
    }

    private static Sistema novoAmbiente() {
        return new Sistema();
    }

    private static void esperaAceitar(String nome, Runnable acao) {
        total++;
        try {
            acao.run();
            System.out.println("[OK] " + nome);
        } catch (NegocioException e) {
            falhas++;
            System.out.println("[FALHOU] " + nome + " (recusado indevidamente): " + e.getMessage());
        }
    }

    private static void esperaNegocio(String nome, Runnable acao) {
        total++;
        try {
            acao.run();
            falhas++;
            System.out.println("[FALHOU] " + nome + " (nao foi recusado)");
        } catch (NegocioException e) {
            System.out.println("[OK] " + nome + " (recusado: " + e.getMessage() + ")");
        }
    }

    private static void igual(String nome, Object obtido, Object esperado) {
        total++;
        if (java.util.Objects.equals(obtido, esperado)) {
            System.out.println("[OK] " + nome);
        } else {
            falhas++;
            System.out.println("[FALHOU] " + nome + " -> esperado: " + esperado + ", obtido: " + obtido);
        }
    }
}