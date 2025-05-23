package aplicacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


public class Ache {
	/*
	 * 
	 * IDENTIFICACAO DO GRUPO
	 * Ana Luiza Oliveira Dourado - 558793
	 * Felipe Wapf Feetback - 557217
	 * Lucas Rodrigues Grecco - 558261
	 * Monique Ferreira dos Anjos - 558262
	 * Ronaldo Veloso Filho - 556445
	 * 
	 */

	public static class Produto {
		// Definindo atributos da classe Produto
		String nome;
		int codigo, producaoMax, producaoMes1, producaoMes2, producaoMes3;
		double producaoMedia, porcentagemNaoProducao;

		// Método para formartar a impressão dos dados
		public String formatarLinha(){
			return String.format("%-8d %-20s %-15d %-12d %-12d %-12d %-15.2f %-15.2f\n",
			codigo, nome, producaoMax, producaoMes1, producaoMes2, producaoMes3, producaoMedia, porcentagemNaoProducao);
		}
		
		public double calcularMedia(int producaoMes1, int producaoMes2, int producaoMes3){
			return (producaoMes1 + producaoMes2 + producaoMes3) / 3.0;
		}
		public double calcularPorcentagemNaoProducao(double producaoMedia, int producaoMax){
			return ((producaoMax - producaoMedia) / producaoMax) * 100;
		}
	}
	
	public static final int N = 30;

	// Método para Imprimir o Cabeçalho 
	public static String imprimirCabecalho(){
		return String.format("\n%-8s %-20s %-15s %-12s %-12s %-12s %-15s %-15s\n",
		"Codigo", "Fabrica", "Prod. Maxima", "Prod. Mes 1", "Prod. Mes 2", "Prod. Mes 3", "Media Producao", "% Nao Producao");
	}
	// Função fabricaMaiorProducao() que procura a fabrica com maior capacidade de producao de um produto
	public static int fabricaMaiorProducao(int codInput , ArrayList<Produto> produtos){
		int pos = -1;
		int maior = 0;
		for (int i = 0; i < produtos.size(); i++) {
			if (codInput == produtos.get(i).codigo) {
				if (produtos.get(i).producaoMax > maior) {
					maior = produtos.get(i).producaoMax;
					pos = i;
				}
			}
		}
		return pos;
	}
	// Classe auxiliar para retornar vetor de índices e quantidade de elementos encontrados
	public static class ResultadoBusca {
		public int[] indices;
		public int quantidade;
		public ResultadoBusca(int[] indices, int quantidade) {
			this.indices = indices;
			this.quantidade = quantidade;
		}
	}
	// Busca a fabrica e retorna um objeto com os indices das fabricas que fabricam o produto e o número de elementos encontrados
	public static ResultadoBusca buscaPorFabrica(String fabInput, ArrayList<Produto> produtos) {
		int[] indices = new int[1];
		int cont = 0;
		for (int i = 0; i < N; i++) {
			String nome = produtos.get(i).nome.trim();
			if (nome.equalsIgnoreCase(fabInput.trim())) {
				if (cont == indices.length) {
					int[] novoIndices = new int[indices.length + 1];
					System.arraycopy(indices, 0, novoIndices, 0, indices.length);
					indices = novoIndices;
				}
				indices[cont] = i;
				cont++;
			}
		}
		// Ajusta o tamanho do vetor para a quantidade encontrada
		int[] resultado = new int[cont];
		System.arraycopy(indices, 0, resultado, 0, cont);
		return new ResultadoBusca(resultado, cont);
	}
	// Função selecionaProdAbaixo() gera um objeto com os índices onde o percentual de produção está abaixo do limite
	public static ResultadoBusca selecionaProdAbaixo(double porcLimiteInput, ArrayList<Produto> produtos){
		int[] indices = new int[1];
		int cont = 0;
		for (int i = 0; i < N; i++) {
			if (produtos.get(i).porcentagemNaoProducao > porcLimiteInput) {
				if (cont == indices.length) {
					int[] novoIndices = new int[indices.length + 1];
					System.arraycopy(indices, 0, novoIndices, 0, indices.length);
					indices = novoIndices;
				}
				indices[cont] = i;
				cont++;
			}
		}
		int[] resultado = new int[cont];
		System.arraycopy(indices, 0, resultado, 0, cont);
		return new ResultadoBusca(resultado, cont);
	}
	
	public static void main(String[] args) {
		// Obtem caminho da pasta workspace da IDE
		String diretorio = System.getProperty("user.dir");
		// Concatena caminho ate a pasta workspace da IDE com arquivo disponilizado no projeto
		String caminhoDoArquivo = diretorio + "/src/arquivos/planilhaProducao.csv";

		/*
		 * Declarar vetores usado no programa
		 * As linhas com erros no codigo precisam desses vetores  
		 */
		// Vetor produtos para armazenar os dados do arquivo CSV
		ArrayList<Produto> produtos = new ArrayList<Produto>();

		try {
			// Cria um objeto File com o caminho do arquivo
			File arquivo = new File(caminhoDoArquivo);

                    try ( // Cria um objeto da classe Scanner para ler o arquivo
                            Scanner leArq = new Scanner(arquivo)) {
                        int i = 0;
                        while (leArq.hasNextLine() && i < N) {
							Produto produto = new Produto();
                            // Le uma linha do arquivo
                            String linha = leArq.nextLine();
                            //separa em Strings cada parte separada por ";"
                            String[] partes = linha.split(";");
                            // atribui a cada vetor declarado anteriormente as partes separadas
                            // fazendo conversao de String para int quando necessario
                            produto.codigo = Integer.parseInt(partes[0]);
                            produto.nome = partes[1];
                            produto.producaoMax = Integer.parseInt(partes[2]);
                            produto.producaoMes1 = Integer.parseInt(partes[3]);
                            produto.producaoMes2 = Integer.parseInt(partes[4]);
                            produto.producaoMes3 = Integer.parseInt(partes[5]);
							produto.producaoMedia = produto.calcularMedia(produto.producaoMes1, produto.producaoMes2, produto.producaoMes3);
							produto.porcentagemNaoProducao = produto.calcularPorcentagemNaoProducao(produto.producaoMedia, produto.producaoMax);
							// armazenar produto
							produtos.add(produto);
                            i++;
                        }
                        // Fechar o objeto da classe Scanner le
                    }
		} catch (FileNotFoundException e) {
			// Caso o arquivo n�o seja encontrado
			System.out.println("Arquivo nao encontrado: " + e.getMessage());
		}
		// Imprimir todos os dados
		System.out.println("\nQuantidade de Produtos : " + produtos.size());
		System.out.println(imprimirCabecalho());
	
		for (Produto produto : produtos){
			System.out.println(produto.formatarLinha());
		}
		try (Scanner le = new Scanner(System.in)) {
			
			int opcao;
			
			do {
				// Removido limparTerminal();
				System.out.println("0 - Encerrar");
				System.out.println("1 - Procura fabrica com maior capacidade de producao de um produto");
				System.out.println("2 - Procura produtos fabricados em uma especifica unidade");
				System.out.println("3 - Seleciona e apresenta produtos com percentual de nao producao acima de um limite");
				System.out.println("4 - Inserir um novo produto");
				System.out.println("5 - Listagem Geral");
				System.out.println("6 - Listagem em Ordem Decrescente - Nao Producao");
				System.out.println("7 - Listagem em Ordem Alfabetica");
				System.out.print("Opcao: ");
				opcao = le.nextInt();
				le.nextLine();
				switch (opcao) {
					case 0 -> System.out.println("Encerrado o programa.");
					case 1 -> {
						// Removido limparTerminal();
						System.out.print("Qual o codigo do produto a ser pesquisado? ");
						int codProcurado = le.nextInt();
						// Chama a funcao que procura a fabrica com maior capacidade de producao de um produto
						int pos = fabricaMaiorProducao(codProcurado, produtos);
						if (pos != -1) {
							System.out.println("Fabrica com maior capacidade de producao do produto " + codProcurado + ": ");
							// Chama a funcao que imprime os dados da fabrica
							System.out.println(imprimirCabecalho());
							System.out.println(produtos.get(pos).formatarLinha());
						} else {
							System.out.println("Produto nao encontrado.");
						}
					}
					case 2 -> {
						// Removido limparTerminal();
						System.out.print("Fabrica para pesquisar produtos:");
						String fab = le.nextLine();
						System.out.println(fab);
						ResultadoBusca resultado = buscaPorFabrica(fab, produtos);
						if (resultado.quantidade > 0) {
							System.out.println("\nQuantidade de Produtos Encontrados : " + resultado.quantidade);
							System.out.println("Produtos fabricados na unidade " + fab + ": ");
							System.out.println(imprimirCabecalho());
							for (int j = 0; j < resultado.quantidade; j++) {
								int indice = resultado.indices[j];
								System.out.println(produtos.get(indice).formatarLinha());
							}
							System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
						} else {
							System.out.println("Fabrica nao encontrada.");
						}
					}
					case 3 -> {
						// Removido limparTerminal();
						System.out.print("Qual o valor limite de percentual nao produzido que se deseja pesquisar: ");
						double porcLimite = le.nextDouble();
						ResultadoBusca resultado = selecionaProdAbaixo(porcLimite, produtos);
						if (resultado.quantidade > 0) {
							System.out.println("\nQuantidade de Produtos Encontrados : " + resultado.quantidade);
							System.out.println("Produtos com percentual de nao producao acima de " + porcLimite + "% : ");
							System.out.println(imprimirCabecalho());
							for (int j = 0; j < resultado.quantidade; j++) {
								int indice = resultado.indices[j];
								System.out.println(produtos.get(indice).formatarLinha());
							}
							System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
						} else {
							System.out.println("Nenhum produto encontrado com percentual de nao producao acima de " + porcLimite + ".");
						}
					}
					case 4 -> {
						// Removido limparTerminal();
						if (produtos.size() < 100){
							Produto novoProduto = new Produto();
							System.out.println("\nCadastro de Novos Produtos");
							System.out.println("Insira o código do produto (0000) :");
							novoProduto.codigo = le.nextInt();
							le.nextLine(); // Consumir a quebra de linha pendente
							System.out.println("Insira o nome da fábrica :");
							novoProduto.nome = le.nextLine();
							System.out.println("Insira a producao Maxima (número inteiro):");
							novoProduto.producaoMax = le.nextInt();
							System.out.println("Insira a producao do mês 1 (número inteiro) :");
							novoProduto.producaoMes1 = le.nextInt();
							System.out.println("Insira a producao do mês 2 (número inteiro) :");
							novoProduto.producaoMes2 = le.nextInt();
							System.out.println("Insira a producao do mês 3 (número inteiro) :");
							novoProduto.producaoMes3 = le.nextInt();
							novoProduto.producaoMedia = novoProduto.calcularMedia(novoProduto.producaoMes1, novoProduto.producaoMes2, novoProduto.producaoMes3);
							novoProduto.porcentagemNaoProducao = novoProduto.calcularPorcentagemNaoProducao(novoProduto.producaoMedia, novoProduto.producaoMax);
							produtos.add(novoProduto);
							System.out.println("Produto Cadastrado com Sucesso !");
						} else {
							System.out.println("Quantidade Máxima de Produtos Cadastrados");
						}
					}
					case 5 -> {
						// Removido limparTerminal();
						System.out.println(imprimirCabecalho());
						System.out.println("Quantidade de Produtos : " + produtos.size());
						for (Produto produto : produtos){
							System.out.println(produto.formatarLinha());
						}
					}
					case 6 -> {
						ArrayList<Produto> produtosOrdenadosNaoProducao = new ArrayList<>(produtos);
						produtosOrdenadosNaoProducao.sort(
							Comparator.comparingDouble((Produto p) -> p.porcentagemNaoProducao).reversed()
						);
						System.out.println(imprimirCabecalho());
						for (int i = 0 ; i < 10 ; i++){
							Produto item = produtosOrdenadosNaoProducao.get(i);
							System.out.println(item.formatarLinha());
						}
					}
					case 7 -> {
						ArrayList<Produto> produtosOrdenadosAlfabetica = new ArrayList<>(produtos);
						produtosOrdenadosAlfabetica.sort(Comparator.comparing(produto -> produto.nome.toLowerCase()));

						// Imprimir a Lista
						System.out.println("\nQuantidade de Produtos : " + produtosOrdenadosAlfabetica.size());
						System.out.println(imprimirCabecalho());
						for (Produto item : produtosOrdenadosAlfabetica){
							System.out.println(item.formatarLinha());
						}
					}
					default -> System.out.println("Opcao invalida");
				}
			} while (opcao != 0);
		}
	}

	
	
}
