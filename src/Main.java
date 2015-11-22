import java.io.PrintWriter;
import java.util.LinkedList;

public class Main {
	final public int PRONTO = 1;
	final public int EXECUTANDO = 2;
	final public int ESPERA = 3;

	final public int totalProcessos = 10;

	LinkedList<PCB> filaProntos = new LinkedList<PCB>();
	LinkedList<PCB> filaEspera = new LinkedList<PCB>();

	int nProcessoFinalizados = 0;
	int tempoMedioProcessamento = 0;
	int tempoMedioEspera = 0;
	int tempoMedioTurnaround = 0;
	double tempoTotalUtilizacaoCPU = 0;
	double tempoCPUOcupada = 0;
	int taxaPercentualOcupacaoCPU = 0;
	double tempoCPUOciosa = 0;
	int taxaPercentualOciosidadeCPU = 0;
	int totalPicos = 0;

	PCB obj = null;
	int contador = 0;
	ArquivoUtils logs = new ArquivoUtils();

	public static void main(String[] args) throws InterruptedException {

		ArquivoUtils arq = new ArquivoUtils(true);
		Main m = new Main();
		// ler 10 processos
		while(true) {
			LinkedList<PCB> saida = arq.ler_processos();
			m.preencheFilaProntos(saida);
			TiposEstados.exibePCB(m.filaProntos.get(0));
			while (!m.filaProntos.isEmpty()) {
				new Thread(m.threadExec).start();
				Thread.sleep(100);
			}
			m.ArquivoLOG3();
		}

	}

	public void preencheFilaProntos(LinkedList<PCB> saida) {
		for (int i = 0; i < saida.size(); i++) {
			filaProntos.add(saida.get(i));
		}
	}

	Thread threadExec = new Thread() {
		public void run() {
			obj = filaProntos.get(0);
			obj.setEstado(TiposEstados.EXECUTANDO);
			int contadorInterno = 0;
			if (obj.getPicoCPUAtual() == 0) {
				obj.setTempoInicializacao(contador);
			}
			while (contadorInterno < obj.getPicosCPU()[obj.getPicoCPUAtual()]) {
				contador++;
				contadorInterno++;
				if (contador % 200 == 0) {
					ArquivoLOG2();
				}
			}

			obj.setEstado(TiposEstados.ESPERA);
			filaEspera.add(obj);
			filaProntos.remove(obj);

			new Thread(threadEsperaES).start();
			synchronized (threadEsperaES) {
				try {
					threadEsperaES.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Pico de CPU " + contadorInterno);
			obj.setContador(contador);
			System.out.println("Contador " + obj.getContador());

		}

	};

	Thread threadEsperaES = new Thread() {
		// processo de ES sempre irão obedecer o padrão FIFO
		public void run() {
			synchronized (this) {
				PCB obj = filaEspera.get(0);
				for (int i = 0; i < 10; i++) {
					contador++;
					if (contador % 200 == 0) {
						ArquivoLOG2();
					}
				}

				int pico = obj.getPicoCPUAtual();
				obj.setPicoCPUAtual(++pico);

				obj.setFinalizacaoES(contador);
				System.out.println("Tempo de finalizacao do atual E/S "
						+ obj.getFinalizacaoES());

				if (obj.getPicoCPUAtual() >= obj.getNumeroPicosCPU()) {
					obj.setEstado(TiposEstados.FINALIZADO);
					nProcessoFinalizados++;
					ArquivoLOG(obj);
					filaEspera.remove(0);
				} else {
					filaProntos.add(obj);
					filaEspera.remove(0);
				}

				this.notify();
			}
		}
	};
	int i = 0;

	public void ArquivoLOG(PCB pcb) {
		/* Primeiro LOG */
		ArquivoUtils arq = new ArquivoUtils();
		PrintWriter write = arq.escreveLog("log1-Geraldo-FIFO.txt");
		System.out.println("PID " + pcb.getId_processo());
		write.println("PID " + pcb.getId_processo());
		System.out.println("Tempo de chegada " + pcb.getTempoChegada());
		write.println("Tempo de chegada " + pcb.getTempoChegada());
		System.out.println("Tempo de finalizacao " + pcb.getFinalizacaoES());
		write.println("Tempo de finalizacao " + pcb.getFinalizacaoES());
		int tempoProcessamento = pcb.getFinalizacaoES()
				- pcb.getTempoInicializacao();
		tempoMedioProcessamento += tempoProcessamento;
		tempoTotalUtilizacaoCPU += pcb.getFinalizacaoES()
				- pcb.getTempoInicializacao();
		System.out.println("Tempo de processamento " + tempoProcessamento);
		write.println("Tempo de processamento " + tempoProcessamento);
		totalPicos += pcb.getNumeroPicosCPU();
		// tempo de espera
		int tempoEspera = pcb.getTempoInicializacao() - pcb.getTempoChegada();
		tempoMedioEspera += tempoEspera;
		System.out.println("Tempo de espera " + tempoEspera);
		write.println("Tempo de espera " + tempoEspera);

		write.close();
		// i++;
		// tempo de turnaround
	}

	public void ArquivoLOG2() {
		/* Segundo LOG */
		ArquivoUtils arq = new ArquivoUtils();
		PrintWriter write = arq.escreveLog("log2-Geraldo-FIFO.txt");
		System.out.println("Número de processos na fila de prontos "
				+ filaProntos.size());
		write.println("Número de processos na fila de prontos "
				+ filaProntos.size());
		System.out.println("Número de processos na fila de bloqueados "
				+ filaEspera.size());
		write.println("Número de processos na fila de bloqueados "
				+ filaEspera.size());
		System.out.println("Número de processos finalizados "
				+ nProcessoFinalizados);
		write.println("Número de processos finalizados " + nProcessoFinalizados);
		write.println("");

		write.close();
	}

	public void ArquivoLOG3() {
		/* Terceito LOG */
		ArquivoUtils arq = new ArquivoUtils();
		PrintWriter write = arq.escreveLog("log3-Geraldo-FIFO.txt");
		String algoritmo = "FCFS";
		System.out.println("Algoritmo " + algoritmo);
		write.println("Algoritmo " + algoritmo);
		System.out.println("Valor atual do ciclo de CPU " + contador);
		write.println("Valor atual do ciclo de CPU " + contador);
		tempoMedioProcessamento = tempoMedioProcessamento / totalProcessos;
		System.out.println("Tempo Medio de processamento "
				+ tempoMedioProcessamento);
		write.println("Tempo Medio de processamento " + tempoMedioProcessamento);
		System.out.println("Tempo total de utilizacao da CPU "
				+ tempoTotalUtilizacaoCPU);
		write.println("Tempo total de utilizacao da CPU "
				+ tempoTotalUtilizacaoCPU);
		System.out.println("Tempo medio de espera " + tempoMedioEspera
				/ totalProcessos);
		write.println("Tempo medio de espera " + tempoMedioEspera
				/ totalProcessos);
		// tempo medio de turnaround
		// tempo que a cpu permaneceu ocupada
		tempoCPUOciosa = totalPicos * 10;
		tempoCPUOcupada = tempoTotalUtilizacaoCPU - tempoCPUOciosa;
		System.out.println("Tempo que a cpu permaneceu ocupada "
				+ tempoCPUOcupada);
		write.println("Tempo que a cpu permaneceu ocupada " + tempoCPUOcupada);
		System.out.println("Taxa percentual de ocupacao da CPU "
				+ (tempoCPUOcupada / tempoTotalUtilizacaoCPU) * 100 + "%");
		write.println("Taxa percentual de ocupacao da CPU " + tempoCPUOcupada
				/ tempoTotalUtilizacaoCPU * 100 + "%");
		System.out.println("Tempo que a cpu permaneceu ociosa "
				+ tempoCPUOciosa);
		write.println("Tempo que a cpu permaneceu ociosa " + tempoCPUOciosa);
		System.out.println("Taxa percentual da ociosidade da CPU "
				+ (tempoCPUOciosa / tempoTotalUtilizacaoCPU) * 100 + "%");
		write.println("Taxa percentual da ociosidade da CPU " + (tempoCPUOciosa
				/ tempoTotalUtilizacaoCPU) * 100 + "%");
		write.println(" ");

		write.close();

	}

}
