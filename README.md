# Sistema de Gerenciamento de Biblioteca

Atividade prática da disciplina de Arquiteturas de Software com Java — PUC Goiás.

## Compilar e rodar

Precisa ter Java 17 e Maven instalados.

```bash
mvn compile
mvn exec:java -Dexec.mainClass="biblioteca.apresentacao.Main"
```

Ou gerar o JAR:

```bash
mvn package
java -jar target/sistema-biblioteca-1.0.jar
```

## Etapas do projeto

**Etapa 1 — Arquitetura em Camadas:** separação entre domínio, infraestrutura, aplicação e apresentação. As dependências fluem sempre de cima para baixo.

**Etapa 2 — Arquitetura Hexagonal:** o domínio passa a depender apenas de interfaces (portas). Os detalhes de persistência ficam nos adaptadores, que podem ser trocados sem alterar nada no núcleo.

**Etapa 3 — Eventos assíncronos:** um `EventBus` genérico conecta o serviço de empréstimo aos consumidores sem que eles se conheçam diretamente.

## Decisões de design

- A regra de disponibilidade de exemplares fica dentro de `Livro.realizarEmprestimo()`, não no serviço, porque é uma invariante do domínio.
- O separador do CSV é `;` — títulos e nomes com ponto e vírgula podem causar problema na leitura, limitação conhecida.
- O `EventBus` usa `Consumer<T>` do Java padrão, sem dependência de biblioteca externa.

## Dificuldades

- Garantir que nenhuma classe de domínio referenciasse infraestrutura foi o ponto que exigiu mais atenção, especialmente ao mover o `EmprestimoServico` na Etapa 2.
- A leitura do CSV precisou de `-1` no `split` para não cortar campos vazios no final da linha.
