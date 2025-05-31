package aplicacao;

import java.io.File;
import java.io.FileNotFoundException;
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
		public String formatarLinha() {
			return String.format("%-8d %-20s %-15d %-12d %-12d %-12d %-15.2f %-15.2f\n",
					codigo, nome, producaoMax, producaoMes1, producaoMes2, producaoMes3, producaoMedia,
					porcentagemNaoProducao);
		}

		public double calcularMedia(int producaoMes1, int producaoMes2, int producaoMes3) {
			return (producaoMes1 + producaoMes2 + producaoMes3) / 3.0;
		}

		public double calcularPorcentagemNaoProducao(double producaoMedia, int producaoMax) {
			return ((producaoMax - producaoMedia) / producaoMax) * 100;
		}
	}

	public static final int N = 100;

	// Método para Imprimir o Cabeçalho
	public static String imprimirCabecalho() {
		return String.format("\n%-8s %-20s %-15s %-12s %-12s %-12s %-15s %-15s\n",
				"Codigo", "Fabrica", "Prod. Maxima", "Prod. Mes 1", "Prod. Mes 2", "Prod. Mes 3", "Media Producao",
				"% Nao Producao");
	}

	// Função fabricaMaiorProducao() que procura a fabrica com maior capacidade de
	// producao de um produto
	public static int fabricaMaiorProducao(int codInput, Produto[] produtos, int n) {
		int pos = -1;
		int maior = 0;
		for (int i = 0; i < n; i++) {
			if (codInput == produtos[i].codigo) {
				if (produtos[i].producaoMax > maior) {
					maior = produtos[i].producaoMax;
					pos = i;
				}
			}
		}
		return pos;
	}

	// Busca a fabrica e retorna um objeto com os indices das fabricas que fabricam
	// o produto e o número de elementos encontrados
	public static int buscaPorFabrica(String fabInput, Produto[] produtos, int[] indices) {
		int cont = 0;
		for (int i = 0; i < N; i++) {
			String nome = produtos[i].nome.trim();
			if (nome.equalsIgnoreCase(fabInput.trim())) {
				indices[cont] = i;
				cont++;
			}
		}
		return cont;
	}

	// Função selecionaProdAbaixo() gera um objeto com os índices onde o percentual
	// de produção está abaixo do limite
	public static int selecionaProdAbaixo(double porcLimiteInput, Produto[] produtos, int[] indices) {
		int cont = 0;
		for (int i = 0; i < N; i++) {
			if (produtos[i].porcentagemNaoProducao > porcLimiteInput) {
				indices[cont] = i;
				cont++;
			}
		}
		return cont;
	}

	public static void bubbleSortOrdemAlfabetica(int n, Produto[] produtos) {
		boolean troca= true; 
		for (int i=0;(i<n-1) && (troca); i++){
			Produto aux;
			troca= false; 
			for (int j=0;j<n-i-1;j++){
				if (produtos[j].nome.compareToIgnoreCase(produtos[j+1].nome) > 0) {
					aux = produtos[j];
					produtos[j] = produtos[j+1];
					produtos[j+1] = aux;
					troca = true;
				}
			}
		}
	}

	public static void bubbleSortPorcentagemNaoProducaoDesc(int n, Produto[] produtos) {
		boolean troca = true;
		for (int i = 0; (i < n - 1) && troca; i++) {
			Produto aux;
			troca = false;
			for (int j = 0; j < n - i - 1; j++) {
				if (produtos[j].porcentagemNaoProducao < produtos[j + 1].porcentagemNaoProducao) {
					aux = produtos[j];
					produtos[j] = produtos[j + 1];
					produtos[j + 1] = aux;
					troca = true;
				}
			}
		}
	}

	public static int buscaBinariaMaiorProducao(int inputCodigo, int n, Produto[] produtos) {
		// Busca binária para encontrar o primeiro produto com o código informado
		int low = 0;
		int high = n - 1;
		int posicao = -1;
		while (low <= high) {
			int mid = (low + high) / 2;
			if (produtos[mid].codigo == inputCodigo) {
				posicao = mid;
				break;
			} else if (inputCodigo < produtos[mid].codigo) {
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		if (posicao == -1) {
			return -1; // Não encontrado
		}
		// Encontrou um produto com o código. Agora procura o intervalo de produtos com esse código.
		int inicio = posicao;
		int fim = posicao;
		// Volta até o primeiro com o mesmo código
		while (inicio > 0 && produtos[inicio - 1].codigo == inputCodigo) {
			inicio--;
		}
		// Avança até o último com o mesmo código
		while (fim < n - 1 && produtos[fim + 1].codigo == inputCodigo) {
			fim++;
		}
		// Procura o de maior producaoMax nesse intervalo
		int maiorProducaoMax = produtos[inicio].producaoMax;
		int indiceMaior = inicio;
		for (int i = inicio + 1; i <= fim; i++) {
			if (produtos[i].producaoMax > maiorProducaoMax) {
				maiorProducaoMax = produtos[i].producaoMax;
				indiceMaior = i;
			}
		}
		return indiceMaior;
	}

	public static void main(String[] args) {
		// Obtem caminho da pasta workspace da IDE
		String diretorio = System.getProperty("user.dir");
		// Concatena caminho ate a pasta workspace da IDE com arquivo disponilizado no
		// projeto
		String caminhoDoArquivo = diretorio + "/src/arquivos/planilhaProducao.csv";

		/*
		 * Declarar vetores usado no programa
		 * As linhas com erros no codigo precisam desses vetores
		 */
		// Vetor produtos para armazenar os dados do arquivo CSV
		Produto produtos[] = new Produto[N];
		int numeroDeProdutosCadastrados = 0;
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
					// separa em Strings cada parte separada por ";"
					String[] partes = linha.split(";");
					// atribui a cada vetor declarado anteriormente as partes separadas
					// fazendo conversao de String para int quando necessario
					produto.codigo = Integer.parseInt(partes[0]);
					produto.nome = partes[1];
					produto.producaoMax = Integer.parseInt(partes[2]);
					produto.producaoMes1 = Integer.parseInt(partes[3]);
					produto.producaoMes2 = Integer.parseInt(partes[4]);
					produto.producaoMes3 = Integer.parseInt(partes[5]);
					produto.producaoMedia = produto.calcularMedia(produto.producaoMes1, produto.producaoMes2,
							produto.producaoMes3);
					produto.porcentagemNaoProducao = produto.calcularPorcentagemNaoProducao(produto.producaoMedia,
							produto.producaoMax);
					// armazenar produto
					produtos[i] = produto;
					i++;
				}
				// Fechar o objeto da classe Scanner le
				numeroDeProdutosCadastrados = i;
			}
		} catch (FileNotFoundException e) {
			// Caso o arquivo n�o seja encontrado
			System.out.println("Arquivo nao encontrado: " + e.getMessage());
		}

		// Imprimir todos os dados
		System.out.println(imprimirCabecalho());
		for (int i = 0; i < numeroDeProdutosCadastrados; i++) {
			System.out.println(produtos[i].formatarLinha());
		}
		try (Scanner le = new Scanner(System.in)) {

			int opcao;

			do {
				System.out.println("0 - Encerrar");
				System.out.println("1 - Procura fabrica com maior capacidade de producao de um produto");
				System.out.println("2 - Procura produtos fabricados em uma especifica unidade");
				System.out.println("3 - Seleciona e apresenta produtos com percentual de nao producao acima de um limite");
				System.out.println("4 - Inserir Novo Produto");
				System.out.println("5 - Top 10 Piores Produtos com base na taxa de porcentagem nao produzida");
				System.out.println("6 - Classifica em Ordem Alfabética");
				System.out.print("Opcao: ");
				opcao = le.nextInt();
				le.nextLine();
				switch (opcao) {
					case 0 -> System.out.println("Encerrado o programa.");
					case 1 -> {
						System.out.print("Qual o codigo do produto a ser pesquisado? ");
						int codProcurado = le.nextInt();
						int posBin = buscaBinariaMaiorProducao(codProcurado, numeroDeProdutosCadastrados, produtos);
						if (posBin != -1){
							System.out.println("Fabrica com Maior Capacidade de Producao do Produto " + codProcurado);
							System.out.println(imprimirCabecalho());
							System.out.println(produtos[posBin].formatarLinha());
						} else {
							System.out.println("Produto nao encontrado");
						}
					}
					case 2 -> {
						System.out.print("Fabrica para pesquisar produtos:");
						String fab = le.nextLine();
						System.out.println(fab);
						int elementosEncontradosBuscaPorFabrica;
						int[] indiceDeElementosEncontradosBuscaPorFabrica = new int[N];
						elementosEncontradosBuscaPorFabrica = buscaPorFabrica(fab, produtos,
								indiceDeElementosEncontradosBuscaPorFabrica);
						if (elementosEncontradosBuscaPorFabrica > 0) {
							System.out.println(
									"\nQuantidade de Produtos Encontrados : " + elementosEncontradosBuscaPorFabrica);
							System.out.println("Produtos fabricados na unidade " + fab + ": ");
							System.out.println(imprimirCabecalho());
							for (int j = 0; j < elementosEncontradosBuscaPorFabrica; j++) {
								int indice = indiceDeElementosEncontradosBuscaPorFabrica[j];
								System.out.println(produtos[indice].formatarLinha());
							}
							System.out.println(
									"-------------------------------------------------------------------------------------------------------------------------------------------------");
						} else {
							System.out.println("Fabrica nao encontrada.");
						}
					}
					case 3 -> {
						System.out.print("Qual o valor limite de percentual nao produzido que se deseja pesquisar: ");
						double porcLimite = le.nextDouble();
						int elementosEncontradosSelecionaProdAbaixo;
						int[] indiceDeElementosEncontradosSelecionaProdAbaixo = new int[N];
						elementosEncontradosSelecionaProdAbaixo = selecionaProdAbaixo(porcLimite, produtos,
								indiceDeElementosEncontradosSelecionaProdAbaixo);
						if (elementosEncontradosSelecionaProdAbaixo > 0) {
							System.out.println("\nQuantidade de Produtos Encontrados : "
									+ elementosEncontradosSelecionaProdAbaixo);
							System.out
									.println("Produtos com percentual de nao producao acima de " + porcLimite + "% : ");
							System.out.println(imprimirCabecalho());
							for (int j = 0; j < elementosEncontradosSelecionaProdAbaixo; j++) {
								int indice = indiceDeElementosEncontradosSelecionaProdAbaixo[j];
								System.out.println(produtos[indice].formatarLinha());
							}
							System.out.println(
									"-------------------------------------------------------------------------------------------------------------------------------------------------");
						} else {
							System.out.println("Nenhum produto encontrado com percentual de nao producao acima de "
									+ porcLimite + ".");
						}
					}
					case 4 -> {
						numeroDeProdutosCadastrados = 100;
						if (numeroDeProdutosCadastrados < 100) {
							System.out.println("Digite o nome da fábrica : ");
							String novoNome = le.nextLine();
							System.out.println("Insira o código do Produto (0000): ");
							int novoCodigoProduto = le.nextInt();
							le.nextLine();
							System.out.println("Insira a Produção Max da Fábrica: ");
							int novoProducaoMax = le.nextInt();
							le.nextLine();
							System.out.println("Insira a Produção do Mes 1 da Fábrica: ");
							int novoProducaoMes1 = le.nextInt();
							le.nextLine();
							System.out.println("Insira a Produção do Mes 2 da Fábrica: ");
							int novoProducaoMes2 = le.nextInt();
							le.nextLine();
							System.out.println("Insira a Produção do Mes 3 da Fábrica: ");
							int novoProducaoMes3 = le.nextInt();
							le.nextLine();

							Produto novoProduto = new Produto();
							novoProduto.codigo = novoCodigoProduto;
							novoProduto.nome = novoNome;
							novoProduto.producaoMax = novoProducaoMax;
							novoProduto.producaoMes1 = novoProducaoMes1;
							novoProduto.producaoMes2 = novoProducaoMes2;
							novoProduto.producaoMes3 = novoProducaoMes3;
							novoProduto.producaoMedia = novoProduto.calcularMedia(novoProducaoMes1, novoProducaoMes2, novoProducaoMes3);
							novoProduto.porcentagemNaoProducao = novoProduto.calcularPorcentagemNaoProducao(novoProduto.producaoMedia,novoProducaoMax);
							produtos[numeroDeProdutosCadastrados] = novoProduto;
							numeroDeProdutosCadastrados++;
							bubbleSortOrdemAlfabetica(numeroDeProdutosCadastrados, produtos);
							System.out.println("Produto " + novoProduto.codigo + " cadastrado com sucesso");

						} else {
							System.out.println("Número máximo de produtos cadastrados foi atingido !");
						}
						break;
					}
					case 5 -> {
						bubbleSortPorcentagemNaoProducaoDesc(numeroDeProdutosCadastrados, produtos);
						System.out.println("\nTop 10 Piores Taxas de Nao Producao");
						System.out.println(imprimirCabecalho());
						for (int i = 0; i < 10; i++) {
							System.out.println(produtos[i].formatarLinha());
						}
						bubbleSortOrdemAlfabetica(numeroDeProdutosCadastrados, produtos);
					}
					case 6 -> {
						bubbleSortOrdemAlfabetica(numeroDeProdutosCadastrados, produtos);
						System.out.println("Em ordem Alfabética\n");
						System.out.println(imprimirCabecalho());
						for (int i = 0; i < numeroDeProdutosCadastrados; i++) {
							System.out.println(produtos[i].formatarLinha());
						}
						continue;
					}
					default -> System.out.println("Opcao invalida");
				}
			} while (opcao != 0);
		}
	}

}
