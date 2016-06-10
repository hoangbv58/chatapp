package com.baidar.androidChatter.interfacer;


public interface SocketerInterface {
	
	public String sendHttpRequest(String params);
	public int startListening(int port);
	public void stopListening();
	public void exit();
	public int getListeningPort();

}
