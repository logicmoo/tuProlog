package alice.tuprolog.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

import alice.tuprolog.TuAbstractSocket;
import alice.tuprolog.Client_Socket;
import alice.tuprolog.Datagram_Socket;
import alice.tuprolog.TuInt;
import alice.tuprolog.TuLibrary;
//import alice.tuprolog.MalformedGoalException;
//import alice.tuprolog.NoSolutionException;
import alice.tuprolog.TuProlog;
import alice.tuprolog.TuPrologError;
import alice.tuprolog.Server_Socket;
//import alice.tuprolog.SolveInfo;
import alice.tuprolog.TuStruct;
import alice.tuprolog.TuTerm;
import alice.tuprolog.Term;
import alice.tuprolog.interfaces.*;

/**
 * 
 * @author Mirco Bordoni
 * 
 * This library implements TCP socket synchronous and asynchronous communication between Prolog hosts.
 *
 */

public class SocketLibrary extends TuLibrary implements ISocketLib {
	private static final long serialVersionUID = 1L;
	private String addrRegex;
	private LinkedList<ThreadReader> readers;			// Active readers
	private LinkedList<ServerSocket> serverSockets;		// Opened ServerSockets
	private LinkedList<Socket> clientSockets;			// Opened Sockets

	public SocketLibrary() {
		addrRegex = "[\\. :]";	// Address:Port parsed using regex
		readers = new LinkedList<ThreadReader>();
		serverSockets=new LinkedList<ServerSocket>();
		clientSockets=new LinkedList<Socket>();
	}

	@Override
	public String getTheory() {
		return "";
	}

	

	/* SocketLib UDP extension by Adelina Benedetti */
	
	// Open an udp socket

	@Override
	public boolean udp_socket_open_2(TuStruct Address, Term Socket) throws TuPrologError
	{
		if (!(Socket.getTerm() .isVar())) { // Socket has to be a variable
			throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
		}
		byte[] address = new byte[4];
		int port;

		// Transform IP:Port to byte[] array and port number
		Pattern p = Pattern.compile(addrRegex);
		String[] split = p.split(Address.getName());
		if (split.length != 5)
			throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
		for (int i = 0; i < split.length - 1; i++) {
			address[i] = Byte.parseByte(split[i]);
		}
		port = Integer.parseInt(split[split.length - 1]);

		try {
			DatagramSocket s=new DatagramSocket(port, InetAddress.getByAddress(address));

			Socket.unify(this.getEngine(), new Datagram_Socket(s));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
		} catch (IOException e) {
			e.printStackTrace();
			throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
		}

		return true;
	}
	
	// send an udp data
	
	@Override
	public boolean udp_send_3(Term Socket, Term Data, TuStruct AddressTo) throws TuPrologError
	{
		if (!(Socket.getTerm() .isVar())) { // Socket has to be a variable
			throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
		}
		byte[] address = new byte[4];
		int port;

		// Transform IP:Port to byte[] array and port number
		Pattern p = Pattern.compile(addrRegex);
		String[] split = p.split(AddressTo.getName());
		if (split.length != 5)
			throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
		for (int i = 0; i < split.length - 1; i++) {
			address[i] = Byte.parseByte(split[i]);
		}
		port = Integer.parseInt(split[split.length - 1]);
		{
			DatagramSocket s = ((Datagram_Socket) Socket.getTerm()).getSocket();
			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(baos);
				 oos.writeObject(Data);
				 oos.flush();
				 byte[] Buf= baos.toByteArray();
				 DatagramPacket packet = new DatagramPacket(Buf, Buf.length,port);
				 s.send(packet);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}	


	return true;
}

// udp socket close
@Override
public boolean udp_socket_close_1(Term Socket) throws TuPrologError {
	if (Socket.getTerm() .isVar()) { 			
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	if (!(((Server_Socket) Socket.getTerm()).isDatagramSocket())) {		
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	DatagramSocket s=((Datagram_Socket) Socket.getTerm()).getSocket();
	s.close();
	return true;
}

//udp receive data
@Override
public boolean udp_receive(Term Socket, Term Data, TuStruct AddressFrom,
		TuStruct Options) throws TuPrologError {
	if (!(Socket.getTerm() .isVar())) { 
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	byte[] address = new byte[4];
	@SuppressWarnings("unused")
	int port;

	// Transform IP:Port to byte[] array and port number
	Pattern p = Pattern.compile(addrRegex);
	String[] split = p.split(AddressFrom.getName());
	if (split.length != 5)
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	for (int i = 0; i < split.length - 1; i++) {
		address[i] = Byte.parseByte(split[i]);
	}
	port = Integer.parseInt(split[split.length - 1]);
	DatagramSocket s= ((Datagram_Socket) Socket.getTerm()).getSocket();
	byte[] buffer = new byte[100000];
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length );
	try {
		
		s.receive(packet);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	LinkedList<Term> list = StructToList(Options);
	for (Term t : list) { // Explore options list
		if (((TuStruct) t).getName().equals("timeout")) { // If a timeout has been specified
			int time = Integer.parseInt(((TuStruct) t).getArg(0).toString());
			try {
				s.setSoTimeout(time);
			} catch (SocketException e) {
				e.printStackTrace();
				
			}
		}
		if(((TuStruct) t).getName().equals("size")){//if a datagram size has been specified
			int size=Integer.parseInt(((TuStruct) t).getArg(0).toString());
			packet.setLength(size);
		}
	}
		
	
	return true;
}

/**
 * Create a ServerSocket bound to the specified Address.
 * 
 * @throws TuPrologError if Socket is not a variable
 */

@Override
public boolean tcp_socket_server_open_3(TuStruct Address, Term Socket, TuStruct Options) throws TuPrologError {
	int backlog=0;

	if (!(Socket.getTerm() .isVar())) { // Socket has to be a variable
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}

	byte[] address = new byte[4];
	int port;

	// Transform IP:Port to byte[] array and port number
	Pattern p = Pattern.compile(addrRegex);
	String[] split = p.split(Address.getName());
	if (split.length != 5)
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	for (int i = 0; i < split.length - 1; i++) {
		address[i] = Byte.parseByte(split[i]);
	}
	port = Integer.parseInt(split[split.length - 1]);


	LinkedList<Term> list = StructToList(Options); 			// Convert Options Struct to a LinkedList
	for (Term t : list) { 									// Explore Options list
		if (((TuStruct) t).getName().equals("backlog")) { 	// If a backlog has been specified
			backlog = Integer.parseInt(((TuStruct) t).getArg(0).toString());
		}
	}

	// Create a server socket.
	try {
		ServerSocket s=new ServerSocket(port, backlog, InetAddress.getByAddress(address));
		addServerSocket(s);
		Socket.unify(this.getEngine(), new Server_Socket(s));
	} catch (UnknownHostException e) {
		e.printStackTrace();
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	} catch (IOException e) {
		e.printStackTrace();
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}

	return true;
}

// Add a newly created ServerSocket to the list serverSockets, so they can be closed when the engine 
// has solved a goal or is halted.
private void addServerSocket(ServerSocket s){
	for(ServerSocket sock: serverSockets){
		if(sock.equals(s))return;
	}
	serverSockets.add(s);
}

// Add a newly created ClientSocket to the list clientSockets, so they can be closed when the engine 
// has solved a goal or is halted.
private void addClientSocket(Socket s){
	for(Socket sock: clientSockets){
		if(sock.equals(s))return;
	}
	clientSockets.add(s);
}


/**
 * Accept a connection to the specified ServerSocket. This method blocks
 * until a connection is received.
 * 
 * @throws TuPrologError if ServerSock is a variable or it is not a Server_Socket
 */
@Override
public boolean tcp_socket_server_accept_3(Term ServerSock, Term Client_Addr, Term Client_Slave_Socket) throws TuPrologError {

	if (ServerSock.getTerm() .isVar()) { 	// ServerSock has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}

	TuAbstractSocket as= (TuAbstractSocket)ServerSock.getTerm();
	if(!as.isServerSocket()){									// ServerSock has to be a Server_Socket
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}

	ServerSocket s = ((Server_Socket) ServerSock.getTerm()).getSocket();
	Socket client;
	try {
		client = s.accept();
		Client_Addr.unify(this.getEngine(), TuTerm.createAtomTerm(client.getInetAddress().getHostAddress() + ":" + client.getPort()));
		Client_Slave_Socket.unify(this.getEngine(), new Client_Socket(client));
		addClientSocket(client);
	} catch (IOException e) {
		//e.printStackTrace();
		return false;
	}
	return true;
}

/**
 * Create a Client_Socket and connect it to a specified address.
 * @throws TuPrologError if Socket is not a variable
 */
@Override
public boolean tcp_socket_client_open_2(TuStruct Address, Term SocketTerm) throws TuPrologError {
	if (!(SocketTerm.getTerm() .isVar())) { // Socket has to be a variable
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 2);
	}

	byte[] address = new byte[4];
	int port;

	// IP:Port --> IP in byte[] array and port number
	Pattern p = Pattern.compile(addrRegex);
	String[] split = p.split(Address.getName());
	if (split.length != 5)
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	for (int i = 0; i < split.length - 1; i++) {
		address[i] = Byte.parseByte(split[i]);
	}
	port = Integer.parseInt(split[split.length - 1]);

	Socket s;
	try {
		s = new Socket(InetAddress.getByAddress(address), port);
		SocketTerm.unify(this.getEngine(), new Client_Socket(s));
		addClientSocket(s);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

/**
 * Close a Server_Socket
 * @throws TuPrologError if serverSocket is a variable or it is not a Server_Socket
 */
@Override
public synchronized boolean tcp_socket_server_close_1(Term serverSocket) throws TuPrologError {
	if (serverSocket.getTerm() .isVar()) { 			// serverSocket has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	if (!(((Server_Socket) serverSocket.getTerm()).isServerSocket())) {		// serverSocket has to be a Server_Socket
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	try {
		ServerSocket s=((Server_Socket) serverSocket.getTerm()).getSocket();
		s.close();
		// Remove closed ServerSocket from serverSockets list
		for(int i=0;i<serverSockets.size();i++){
			if(serverSockets.get(i).equals(s)){
				serverSockets.remove(i);
				return true;
			}
		}
	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

/**
 * Send Msg through the socket Socket. Socket has to be connected!
 * @throws TuPrologError if Socket is a variable or it is not a Client_Socket or Msg is not bound
 */
@Override
public boolean write_to_socket_2(Term Socket, Term Msg) throws TuPrologError {
	if (Socket.getTerm() .isVar()) { // Socket has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	if (((TuAbstractSocket) Socket.getTerm()).isServerSocket()) { // Only Client_Sockets can send data
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	if (Msg.getTerm() .isVar()) { // Record has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 2);

	} else {
		Socket sock = (((Client_Socket) Socket.getTerm()).getSocket());
		try {
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			out.writeObject(Msg);		// Write message in OutputStream
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

	}
	return true;
}

/**
 * Synchronous reading from Socket. This is a blocking operation.
 * @param Options The user can specify a timeout using [timeout(millis)]. If timeout expires the
 * 					predicate fails
 * @throws TuPrologError if Socket is not bound or it is not a Client_Socket or Msg is bound
 */
@Override
public boolean read_from_socket_3(Term Socket, Term Msg, TuStruct Options) throws TuPrologError {
	if (Socket.getTerm() .isVar()) { // Socket has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	if (!(Msg.getTerm() .isVar())) { // Message has to be a variable
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 2);
	}
	if (!((TuAbstractSocket) Socket.getTerm()).isClientSocket()) { // Only Client_Sockets can receive data
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	} else {
		Socket sock = (((Client_Socket) Socket.getTerm()).getSocket());

		// Check if a Reader associated to the Socket passed already exists
		ThreadReader r = readerExist(sock);
		// If a thread is already waiting for data on the same socket return false
		if (r != null) {
			if (r.started())
				return false;
		}

		LinkedList<Term> list = StructToList(Options); // Convert Options Struct to a LinkedList
		for (Term t : list) { // Explore options list
			if (((TuStruct) t).getName().equals("timeout")) { // If a timeout has been specified
				int time = Integer.parseInt(((TuStruct) t).getArg(0).toString());
				try {
					sock.setSoTimeout(time); // Set socket timeout
				} catch (SocketException e) {
					e.printStackTrace();
					return false;
				}
			}
		}



		try {
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			Term m = (Term) in.readObject();
			Msg.unify(this.getEngine(), m);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

	}
	return true;
}

/**
 * Asynchronous read from Socket. When a message is received an assertA
 * (by default) is executed to put it in the theory. The user can set the
 * option "assertZ" to use assertZ instead of assertA.
 * 
 * @param Socket
 *            Socket used to read
 * @param Options
 *            a timeout can be specified for the socket with the option
 *            [timeout(millis)]. If timeout expires while reading, nothing
 *            is read and nothing is asserted.
 *            The user can insert the option assertZ to change the way the 
 *            received message is asserted
 * @return true if no error happens
 * @throws TuPrologError if Socket is not bound or it is not a Client_Socket
 */
@Override
public boolean aread_from_socket_2(Term Socket, TuStruct Options) throws TuPrologError {
	ThreadReader r;
	if (Socket.getTerm() .isVar()) { // Socket has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	if (!((TuAbstractSocket) Socket.getTerm()).isClientSocket()) { // Only Client_Sockets can receive data
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	} else {
		// Retrieve socket from the term Socket passed to this method
		Socket sock = (((Client_Socket) Socket.getTerm()).getSocket());

		// Find reader associated with the socket if already exists,
		// otherwise create a new reader
		r = readerExist(sock);
		if (r == null) {
			synchronized (this) {
				readers.add(new ThreadReader(sock, this.getEngine()));
				r = readers.getLast();
			}
		}

		// If reader already reading return true, otherwise start reading
		if (r.started())
			return true;

		try {
			sock.setSoTimeout(0); // Set socket timeout to infinite
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		LinkedList<Term> list = StructToList(Options); // Convert Options Struct to a LinkedList
		for (Term t : list) { // Explore options list
			if (((TuStruct) t).getName().equals("timeout")) { // If a timeout has been specified
				int time = Integer.parseInt(((TuStruct) t).getArg(0).toString());
				try {
					sock.setSoTimeout(time); // Set socket timeout
				} catch (SocketException e) {
					e.printStackTrace();
					return false;
				}
			}
			// If assertZ is specified what is read is written in the theory
			// with assertZ instead of assertA
			if (((TuStruct) t).getName().equals("assertZ")) {
				r.assertZ();
			}
		}

		r.startRead();
	}
	return true;
}


/*
 * Transform the Struct s in a LinkedList
 */
private LinkedList<Term> StructToList(TuStruct s) {
	LinkedList<Term> list = new LinkedList<Term>();
	Term temp;
	temp = s;
	while (true) {
		if (((TuStruct) temp).getName().equals(".")) {
			list.add(((TuStruct) temp).getArg(0));
		} else
			break;
		temp = ((TuStruct) temp).getArg(1);

	}
	return list;
}


/*
 * Check whether a reader associated to socket s already exists
 */
private ThreadReader readerExist(Socket s) {
	for (ThreadReader r : readers) {
		if (r.compareSocket(s))
			return r;
	}
	return null;
}


/*
 * When a goal is solved close all ServerSockets and stop all readers
 */
@Override
public void onSolveEnd(){
	for(ServerSocket s:serverSockets){
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	serverSockets=new LinkedList<ServerSocket>();
	for(Socket s:clientSockets){
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	clientSockets = new LinkedList<Socket>();
	for(ThreadReader r:readers)r.stopRead();

}

/*
 * If the user stops the computation call onSolveEnd() to close all sockets and stop all readers
 */
@Override
public void onSolveHalt(){
	onSolveEnd();
}

public boolean getAddress_2(Term sock, Term addr) throws TuPrologError {
	if (sock.getTerm() .isVar()) { // Socket has to be bound
		throw TuPrologError.instantiation_error(engine.getEngineManager(), 1);
	}
	TuAbstractSocket abs = (TuAbstractSocket) sock.getTerm();
	if (abs.isClientSocket()) {
		Socket s = (((Client_Socket) sock.getTerm()).getSocket());
		addr.unify(this.getEngine(), TuStruct.createTuStruct1(s.getInetAddress().toString(), TuTerm.createAtomTerm(TuTerm.i32(s.getLocalPort()).toString())));
		return true;
	}
	if (abs.isServerSocket()) {
		ServerSocket s = ((Server_Socket) sock.getTerm()).getSocket();
		addr.unify(this.getEngine(), TuStruct.createTuStruct1(s.getInetAddress().toString(), TuTerm.createAtomTerm(TuTerm.i32(s.getLocalPort()).toString())));
		return true;
	}
	if (abs.isDatagramSocket()) {
		DatagramSocket s = (((Datagram_Socket) sock.getTerm()).getSocket());
		addr.unify(this.getEngine(), TuStruct.createTuStruct1(s.getInetAddress().toString(), TuTerm.createAtomTerm(TuTerm.i32(s.getLocalPort()).toString())));
		return true;
	}

	return true;
}



/*
 * Definition of thread Reader. It waits until a message is received and assert it.
 */
private class ThreadReader extends Thread {
	private Socket socket;				// Socket associated to the Reader
	private TuProlog mainEngine;
	private boolean assertA;			// Should it use assertA or assertZ?
	private volatile boolean started;	// True if the thread is already waiting on a socket
	private Semaphore sem;

	protected ThreadReader(Socket socket, TuProlog mainEngine) {
		this.socket = socket;
		this.mainEngine = mainEngine;
		assertA = true;					// assertA by default
		started = false;
		sem = new Semaphore(0);
		this.start();
	}

	// Set the boolean variable started and release the semaphore where the thread is waiting
	protected synchronized void startRead() {
		if(started)return;
		started = true;
		sem.release();
	}

	protected boolean started() {
		return started;
	}

	// Close the socket (to stop the thread if it is waiting on the read method) 
	// and interrupt the thread (if it is waiting on the semaphore)
	protected synchronized void stopRead(){
		this.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected synchronized void assertZ() {
		assertA = false;
	}

	protected boolean compareSocket(Socket s) {
		return s.equals(socket);
	}

	@Override
	public void run() {
		while (true) {
			while (!started) {
				try {
					sem.acquire();
					if(this.isInterrupted())return;
				} catch (InterruptedException e1) {
					//e1.printStackTrace();
					return;
				}
			}
			try {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				if(this.isInterrupted())return;
				Term msg = (Term) in.readObject();
				if(this.isInterrupted())return;					
				TuStruct s = (TuStruct) Term.createTerm(msg.getTerm().toString());
				if (assertA)
					mainEngine.getTheoryManager().assertA(s, true, "", false);
				else
					mainEngine.getTheoryManager().assertZ(s, true, "", false);
				assertA = true; // By default use assertA!
				started = false;
			} catch (IOException e) {
				//e.printStackTrace();
				started = false;
				return;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				started = false;
				return;
			}
		}
	}

}




}
