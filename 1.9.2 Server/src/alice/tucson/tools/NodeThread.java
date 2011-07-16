package alice.tucson.tools;

import alice.tucson.service.Node;

public class NodeThread extends Thread{
	private Node node;
	
	public NodeThread(Node node){
		this.node = node;
	}
	
	public void run(){
		node.install();
	}
	
}
