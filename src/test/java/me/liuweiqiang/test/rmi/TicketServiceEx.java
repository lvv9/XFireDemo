package me.liuweiqiang.test.rmi;

//import me.liuweiqiang.rmi.exception.TicketException;
import me.liuweiqiang.test.exception.TicketException;

public interface TicketServiceEx {

    String requestTicket(String clientID, String serverID, String uri, String clientIP);

    void verifyTicket(String ticket, String clientID, String serverID, String uri, String clientIP) throws TicketException;
}
