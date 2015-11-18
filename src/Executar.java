
public class Executar implements Runnable {
	
	PCB inter;
	int contador=0;
	int picoAtual;
	
	public Executar(PCB aux,int contador,int picoAtual){
		//aux.setEstado(TiposEstados.EXECUTANDO);
		inter = aux;
		this.picoAtual = picoAtual;
		this.contador = contador;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(contador < inter.getPicosCPU()[picoAtual]){
			contador++;
		}
		System.out.println(contador);
		inter.setContador(contador);
		//inter.setContador(contador);
		
	}
	
}
