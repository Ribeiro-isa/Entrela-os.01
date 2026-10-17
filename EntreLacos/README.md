# EntreLacos

Trabalho A3 - PSC (Programacao de Solucoes Computacionais)

Aplicativo ficticio de encontros para adultos, em Java orientado a objetos. O programa roda no
console, com dados em memoria e um menu para alternar o usuario ativo.

## Integrantes e turma

- Nome do integrante Isadora Ribeiro Eugênio
- Nome do integrante 2 
- Nome do integrante 3 (se houver)

Turma: [informar a turma]

## Requisitos

- Java 8 ou superior (testado com JDK 26).

Os testes automaticos usam lambdas (`Runnable`), por isso o minimo e Java 8.

## Como compilar e executar

A partir da pasta do projeto (`EntreLacos`):

    javac -encoding UTF-8 -d out src\*.java

O `-encoding UTF-8` evita problemas com acentos em outros sistemas. Se preferir, os
arquivos podem ser compilados sem essa opcao.

Para executar o programa (menu):

    java -cp out Main

## Resumo das classes

- `Main` - menu do console. Le a entrada, chama as acoes do `Sistema` e exibe mensagens
  claras para acoes recusadas via `NegocioException`. Mantem o usuario ativo, permitindo
  alternar entre perfis para simular conversas.
- `Sistema` - regras de negocio e organizacao dos dados em memoria. Cadastra perfis (com
  e-mail unico), lista em ordem alfabetica, pesquisa por nome ou nome + cidade (metodos
  sobrecarregados de mesmo nome), cria matches, controla curtidas e mensagens.
- `Perfil` - representa um usuario, com atributos protegidos: id unico, nome, e-mail,
  idade, cidade, plano, vetor de ate tres interesses e lista de perfis curtidos. Valida
  dados obrigatorios, idade minima de 18 anos e ausencia de interesses repetidos.
- `Plano` - classe abstrata comum aos planos. Cada plano sobrescreve o metodo
  `getLimiteCurtidas()`, consultado de forma polimorfica pelo sistema.
- `PlanoEssencial` - limita o envio de curtidas a 2 por usuario (constante `LIMITE_CURTIDAS`).
- `PlanoConexao` - limita o envio de curtidas a 5 por usuario (constante `LIMITE_CURTIDAS`).
- `CatalogoInteresses` - catalogo de interesses que existe de forma independente dos
  perfis. Valida se um interesse pertence ao catalogo e define o maximo de 3 interesses.
- `Match` - associa dois perfis e reune as mensagens trocadas entre eles.
- `Mensagem` - possui remetente e texto; o texto nao pode ser vazio.
- `NegocioException` - excecao de negocio usada para recusar acoes (e-mail duplicado,
  limite de curtidas atingido, mensagem sem match, etc.) sem encerrar o programa.

## Principais decisoes de projeto

- O limite de curtidas considera todas as curtidas enviadas durante a execucao; tentativas
  recusadas (curtir a si mesmo, curtir duas vezes a mesma pessoa ou exceder o limite) nao
  chegam a ser contadas, portanto nao consomem limite.
- Um match e criado apenas quando duas pessoas se curtem: ao curtir alguem que ja havia te
  curtido, o `Sistema` cria um unico `Match` entre os dois.
- Mensagens so sao aceitas entre perfis que possuem um `Match`.

## Testes realizados

Ha uma classe de testes `TesteSistema` que cobre os principais cenarios:

- Cadastro: campos obrigatorios, idade minima, e-mail duplicado (case insensitive),
  interesses repetidos ou fora do catalogo e limites dos planos via polimorfismo.
- Listagem e pesquisa: ordem alfabetica, exclusao do usuario ativo, pesquisa por nome e
  por nome + cidade (metodos sobrecarregados) e ausencia de resultados.
- Curtidas e matches: impossibilidade de curtir a si mesmo ou a mesma pessoa duas vezes,
  limite do plano atingido, match unico quando ha curtida reciproca e confirmacao de que
  tentativas recusadas nao alteram os dados.
- Mensagens: bloqueio sem match, mensagem vazia recusada e ordem de envio preservada.

Como executar os testes:

    javac -encoding UTF-8 -d out src\*.java
    java -cp out TesteSistema

## Exemplo rapido de uso no console

1. Cadastre dois perfis (menu opcao 1), um com plano Essencial e outro Conexao.
2. Selecione o usuario ativo (opcao 2).
3. Liste os perfis (opcao 3) e curta o outro perfil (opcao 6).
4. Alterne o usuario ativo (opcao 2) e curta de volta para formar o match (opcao 6).
5. Verifique os matches (opcao 7) e troque mensagens (opcoes 8 e 9), alternando o
   usuario ativo para simular a conversa.