package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint("/chat/{username}")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(String msg) throws IOException {
        //TODO: add send message method.
        for (Map.Entry<String,Session> sessionEntry: onlineSessions.entrySet()) {
            // or - session.getValue().getAsyncRemote().sendText(msg);
            sessionEntry.getValue().getBasicRemote().sendText(msg);
        }
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        //TODO: add on open connection.
        onlineSessions.put(session.getId(),session);
        Message openMsg = new Message("ENTER", username, " has joined the chat room", onlineSessions.size());
        sendMessageToAll(JSON.toJSONString(openMsg));
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) throws IOException {
        //TODO: add send message.
        Message inMsg = (Message) JSON.parseObject(jsonStr, Message.class);
        Message outMsg = new Message("CHAT", inMsg.getUsername(), inMsg.getMsg(), onlineSessions.size());
        sendMessageToAll(JSON.toJSONString(outMsg));
    }



    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) throws IOException {
        //TODO: add close connection.
        onlineSessions.remove(session.getId());
        Message closeMsg = new Message("LEAVE", username, " has left the chat room", onlineSessions.size());
        sendMessageToAll(JSON.toJSONString(closeMsg));
        // session.close();
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
