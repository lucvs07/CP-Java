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
	public static final int N = 30;

	// Função calculaMediaPorcentagemProducao() que calcula a media de producao e o percentual de nao producao
	public static void calculaMediaPorcentagemProducao(int[] prodMes1, int[] prodMes2, int[] prodMes3, int[] prodMaxima, double[] producaoMedia, double[] porcentagemNaoProducao) {
		for (int i = 0; i < N; i++) {
			// Calcula a media de producao
			producaoMedia[i] = (prodMes1[i] + prodMes2[i] + prodMes3[i]) / 3.0;
			// Calcula o percentual de nao producao
			porcentagemNaoProducao[i] = ((prodMaxima[i] - producaoMedia[i]) / prodMaxima[i]) * 100;
		}
	}

	// Função imprimirDados() que imprime os dados dos vetores
	public static void imprimirDados(int[] codigos, String[] fabricas, int[] prodMaxima, int[] prodMes1, int[] prodMes2, int[] prodMes3, double[] producaoMedia, double[] porcentagemNaoProducao){
		System.out.printf("%-8s %-20s %-15s %-12s %-12s %-12s %-15s %-15s\n", 
        "Codigo", "Fabrica", "Prod. Maxima", "Prod. Mes 1", "Prod. Mes 2", "Prod. Mes 3", "Media Producao", "% Nao Producao");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
		for (int j = 0; j < N; j++) {
			System.out.printf("%-8d %-20s %-15d %-12d %-12d %-12d %-15.2f %-15.2f\n", 
            codigos[j], fabricas[j], prodMaxima[j], prodMes1[j], prodMes2[j], prodMes3[j], producaoMedia[j], porcentagemNaoProducao[j]);
		}
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	// Funçào imprimirCabecalho() que imprime o cabecalho da tabela
	public static void imprimirCabecalho() {
		System.out.printf("%-8s %-20s %-15s %-12s %-12s %-12s %-15s %-15s\n", 
        "Codigo", "Fabrica", "Prod. Maxima", "Prod. Mes 1", "Prod. Mes 2", "Prod. Mes 3", "Media Producao", "% Nao Producao");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	// Função para imprimir os dados de uma fabrica especifica
	public static void imprimirDadosUnico(int pos ,int[] codigos, String[] fabricas, int[] prodMaxima, int[] prodMes1, int[] prodMes2, int[] prodMes3, double[] producaoMedia, double[] porcentagemNaoProducao){
		System.out.printf("%-8d %-20s %-15d %-12d %-12d %-12d %-15.2f %-15.2f\n", 
        codigos[pos], fabricas[pos], prodMaxima[pos], prodMes1[pos], prodMes2[pos], prodMes3[pos], producaoMedia[pos], porcentagemNaoProducao[pos]);
	}

	// Função fabricaMaiorProducao() que procura a fabrica com maior capacidade de producao de um produto
	public static int fabricaMaiorProducao(int codInput ,int[] codigos, String[] fabricas, int[] prodMaxima, int[] prodMes1, int[] prodMes2, int[] prodMes3, double[] producaoMedia, double[] porcentagemNaoProducao){
		int pos = -1;
		int maior = 0;
		for (int i = 0; i < N; i++) {
			if (codInput == codigos[i]) {
				if (prodMaxima[i] > maior) {
					maior = prodMaxima[i];
					pos = i;
				}
			}
		}
		return pos;
	}
	// Busca a fabrica e retorna um vetor com os indices das fabricas que fabricam o produto e o número de elementos do vetor
	public static int[] buscaPorFabrica(String fabInput, String[] fabricas) {
		int[] indices = new int[1];
		int cont = 0;
		for (int i = 0; i < N; i++) {
			if (fabricas[i].trim().equalsIgnoreCase(fabInput.trim())) {
				// Verifica se o vetor precisa ser expandido
				if (cont == indices.length) {
					int[] novoIndices = new int[indices.length + 1];
					System.arraycopy(indices, 0, novoIndices, 0, indices.length);
					indices = novoIndices;
				}
				indices[cont] = i;
				cont++;
			}
		}
		return indices;
	}
	// Função selecionaProdAbaixo() gera um vetor com os índices de vetor onde na tabela se encontra o percentual de produção abaixo da capacidade máxima da fábrica
	public static int[] selecionaProdAbaixo(double porcLimiteInput, double[] porcentagemNaoProducao){
		int[] indices = new int[1];
		int cont = 0;
		for (int i = 0; i < N; i++) {
			if (porcentagemNaoProducao[i] > porcLimiteInput) {
				// Verifica se o vetor precisa ser expandido
				if (cont == indices.length) {
					// Cria um novo vetor com o dobro do tamanho atual
					int[] novoIndices = new int[indices.length + 1];
					System.arraycopy(indices, 0, novoIndices, 0, indices.length);
					indices = novoIndices;
				}
				indices[cont] = i;
				cont++;
			}
		}
		return indices;
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
		// Vetores para armazenar os dados do arquivo CSV
		int[] codigos = new int[N];
		String[] fabricas = new String[N];
		int[] prodMaxima = new int[N];
		int[] prodMes1 = new int[N];
		int[] prodMes2 = new int[N];
		int[] prodMes3 = new int[N];

		try {
			// Cria um objeto File com o caminho do arquivo
			File arquivo = new File(caminhoDoArquivo);

			// Cria um objeto da classe Scanner para ler o arquivo
			Scanner leArq = new Scanner(arquivo);
			int i = 0;
			while (leArq.hasNextLine() && i < N) {
				// Le uma linha do arquivo
				String linha = leArq.nextLine();
				//separa em Strings cada parte separada por ";"
				String[] partes = linha.split(";");
				// atribui a cada vetor declarado anteriormente as partes separadas 
				// fazendo conversao de String para int quando necessario
				codigos[i] = Integer.parseInt(partes[0]);
				fabricas[i] = partes[1];
				prodMaxima[i] = Integer.parseInt(partes[2]);
				prodMes1[i] = Integer.parseInt(partes[3]);
				prodMes2[i] = Integer.parseInt(partes[4]);
				prodMes3[i] = Integer.parseInt(partes[5]);
				i++;
			}
			// Fechar o objeto da classe Scanner le
			leArq.close();
		} catch (FileNotFoundException e) {
			// Caso o arquivo n�o seja encontrado
			System.out.println("Arquivo nao encontrado: " + e.getMessage());
		}
		
		try (Scanner le = new Scanner(System.in)) {
			/*
			 * Declara variaveis e vetores necessarios para item 2 do enunciado.
			 * Chamar a funcao calculaMediaPorcentagemProducao() e apresentar os vetores por ela gerados
			 */
			double[] producaoMedia = new double[N];
			double[] porcentagemNaoProducao = new double[N];
			// Chama a funcao que calcula a media de producao e o percentual de nao producao
			calculaMediaPorcentagemProducao(prodMes1, prodMes2, prodMes3, prodMaxima, producaoMedia, porcentagemNaoProducao);
			// Apresenta os vetores gerados pela funcao
			imprimirDados(codigos, fabricas, prodMaxima, prodMes1, prodMes2, prodMes3, producaoMedia, porcentagemNaoProducao);

			
			
			int opcao = 0;
			
			do {
				System.out.println("0 - Encerrar");
				System.out.println("1 - Procura fabrica com maior capacidade de producao de um produto");
				System.out.println("2 - Procura produtos fabricados em uma especifica unidade");
				System.out.println("3 - Seleciona e apresenta produtos com percentual de nao producao acima de um limite");
				System.out.print("Opcao: ");
				opcao = le.nextInt();
				le.nextLine();
				switch (opcao) {
				case 0:
					System.out.println("Encerrado o programa.");
					break;
				case 1:
					System.out.print("Qual o codigo do produto a ser pesquisado? ");
					int codProcurado = le.nextInt();
					// Chama a funcao que procura a fabrica com maior capacidade de producao de um produto
					int pos = fabricaMaiorProducao(codProcurado, codigos, fabricas, prodMaxima, prodMes1, prodMes2, prodMes3, producaoMedia, porcentagemNaoProducao);
					if (pos != -1) {
						System.out.println("Fabrica com maior capacidade de producao do produto " + codProcurado + ": ");
						// Chama a funcao que imprime os dados da fabrica
						imprimirCabecalho();
						imprimirDadosUnico(pos, codigos, fabricas, prodMaxima, prodMes1, prodMes2, prodMes3, producaoMedia, porcentagemNaoProducao);
					} else {
						System.out.println("Produto nao encontrado.");
					}
					break;
				case 2:
					System.out.print("Fabrica para pesquisar produtos:");
					// Le a fabrica a ser pesquisada
					String fab = le.nextLine();
					System.out.println(fab);
					/*
					 * Implementacao item 4
					 */
					// Chama a funcao que busca os produtos fabricados em uma especifica unidade
					int[] indices = buscaPorFabrica(fab, fabricas);
					int numeroDeElementos = indices.length;
					if (numeroDeElementos > 0) {
						System.out.println("Produtos fabricados na unidade " + fab + ": ");
						// Chama a funcao que imprime os dados da fabrica
						imprimirCabecalho();
						for (int j = 0; j < numeroDeElementos; j++) {
							imprimirDadosUnico(indices[j], codigos, fabricas, prodMaxima, prodMes1, prodMes2, prodMes3, producaoMedia, porcentagemNaoProducao);
						}
						System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
					} else {
						System.out.println("Fabrica nao encontrada.");
					}
					break;
				case 3:
					System.out.print("Qual o valor limite de percentual nao produzido que se deseja pesquisar: ");
					double porcLimite = le.nextDouble();
					/*
					 * Implementacao item 5
					 */
					// Chama a funcao que retorna os indices dos produtos com percentual de nao producao acima de um limite
					int[] indicesProdAbaixo = selecionaProdAbaixo(porcLimite, porcentagemNaoProducao);
					int numeroDeElementosProdAbaixo = indicesProdAbaixo.length;
					if (numeroDeElementosProdAbaixo > 0) {
						System.out.println("Produtos com percentual de nao producao acima de " + porcLimite + "% 2: ");
						// Chama a funcao que imprime os dados da fabrica
						imprimirCabecalho();
						for (int j = 0; j < numeroDeElementosProdAbaixo; j++) {
							imprimirDadosUnico(indicesProdAbaixo[j], codigos, fabricas, prodMaxima, prodMes1, prodMes2, prodMes3, producaoMedia, porcentagemNaoProducao);
						}
						System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
					} else {
						System.out.println("Nenhum produto encontrado com percentual de nao producao acima de " + porcLimite + ".");
					}
					break;
				default:
					System.out.println("Opcao invalida");
				}
			} while (opcao != 0);
		}
	}

	
	
}
