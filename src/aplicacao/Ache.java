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
	public static int fabricaMaiorProducao(int codInput , Produto[] produtos){
		int pos = -1;
		int maior = 0;
		for (int i = 0; i < N; i++) {
			if (codInput == produtos[i].codigo) {
				if (produtos[i].producaoMax > maior) {
					maior = produtos[i].producaoMax;
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
	public static ResultadoBusca buscaPorFabrica(String fabInput, Produto[] produtos) {
		int[] indices = new int[1];
		int cont = 0;
		for (int i = 0; i < N; i++) {
			String nome = produtos[i].nome.trim();
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
	public static ResultadoBusca selecionaProdAbaixo(double porcLimiteInput, Produto[] produtos){
		int[] indices = new int[1];
		int cont = 0;
		for (int i = 0; i < N; i++) {
			if (produtos[i].porcentagemNaoProducao > porcLimiteInput) {
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
		Produto produtos[] = new Produto[N];

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
							produtos[i] = produto;
                            i++;
                        }
                        // Fechar o objeto da classe Scanner le
                    }
		} catch (FileNotFoundException e) {
			// Caso o arquivo n�o seja encontrado
			System.out.println("Arquivo nao encontrado: " + e.getMessage());
		}
		// Imprimir todos os dados
		System.out.println(imprimirCabecalho());
		for (Produto produto : produtos){
			System.out.println(produto.formatarLinha());
		}
		try (Scanner le = new Scanner(System.in)) {
			
			int opcao;
			
			do {
				System.out.println("0 - Encerrar");
				System.out.println("1 - Procura fabrica com maior capacidade de producao de um produto");
				System.out.println("2 - Procura produtos fabricados em uma especifica unidade");
				System.out.println("3 - Seleciona e apresenta produtos com percentual de nao producao acima de um limite");
				System.out.print("Opcao: ");
				opcao = le.nextInt();
				le.nextLine();
				switch (opcao) {
				case 0 -> System.out.println("Encerrado o programa.");
				case 1 -> {
                                    System.out.print("Qual o codigo do produto a ser pesquisado? ");
                                    int codProcurado = le.nextInt();
                                    // Chama a funcao que procura a fabrica com maior capacidade de producao de um produto
                                    int pos = fabricaMaiorProducao(codProcurado, produtos);
                                    if (pos != -1) {
                                        System.out.println("Fabrica com maior capacidade de producao do produto " + codProcurado + ": ");
                                        // Chama a funcao que imprime os dados da fabrica
										System.out.println(imprimirCabecalho());
										System.out.println(produtos[pos].formatarLinha());
                                    } else {
                                        System.out.println("Produto nao encontrado.");
                                    }
                                }
				case 2 -> {
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
							System.out.println(produtos[indice].formatarLinha());
						}
						System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
					} else {
						System.out.println("Fabrica nao encontrada.");
					}
				}
				case 3 -> {
					System.out.print("Qual o valor limite de percentual nao produzido que se deseja pesquisar: ");
					double porcLimite = le.nextDouble();
					ResultadoBusca resultado = selecionaProdAbaixo(porcLimite, produtos);
					if (resultado.quantidade > 0) {
						System.out.println("\nQuantidade de Produtos Encontrados : " + resultado.quantidade);
						System.out.println("Produtos com percentual de nao producao acima de " + porcLimite + "% : ");
						System.out.println(imprimirCabecalho());
						for (int j = 0; j < resultado.quantidade; j++) {
							int indice = resultado.indices[j];
							System.out.println(produtos[indice].formatarLinha());
						}
						System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
					} else {
						System.out.println("Nenhum produto encontrado com percentual de nao producao acima de " + porcLimite + ".");
					}
				}
				default -> System.out.println("Opcao invalida");
				}
			} while (opcao != 0);
		}
	}

	
	
}
