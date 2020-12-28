package com.yankaizhang.board.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientChat extends JPanel {

    private final ClientCanvas mainCanvas;

    JPanel jponline = null; // 显示在线人员
    JPanel jpMessage = null; // 发送消息组件
    JButton jbSend = null; // 发送按钮
    JButton jbClear = null; // 清除按钮
    JTextField jtf = null; // 消息编辑框
    JScrollPane jscr = null;// 聊天记录框滚动条
    JScrollPane jscro = null;// 在线人员显示框滚动条
    JTextArea jta = null;
    JTextArea jtonline = null;


    public ClientChat(ClientCanvas canvas){
        mainCanvas = canvas;
        initCharArea();
    }

    /**
     * 初始化聊天区域
     */
    private void initCharArea() {
        jponline = new JPanel();
        jpMessage = new JPanel();
        jbSend = new JButton("发送");
        jbSend.setFont(new Font("宋体", Font.BOLD, 16));
        jbSend.addActionListener(e -> sendChatMsg());


        // 清空聊天框
        jbClear = new JButton("清空");
        jbClear.setFont(new Font("宋体", Font.BOLD, 16));
        jbClear.addActionListener(e -> {
            jtf.setText("");
            jta.setText("");
        });


        jtf = new JTextField();
        jtf.setForeground(Color.BLUE);
        jtf.setFont(new Font("宋体", Font.PLAIN, 16));
        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendChatMsg();
                }
            }
        });

        jta = new JTextArea();
        jta.setEditable(false);
        jta.setForeground(Color.blue);
        jta.setFont(new Font("宋体", Font.PLAIN, 16));
        jscr = new JScrollPane(jta);
        jscr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jscr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jta.setLineWrap(true);
        jpMessage.setLayout(new GridLayout(2, 1, 0, 0));

        // 设置在线人员显示
        jtonline = new JTextArea();
        jtonline.setEditable(false);
        jtonline.setForeground(Color.magenta);
        jtonline.setFont(new Font("宋体", Font.PLAIN, 16));
        jtonline.setLineWrap(true);
        jtonline.setText("");

        jscro = new JScrollPane(jtonline);
        jscro.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jscro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jscro.setPreferredSize(new Dimension(180,80));


        JPanel panel = new JPanel();
        panel.add(jbSend);
        panel.add(jbClear);
        jpMessage.add(jtf);
        jpMessage.add(panel);

        // 添加在线人数JPanel
        jponline.setLayout(new BorderLayout());
        jponline.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "聊天窗口", TitledBorder.CENTER, TitledBorder.BELOW_BOTTOM, null, Color.black));
        jponline.add(jscro,BorderLayout.CENTER);

        this.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "在线人员", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, null, Color.black));
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(200, 70));
        this.add(jscr);
        this.add(jponline, BorderLayout.NORTH);
        this.add(jpMessage, BorderLayout.SOUTH);
    }


    private void sendChatMsg() {
        String str = jtf.getText().trim();
        if ("".equals(str)){
            JOptionPane.showMessageDialog(null, "不能发送空消息", "提示", JOptionPane.ERROR_MESSAGE);
        }
        if (mainCanvas.isOnline()){
            mainCanvas.sendTextMessage(mainCanvas.getUserName() + ":" + str);
        }
        jtf.setText("");
    }

    /**
     * 在聊天框中展示聊天信息
     */
    public void showMsg(String msg){
        jta.append(msg);
    }

    /**
     * 清空在线列表和聊天信息
     */
    public void reset(){
        jtf.setText("");
        jta.setText("");
        jtonline.setText("");
    }
}
