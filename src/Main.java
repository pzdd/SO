import java.util.LinkedList;

public class Main {
	final public int PRONTO = 1;
	final public int EXECUTANDO = 2;
	final public int ESPERA = 3;

	LinkedList<PCB> filaProntos = new LinkedList<PCB>();
	LinkedList<PCB> filaEspera = new LinkedList<PCB>();
	int nProcessoFinalizados = 0;
	int tempoMedioProcessamento = 0;
	int tempoMedioEspera = 0;
	int tempoMedioTurnaround = 0;
	int tempoTotalUtilizacaoCPU = 0;
	int tempoCPUOcupada = 0;
	int taxaPercentualOcupacaoCPU = 0;
	int tempoCPUOciosa = 0;
	int taxaPercentualOciosidadeCPU = 0;
	

	PCB obj = null;
	int contador = 0;
	ArquivoUtils logs = new ArquivoUtils();

	public static void main(String[] args) throws InterruptedException {
		
		ArquivoUtils arq = new ArquivoUtils();
		// ler 10 processos
		LinkedList<PCB> saida = arq.ler_processos();
		Main m = new Main();
		m.preencheFilaProntos(saida);
		TiposEstados.exibePCB(m.filaProntos.get(0));
		while(!m.filaProntos.isEmpty()){
			new Thread(m.threadExec).start();
			Thread.sleep(100);
		}
		m.ArquivoLOG3();
		
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
				
				while (contadorInterno < obj.getPicosCPU()[obj.getPicoCPUAtual()]) {
					contador++;
					contadorInterno++;
					if(contador % 200 == 0){
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
				System.out.println("Contador "+ obj.getContador());
				
		}

	};

	Thread threadEsperaES = new Thread() {
		//processo de ES sempre irão obedecer o padrão FIFO
		public void run() {
			synchronized (this) {
				PCB obj = filaEspera.get(0);
				for (int i = 0; i < 10; i++) {
					contador++;
					if(contador % 200 == 0){
						ArquivoLOG2();
					}
				}
				
				int pico = obj.getPicoCPUAtual();
				obj.setPicoCPUAtual(++pico);
				
				
				
				obj.setFinalizacaoES(contador);
				System.out.println("Tempo de finalizacao do atual E/S " + obj.getFinalizacaoES());
				
				if(obj.getPicoCPUAtual() >= obj.getNumeroPicosCPU()){
					obj.setEstado(TiposEstados.FINALIZADO);
					nProcessoFinalizados++;
					ArquivoLOG(obj);
					filaEspera.remove(0);
				}else{
					filaProntos.add(obj);
					filaEspera.remove(0);
				}
				
				this.notify();
			}
		}
	};
	
	public void ArquivoLOG(PCB pcb){
		/* Primeiro LOG */
		System.out.println("PID " + pcb.getId_processo());
		System.out.println("Tempo de chegada " + pcb.getTempoChegada());
		System.out.println("Tempo de finalizacao " + pcb.getFinalizacaoES());
		int tempoProcessamento = pcb.getFinalizacaoES() - pcb.getTempoChegada();
		tempoMedioProcessamento += tempoProcessamento;
		System.out.println("Tempo de processamento " + tempoProcessamento);
		//tempo de espera
		//tempo de turnaround
	}
	public void ArquivoLOG2(){
		/* Segundo LOG */
		System.out.println("Número de processos na fila de prontos " + filaProntos.size());
		System.out.println("Número de processos na fila de bloqueados " + filaEspera.size());
		System.out.println("Número de processos finalizados " + nProcessoFinalizados);
	}
	public void ArquivoLOG3(){
		String algoritmo = "FCFS";
		System.out.println("Algoritmo " + algoritmo);
		System.out.println("Valor atual do ciclo de CPU " + contador);
		tempoMedioProcessamento = tempoMedioProcessamento/2;
		System.out.println("Tempo Medio de processamento" + tempoMedioProcessamento);
		
	}



}
