package com.yankaizhang.board.headless;

/**
 * 控制台版本服务端
 */
public class ServerHeadless {

    private final ServerAcceptorHeadless acceptor = new ServerAcceptorHeadless();

    public ServerHeadless(){
        int port = 7800;
        acceptor.setPort(port);
        acceptor.start();
    }

    public static void main(String[] args) {
        new ServerHeadless();
    }
}
