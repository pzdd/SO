
public class PCB {
	private int id_processo;
	/* Estados possiveis PRONTO,EXECUTANDO,ESPERA*/
	private int estado;
	/*  o número acumulado de unidades de tempo de execução*/
	private int contador;
	private int numeroPicosCPU;
	private int picosCPU[] = new int[50];
	
	private int tempoChegada;
	private int tempoInicializacao;

	private int picoCPUAtual;
	/* Tempo de finalização do atual serviçode E/S*/
	private int finalizacaoES;
	
	public PCB(){
		
	}
	
	public void setPicosCPU(int[] picosCPU) {
		this.picosCPU = picosCPU;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getContador() {
		return contador;
	}
	public void setContador(int contador) {
		this.contador = contador;
	}
	public int getNumeroPicosCPU() {
		return numeroPicosCPU;
	}
	public void setNumeroPicosCPU(int numeroPicosCPU) {
		this.numeroPicosCPU = numeroPicosCPU;
	}
	public int getPicoCPUAtual() {
		return picoCPUAtual;
	}
	public void setPicoCPUAtual(int picoCPUAtual) {
		this.picoCPUAtual = picoCPUAtual;
	}
	public int getFinalizacaoES() {
		return finalizacaoES;
	}
	public void setFinalizacaoES(int finalizacaoES) {
		this.finalizacaoES = finalizacaoES;
	}
	public int getId_processo() {
		return id_processo;
	}
	public void setId_processo(int id_processo) {
		this.id_processo = id_processo;
	}

	public int[] getPicosCPU() {
		return picosCPU;
	}

	public int getTempoChegada() {
		return tempoChegada;
	}

	public void setTempoChegada(int tempoChegada) {
		this.tempoChegada = tempoChegada;
	}

	public int getTempoInicializacao() {
		return tempoInicializacao;
	}

	public void setTempoInicializacao(int tempoInicializacao) {
		this.tempoInicializacao = tempoInicializacao;
	}
	

}
