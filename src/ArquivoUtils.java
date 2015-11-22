import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class ArquivoUtils {

	private BufferedReader lerArq = null;

	public ArquivoUtils() {

	}

	public ArquivoUtils(boolean ler) {
		try {
			FileReader arq = new FileReader("processos.dat");
			lerArq = new BufferedReader(arq);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<PCB> ler_processos() {
		String aux = "";
		LinkedList<PCB> retorno = new LinkedList<PCB>();
		try {
			for (int i = 0; i < 10; i++) {
				int picos[] = new int[50];
				aux = lerArq.readLine();
				if(aux == null){
					break;
				}
				String num[] = aux.split(" ");
				System.out.println("PID " + num[0]);
				System.out.println("Tempo de chegada " + num[1]);
				System.out.println("Quantidade de picos de CPU " + num[2]);
				for (int j = 0; j < (num.length - 3); j++) {
					System.out.println("Sequência de picos de CPU "
							+ num[j + 3]);
					picos[j] = Integer.parseInt(num[j + 3]);
				}
				PCB pcb = new PCB();
				pcb.setId_processo(Integer.parseInt(num[0]));
				pcb.setTempoChegada(Integer.parseInt(num[1]));
				pcb.setEstado(TiposEstados.PRONTO);
				pcb.setNumeroPicosCPU(Integer.parseInt(num[2]));
				pcb.setPicosCPU(picos);
				pcb.setPicoCPUAtual(0);
				retorno.add(pcb);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("fim do arquivo!");
		}
		return retorno;
	}

	public PrintWriter escreveLog(String nomeArquivo) {
		PrintWriter arq = null;
		try {
			FileWriter escreve = new FileWriter(nomeArquivo, true);
			arq = new PrintWriter(new BufferedWriter(escreve));
		} catch (IOException e) {
			System.out.println("Arquivo não existente!");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arq;

	}
}
