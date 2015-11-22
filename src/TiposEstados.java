public class TiposEstados {
	static final public int PRONTO = 1;
	static final public int EXECUTANDO = 2;
	static final public int ESPERA = 3;
	static final public int FINALIZADO = 4;

	static public void exibePCB(PCB pcb) {
		if (pcb.getEstado() == PRONTO) {
			System.out.println("Estado PRONTO");
		} else if (pcb.getEstado() == EXECUTANDO) {
			System.out.println("Estado EXECUTANDO");
		} else {
			System.out.println("Estado ESPERA");
		}
		System.out.println("PID " + pcb.getId_processo());
		System.out.println("Número de picos CPU " + pcb.getNumeroPicosCPU());
		System.out.println("Pico atual " + pcb.getPicoCPUAtual());
	}
}
