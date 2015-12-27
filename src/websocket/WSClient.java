package websocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Basil on 26/12/2015.
 * https://dzone.com/articles/sample-java-web-socket-client
 * http://www.programmingforliving.com/2013/08/jsr-356-java-api-for-websocket-client-api.html
 * https://blog.openshift.com/how-to-build-java-websocket-applications-using-the-jsr-356-api/
 */


@ClientEndpoint
public class WSClient {

    private Session session;
    private String message;
    private URI uri;
    private MessageHandler messageHandler;

    public WSClient(URI uri) {
        session = null;
        this.uri = uri;
        message = null;
        start();
    }

    public void start() {

        if (session == null) {
            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                container.connectToServer(this, uri);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Connection to " + uri.toString() + " already open.");
        }

    }

    public void stop() {
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        session = null;
    }

    public void restart() {

        try {
            if (session != null) {
                session.close();
                session = null;
            }

            start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void restart(URI uri) {
        this.uri = uri;
        restart();
    }

    public String getMessage() {
        return message;
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param session the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Session open: " + session.toString());
        this.session = session;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession
     *            the userSession which is getting closed.
     * @param reason
     *            the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Closing session: " + userSession.toString() + ", reason: " + reason);
        this.session = null;
        start();    // restart
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a
     * client send a message.
     *
     * @param message
     *            The text message
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message received: " + message);
        this.message = message;
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author Jiji_Sasidharan
     */
    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}