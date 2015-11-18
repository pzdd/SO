import java.util.LinkedList;

public class Main {
	final public int PRONTO = 1;
	final public int EXECUTANDO = 2;
	final public int ESPERA = 3;

	LinkedList<PCB> prontos = new LinkedList<PCB>();
	LinkedList<PCB> espera = new LinkedList<PCB>();

	PCB aux = null;
	int contador = 0;

	public static void main(String[] args) throws InterruptedException {
		
		ArquivoUtils arq = new ArquivoUtils();
		// ler 10 processos
		LinkedList<PCB> saida = arq.ler_processos();
		Main m = new Main();
		m.preenche_fila_prontos(saida);
		TiposEstados.exibe_PCB(m.prontos.get(0));
		while(!m.prontos.isEmpty()){
			new Thread(m.exe).start();
			Thread.sleep(100);
		}
	}

		
	public void preenche_fila_prontos(LinkedList<PCB> saida) {
		for (int i = 0; i < saida.size(); i++) {
			prontos.add(saida.get(i));
		}
	}
	
	Thread exe = new Thread() {
		public void run() {
				aux = prontos.get(0);
				aux.setEstado(TiposEstados.EXECUTANDO);
				int contador_interno = 0;
				while (contador_interno < aux.getPicosCPU()[aux.getPicoCPUAtual()]) {
					contador++;
					contador_interno++;
				}
				aux.setEstado(TiposEstados.ESPERA);
				espera.add(aux);
				prontos.remove(aux);
					new Thread(esperar).start();
							synchronized (esperar) {
								try {
									esperar.join();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						System.out.println("contador interno "
								+ contador_interno);
						System.out.println("Tempo de espera "+ contador);
		}

	};

	Thread esperar = new Thread() {
		public void run() {
			synchronized (this) {
				for (int i = 0; i < 10; i++) {
					contador++;
				}
				PCB obj = espera.get(0);
				//prontos.add(obj);
				int pico = obj.getPicoCPUAtual();
				obj.setPicoCPUAtual(++pico);
				if(obj.getPicoCPUAtual() >= obj.getNumeroPicosCPU()){
					obj.setEstado(TiposEstados.FINALIZADO);
					espera.remove(0);
				}else{
					prontos.add(obj);
					espera.remove(0);
				}
				this.notify();
			}
		}
	};

}
