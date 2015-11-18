import java.util.LinkedList;


public class Fila<T> {
	
	LinkedList<T> pilha = new LinkedList<T>();
	
	public void adicionar(T e){
		pilha.add(e);
	}
	public void remover(){
		pilha.remove(0);
	}
	public int length(){
		return (pilha.size() == 0) ? 0 : pilha.size();   
	}
	public T get(int index){
		return pilha.get(index);
	}

}
