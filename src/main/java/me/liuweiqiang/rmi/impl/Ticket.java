package me.liuweiqiang.rmi.impl;

import me.liuweiqiang.rmi.TicketServiceEx;
import me.liuweiqiang.rmi.exception.TicketException;
import org.springframework.stereotype.Component;

@Component
public class Ticket implements TicketServiceEx {

    public String requestTicket(String clientID, String serverID, String uri, String clientIP) {
        return "Qiang";
    }

    public void verifyTicket(String ticket, String clientID, String serverID, String uri, String clientIP) throws
            TicketException {
        if ("1".equals(ticket)) {
            throw new RuntimeException();
        }
        throw new TicketException();
    }
}
