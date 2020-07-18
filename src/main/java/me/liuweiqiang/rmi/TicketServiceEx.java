package me.liuweiqiang.rmi;

import me.liuweiqiang.rmi.exception.TicketException;

public interface TicketServiceEx {

    String requestTicket(String clientID, String serverID, String uri, String clientIP);

    void verifyTicket(String ticket, String clientID, String serverID, String uri, String clientIP) throws TicketException;
}
