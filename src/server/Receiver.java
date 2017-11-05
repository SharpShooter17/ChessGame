package server;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import common.Request;
import javafx.util.Pair;

public abstract class Receiver extends Thread {
	private static int idCounter = 0;
	private int id;
	private Receiver parent;

	private List<Client> clientList;
	private Queue<Pair<Request, Client>> requestQueue;
	private Queue<Integer> tasks;

	private boolean bDone;

	protected abstract void handleTheRequest(Pair<Request, Client> request);
	protected abstract void handleTheTask(Integer task);
	protected abstract void sendRespond();
	protected abstract void loop();

	public void run(){
		while(!bDone){
			this.loop();
			if (this.isRequest()){
				this.handleTheRequest(this.getRequest());
			}

			if (this.isTask()){
				this.handleTheTask(this.getTask());
			} else {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	@SuppressWarnings("static-access")
	public Receiver(Receiver parent){
		this.clientList = new ArrayList<Client>();
		this.requestQueue = new ArrayDeque<Pair<Request, Client>>();
		this.bDone = false;
		this.id = this.idCounter++;
		this.parent = parent;
		this.tasks = new ArrayDeque<>();
	}
	public boolean addClient(Client client){
		return getClientList().add(client);
	}
	public boolean removeClient(Client client){
		return getClientList().remove(client);
	}
	public void addRequest(Request request, Client client){
		getRequestList().add(new Pair<Request, Client>(request, client));
	}
	public synchronized List<Client> getClientList() {
		return clientList;
	}
	private boolean isRequest(){
		return !getRequestList().isEmpty();
	}
	private Pair<Request, Client> getRequest(){
		return getRequestList().poll();
	}
	public void close(){
		bDone = true;
	}
	public int getMyId(){
		return this.id;
	}
	private synchronized Queue<Pair<Request, Client>> getRequestList(){
		return this.requestQueue;
	}
	public Receiver getParent(){
		return this.parent;
	}
	private Integer getTask(){
		return tasks.poll();
	}
	private boolean isTask(){
		return !this.tasks.isEmpty();
	}
	protected void addTask(Integer task){
		this.tasks.add(task);
	}
}
