import java.util.LinkedList;

public class Main {
	final public int PRONTO = 1;
	final public int EXECUTANDO = 2;
	final public int ESPERA = 3;

	LinkedList<PCB> prontos = new LinkedList<PCB>();
	LinkedList<PCB> espera = new LinkedList<PCB>();

	PCB aux = null;
	int contador = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArquivoUtils arq = new ArquivoUtils();
		// ler 10 processos
		LinkedList<PCB> saida = arq.ler_processos();
		Main m = new Main();
		m.preenche_fila_prontos(saida);
		TiposEstados.exibe_PCB(m.prontos.get(0));
		m.exe.start();
	}

		
	public void preenche_fila_prontos(LinkedList<PCB> saida) {
		for (int i = 0; i < saida.size(); i++) {
			prontos.add(saida.get(i));
		}
	}
	
	Thread exe = new Thread() {
		public void run() {
			while (!prontos.isEmpty()) {
				aux = prontos.get(0);
				while (aux.getNumeroPicosCPU() > aux.getPicoCPUAtual()) {
					aux.setEstado(TiposEstados.EXECUTANDO);
					if (aux.getPicosCPU()[aux.getPicoCPUAtual()] > 10) {
						int contador_interno = 0;
						while (contador_interno < (aux.getPicosCPU()[aux
								.getPicoCPUAtual()] - 10)) {
							contador++;
							contador_interno++;
						}
						aux.setEstado(TiposEstados.ESPERA);
						espera.add(aux);
						//prontos.remove(aux);
						if (!espera.isEmpty()) {
							new Thread(esperar).start();
							synchronized (esperar) {
								try {
									esperar.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						System.out.println("contador interno "
								+ contador_interno);

					} else {
						int contador_interno = 0;
						while (contador_interno < (aux.getPicosCPU()[aux
								.getPicoCPUAtual()])) {
							contador++;
							contador_interno++;
						}
						System.out.println("contador interno "
								+ contador_interno);
					}
					int pico = aux.getPicoCPUAtual();
					aux.setPicoCPUAtual(++pico);
					aux.setContador(contador);
					prontos.remove(aux);
					System.out.println("contador " + aux.getContador());
				}
			}
		}

	};

	Thread esperar = new Thread() {
		public void run() {
			synchronized (this) {

				for (int i = 0; i < 10; i++) {
					contador++;
				}
				espera.remove(aux);
				this.notifyAll();
			}
		}
	};

}
