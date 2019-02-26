package alice.tuprolog.interfaces;

import alice.tuprolog.TuPrologError;
import alice.tuprolog.TuStruct;
import alice.tuprolog.Term;

public interface ISocketLib {
    public boolean tcp_socket_client_open_2(TuStruct Address, Term Socket) throws TuPrologError;
    
    public boolean tcp_socket_server_open_3(TuStruct Address, Term Socket, TuStruct Options) throws TuPrologError; 
    
    public boolean tcp_socket_server_accept_3(Term ServerSock, Term Client_Addr, Term Client_Slave_Socket) throws TuPrologError;
    
    public boolean tcp_socket_server_close_1(Term serverSocket) throws TuPrologError;
    
    public boolean read_from_socket_3(Term Socket, Term Msg, TuStruct Options) throws TuPrologError;
    
    public boolean write_to_socket_2(Term Socket, Term Msg) throws TuPrologError;
    
    public boolean aread_from_socket_2(Term Socket, TuStruct Options) throws TuPrologError;
    
    public boolean udp_socket_open_2(TuStruct Address, Term Socket) throws TuPrologError;
    
    boolean udp_send_3(Term Socket, Term Data, TuStruct AddressTo) throws TuPrologError;
    
    boolean udp_receive(Term Socket, Term Data, TuStruct AddressFrom, TuStruct Options) throws TuPrologError;

    public boolean udp_socket_close_1(Term socket) throws TuPrologError;
}
