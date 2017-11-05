package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;

import common.Request;

public class Client extends Thread{
	private Receiver receiver;
	private Socket socket;
	private boolean bDone;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Queue<Request> listRequestToSend;
	private static int counter = 0;
	private int myId;

	public Client(Receiver receiver, Socket socket) {
		this.setReceiver(receiver);
		this.socket = socket;
		this.listRequestToSend = new ArrayDeque<Request>();
		this.myId = Client.counter++;
	}

	public void run(){
		try {
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			this.close();
			return;
		}

		this.bDone = false;

		while (!this.bDone){
			try {
				if (this.socket.getInputStream().available() > 0) {
					Request request = this.readRequest();
					if (request.getCodeRequest() == 600){
						receiver.removeClient(this);
						this.close();
						return;
					} else {
						System.out.println("Request code: " + request.getCodeRequest());
						this.receiver.addRequest(request, this);
					}
				}
			} catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (IOException e) {
			}

			if ( this.isRequest() ){
				this.sendRequest(this.getRequest());
			} else {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		this.close();
	}

	public void close(){
		try {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.bDone = true;
	}

	private Request readRequest() throws ClassNotFoundException, IOException{
		return (Request) this.input.readObject();
	}

	public void addRequest(Request request){
		getListRequestToSend().add(request);
	}

	private boolean isRequest(){
		return !getListRequestToSend().isEmpty();
	}

	private void sendRequest(Request request){
		try {
			this.output.reset();
			this.output.writeObject(request);
			this.output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Receiver getReceiver() {
		return receiver;
	}

	private Request getRequest(){
		return getListRequestToSend().poll();
	}

	private Queue<Request> getListRequestToSend(){
		return this.listRequestToSend;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	public int getMyId(){
		return this.myId;
	}
}
