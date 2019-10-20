package offline;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.List;
import util.Node;

public class LRU extends Memory{

	public LRU(int capacity) {
		super(capacity);
	}
	
	List<Tuple> list = new List<Tuple>();
	
	public synchronized void request(char operation, Tuple t) {

		//System.out.println(" -:>"+operation+" "+t.getStringData());
		numberOfOperation++;
		
		if(t.getNode() == null) {
			missCount++;
			increase(t.size());
			Node<Tuple> n = new Node<>();
			n.setValue(t);
			t.setNode(n);
			
			add(n);	
			
		}else {
			
			if(t.getNode().getList() == coldList) {
				missCount++;
				coldList.remove(t.getNode());
				add(t.getNode());	
			}else if(t.getNode().getList() == list) {
				super.hitCount++;
				list.remove(t.getNode());
				list.add(t.getNode());
			}else {
				System.out.println("List error?");
			}
			
		}
			
	}
	
	public void add(Node<Tuple> node) {
		
		if(list.size() == super.capacity){
			replacement();
		}
		
		list.add(node);
	

	}
	
	
	
	public void replacement(){
		
		coldList.add(list.remove(list.getHead()));
	}
	
	
	public void remove(Tuple p) {
		
		p.getNode().getList().remove(p.getNode());
		
	}
	
	public String toString(){
		return list.toString();
	}

	

	@Override
	public String getName() {
		
		return "Least Recently Used (LRU)";
	}


	@Override
	public List<Tuple> getTuples() {
		List<Tuple> newList = new List<>();
		
		
		
		
		return newList;
	}



	public void saveData() {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh-mm");
		String date = dt.format(new Date());
		
		try {
			PrintWriter logRequests = new PrintWriter(new FileWriter(new File(date+" "+getName()+".tuples") ,  true));
		
			
			Node<Tuple> node = list.getHead();
			while (node != null) {
			
				logRequests.println(node.getValue().getTupleID()+","+node.getValue().getOperation());
				node = node.getNext();
			}
			
			logRequests.flush();
			logRequests.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}


	@Override
	public int getCurrentNumberOfPages() {
		return list.size();
	}

	
}
