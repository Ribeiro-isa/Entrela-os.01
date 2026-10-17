import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        Scanner sc = new Scanner(System.in);
        Perfil ativo = null;
        int opcao = -1;

        do {
            try {
                System.out.println();
                System.out.println("=== ENTRELACOS ===");
                System.out.println("Usuario ativo: " + (ativo == null ? "nenhum" : ativo.getNome()));
                System.out.println("1 - Cadastrar perfil");
                System.out.println("2 - Selecionar usuario ativo");
                System.out.println("3 - Listar perfis em ordem alfabetica");
                System.out.println("4 - Pesquisar por nome");
                System.out.println("5 - Pesquisar por nome e cidade");
                System.out.println("6 - Curtir um perfil");
                System.out.println("7 - Ver meus matches");
                System.out.println("8 - Enviar mensagem");
                System.out.println("9 - Ver conversa com um match");
                System.out.println("0 - Sair");
                System.out.print("Opcao: ");
                opcao = lerInteiro(sc);

                switch (opcao) {
                    case 1:
                        ativo = cadastrarPerfil(sistema, sc);
                        break;
                    case 2:
                        ativo = selecionarPerfil(sistema, sc);
                        break;
                    case 3:
                        listarPerfis(sistema, ativo);
                        break;
                    case 4:
                        pesquisarPorNome(sistema, ativo, sc);
                        break;
                    case 5:
                        pesquisarPorNomeECidade(sistema, ativo, sc);
                        break;
                    case 6:
                        curtirPerfil(sistema, ativo, sc);
                        break;
                    case 7:
                        verMatches(sistema, ativo);
                        break;
                    case 8:
                        enviarMensagem(sistema, ativo, sc);
                        break;
                    case 9:
                        verConversa(sistema, ativo, sc);
                        break;
                    case 0:
                        System.out.println("Encerrando. Ate logo!");
                        break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente.");
                }
            } catch (NegocioException e) {
                System.out.println("Acao recusada: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Digite um numero inteiro.");
            }
        } while (opcao != 0);

        sc.close();
    }

    private static int lerInteiro(Scanner sc) {
        return Integer.parseInt(sc.nextLine().trim());
    }

    private static Perfil cadastrarPerfil(Sistema sistema, Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        System.out.print("Idade (minimo 18): ");
        int idade = lerInteiro(sc);
        System.out.print("Cidade: ");
        String cidade = sc.nextLine();
        System.out.println("Plano:");
        System.out.println("  1 - Essencial (ate 2 curtidas)");
        System.out.println("  2 - Conexao (ate 5 curtidas)");
        System.out.print("Plano: ");
        int opcaoPlano = lerInteiro(sc);
        if (opcaoPlano < 1 || opcaoPlano > 2) {
            throw new NegocioException("Plano invalido.");
        }
        Plano plano = opcaoPlano == 1 ? new PlanoEssencial() : new PlanoConexao();
        String[] interesses = lerInteresses(sc);
        Perfil novo = sistema.cadastrarPerfil(nome, email, idade, cidade, plano, interesses);
        System.out.println("Perfil cadastrado com sucesso!");
        System.out.println(exibirPerfil(novo));
        return novo;
    }

    private static String[] lerInteresses(Scanner sc) {
        String[] opcoes = CatalogoInteresses.OPCOES;
        System.out.println("Interesses disponiveis:");
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println("  " + (i + 1) + " - " + opcoes[i]);
        }
        System.out.println("Escolha ate 3 pelo numero, separados por virgula (ou 0 para nenhum): ");
        String linha = sc.nextLine().trim();
        if (linha.isEmpty() || linha.equals("0")) {
            return new String[0];
        }
        String[] escolhas = linha.split(",");
        if (escolhas.length > CatalogoInteresses.MAX_INTERESSES) {
            throw new NegocioException("No maximo " + CatalogoInteresses.MAX_INTERESSES + " interesses.");
        }
        String[] interesses = new String[escolhas.length];
        for (int i = 0; i < escolhas.length; i++) {
            try {
                int idx = Integer.parseInt(escolhas[i].trim()) - 1;
                if (idx < 0 || idx >= opcoes.length) {
                    throw new NegocioException("Opcao de interesse invalida.");
                }
                interesses[i] = opcoes[idx];
            } catch (NumberFormatException e) {
                throw new NegocioException("Digite os interesses por numero.");
            }
        }
        return interesses;
    }

    private static Perfil selecionarPerfil(Sistema sistema, Scanner sc) {
        List<Perfil> perfis = sistema.getPerfis();
        if (perfis.isEmpty()) {
            System.out.println("Nenhum perfil cadastrado. Cadastre primeiro.");
            return null;
        }
        for (Perfil p : perfis) {
            System.out.println("  [" + p.getId() + "] " + p.getNome());
        }
        System.out.print("Id do perfil: ");
        int id = lerInteiro(sc);
        Perfil p = sistema.buscarPorId(id);
        if (p == null) {
            throw new NegocioException("Perfil nao encontrado.");
        }
        System.out.println("Usuario ativo agora: " + p.getNome());
        return p;
    }

    private static void listarPerfis(Sistema sistema, Perfil ativo) {
        List<Perfil> perfis = sistema.listarPerfis(ativo);
        if (perfis.isEmpty()) {
            System.out.println("Nenhum perfil para exibir.");
            return;
        }
        for (Perfil p : perfis) {
            System.out.println(exibirPerfil(p));
        }
    }

    private static void pesquisarPorNome(Sistema sistema, Perfil ativo, Scanner sc) {
        System.out.print("Nome a pesquisar: ");
        String nome = sc.nextLine();
        List<Perfil> resultado = sistema.pesquisar(ativo, nome);
        if (resultado.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }
        for (Perfil p : resultado) {
            System.out.println(exibirPerfil(p));
        }
    }

    private static void pesquisarPorNomeECidade(Sistema sistema, Perfil ativo, Scanner sc) {
        System.out.print("Nome a pesquisar: ");
        String nome = sc.nextLine();
        System.out.print("Cidade a pesquisar: ");
        String cidade = sc.nextLine();
        List<Perfil> resultado = sistema.pesquisar(ativo, nome, cidade);
        if (resultado.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }
        for (Perfil p : resultado) {
            System.out.println(exibirPerfil(p));
        }
    }

    private static void curtirPerfil(Sistema sistema, Perfil ativo, Scanner sc) {
        if (ativo == null) {
            throw new NegocioException("Selecione um usuario ativo primeiro.");
        }
        List<Perfil> candidatos = sistema.listarPerfis(ativo);
        if (candidatos.isEmpty()) {
            throw new NegocioException("Nao ha outros perfis para curtir.");
        }
        for (Perfil p : candidatos) {
            System.out.println("  [" + p.getId() + "] " + p.getNome());
        }
        System.out.print("Id do perfil que deseja curtir: ");
        int id = lerInteiro(sc);
        Perfil alvo = sistema.buscarPorId(id);
        if (alvo == null) {
            throw new NegocioException("Perfil nao encontrado.");
        }
        Match novo = sistema.curtir(ativo, alvo);
        if (novo != null) {
            System.out.println("Deu MATCH entre " + ativo.getNome() + " e " + alvo.getNome() + "!");
        } else {
            System.out.println("Curtida enviada para " + alvo.getNome() + ".");
        }
    }

    private static void verMatches(Sistema sistema, Perfil ativo) {
        if (ativo == null) {
            throw new NegocioException("Selecione um usuario ativo primeiro.");
        }
        List<Match> matches = sistema.getMatchesDe(ativo);
        if (matches.isEmpty()) {
            System.out.println(ativo.getNome() + " ainda nao tem matches.");
            return;
        }
        System.out.println("Matches de " + ativo.getNome() + ":");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + matches.get(i).outro(ativo).getNome());
        }
    }

    private static void enviarMensagem(Sistema sistema, Perfil ativo, Scanner sc) {
        if (ativo == null) {
            throw new NegocioException("Selecione um usuario ativo primeiro.");
        }
        List<Match> matches = sistema.getMatchesDe(ativo);
        if (matches.isEmpty()) {
            throw new NegocioException(ativo.getNome() + " nao tem matches. Nao ha com quem conversar.");
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + matches.get(i).outro(ativo).getNome());
        }
        System.out.print("Escolha o match pelo numero: ");
        int idx = lerInteiro(sc) - 1;
        if (idx < 0 || idx >= matches.size()) {
            throw new NegocioException("Opcao invalida.");
        }
        Perfil destino = matches.get(idx).outro(ativo);
        System.out.print("Mensagem: ");
        String texto = sc.nextLine();
        sistema.enviarMensagem(ativo, destino, texto);
        System.out.println("Mensagem enviada para " + destino.getNome() + ".");
    }

    private static void verConversa(Sistema sistema, Perfil ativo, Scanner sc) {
        if (ativo == null) {
            throw new NegocioException("Selecione um usuario ativo primeiro.");
        }
        List<Match> matches = sistema.getMatchesDe(ativo);
        if (matches.isEmpty()) {
            throw new NegocioException(ativo.getNome() + " nao tem matches ainda.");
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + matches.get(i).outro(ativo).getNome());
        }
        System.out.print("Escolha o match pelo numero: ");
        int idx = lerInteiro(sc) - 1;
        if (idx < 0 || idx >= matches.size()) {
            throw new NegocioException("Opcao invalida.");
        }
        Match match = matches.get(idx);
        List<Mensagem> mensagens = match.getMensagens();
        if (mensagens.isEmpty()) {
            System.out.println("Nenhuma mensagem trocada ainda.");
            return;
        }
        System.out.println("Conversa com " + match.outro(ativo).getNome() + ":");
        for (Mensagem m : mensagens) {
            System.out.println("  " + m);
        }
    }

    private static String exibirPerfil(Perfil p) {
        String interesses = p.getInteresses().length == 0
                ? "sem interesses"
                : String.join(", ", p.getInteresses());
        return "#" + p.getId() + " " + p.getNome()
                + " | " + p.getIdade() + " anos"
                + " | " + p.getCidade()
                + " | " + p.getEmail()
                + " | Plano " + p.getPlano()
                + " | Interesses: " + interesses;
    }
}