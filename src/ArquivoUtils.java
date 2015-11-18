import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class ArquivoUtils {
	
	private BufferedReader lerArq = null;
	
	public ArquivoUtils(){
		try{
		FileReader arq = new FileReader("processos.dat");
		lerArq = new BufferedReader(arq);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public LinkedList<PCB> ler_processos(){
		String aux = "";
		LinkedList<PCB> retorno = new LinkedList<PCB>();
		try{
		for(int i=0;i<2;i++){
			int picos[] = new int[50];
			aux = lerArq.readLine();
			String num[] = aux.split(" ");
			System.out.println("PID " + num[0]);
			System.out.println("Tempo de chegada "+ num[1]);
			System.out.println("Quantidade de picos de CPU " + num[2]);
			for(int j=0;j<(num.length-3);j++){
				System.out.println("Sequência de picos de CPU "+ num[j+3]);
				picos[j] = Integer.parseInt(num[j+3]); 
			}
			PCB pcb = new PCB();
			pcb.setId_processo(Integer.parseInt(num[0]));
			pcb.setEstado(TiposEstados.PRONTO);
			pcb.setNumeroPicosCPU(Integer.parseInt(num[2]));
			pcb.setPicosCPU(picos);
			pcb.setPicoCPUAtual(0);
			retorno.add(pcb);
		}	
		}catch(IOException e){
			e.printStackTrace();
		}
		return retorno;
	}
}
