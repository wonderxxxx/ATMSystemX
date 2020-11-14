package com.zhu;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class BankServer {

    private JFrame frame;
    private JTextField textField;
    private Request request;
    private JTextArea textArea;
    private Response response;
    private Socket socket;
    private Service service = new Service();
    private ResultCode resultCode;
    private Date date = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private JScrollPane jsp;
//    ScrollPane jsp =new JScrollPane(textArea_1);
//jsp.setBounds(17,106,476,407);
//    add(jsp);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    BankServer window = new BankServer();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public BankServer() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(153, 204, 153));
        frame.setTitle("半圆银行");
        frame.setBounds(100, 100, 495, 657);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("欢迎来到半圆银行后台管理系统");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(new Color(255, 0, 0));
        lblNewLabel.setBounds(6, 6, 483, 45);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("请输入端口号：");
        lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        lblNewLabel_1.setForeground(Color.BLUE);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setBounds(38, 63, 112, 36);
        frame.getContentPane().add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 16));
        textField.setText("8888");
        textField.setBounds(153, 63, 130, 36);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("启动服务");
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    try {
                        start();
                    } catch (IOException e01) {
                        e01.printStackTrace();
                    }
                }).start();
            }
        });
        btnNewButton.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        btnNewButton.setForeground(Color.BLUE);
        btnNewButton.setBounds(328, 64, 117, 36);
        frame.getContentPane().add(btnNewButton);

        textArea = new JTextArea();
        textArea.setEditable(false);
        jsp=new JScrollPane(textArea);
//        textArea.setBounds(6, 127, 483, 502);
        jsp.setBounds(6, 127, 470, 480);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(jsp);


        JLabel lblNewLabel_2 = new JLabel("用户操作信息记录：");
        lblNewLabel_2.setFont(new Font("Lucida Grande", Font.BOLD, 14));
        lblNewLabel_2.setForeground(new Color(0, 0, 0));
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2.setBounds(6, 99, 130, 27);
        frame.getContentPane().add(lblNewLabel_2);
    }

    public void start() throws IOException {
        Properties property = new Properties();
        property.load(new FileInputStream("D:\\Projects\\ATM(5)\\BankServer\\src\\config.properties"));
        int port = Integer.parseInt(property.getProperty("port"));
        //创建一个服务端：
        ServerSocket server = new ServerSocket(port);
        System.out.println("服务器已经启动！");
        //等待状态
        while (true) {
            try {
                socket = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                try {
                    //接受客户端发送的消息
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    request = (Request) ois.readObject();
                    judge(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void judge(Request request) {
        if ("bankCard".equals(request.getType())) {
            //判断卡号是否存在
            resultCode = service.bankCardisExist(Integer.parseInt(request.getValue() + ""));
            if (resultCode.equals(ResultCode.SUCCESS)) {
                //卡号存在,继续检验卡号是否被冻结
                if (!service.judgeCardFreeze(Integer.parseInt(request.getValue() + ""))) {
                    //卡号没有被冻结-且登录成功
                    System.out.println(request.getValue());
                    textArea.append(sdf.format(date) + " " + request.getValue() + "，登录成功！" + "\n");
                    write("bankCard", resultCode);
                } else {
                    //卡号被冻结
                    System.out.println(request.getValue());
                    textArea.append(sdf.format(date)+ " " + request.getValue() + "操作失败-卡号冻结状态中！"+ "\n");
                    write("bankCard", ResultCode.CARD_FREEZE);
                }
            } else if(resultCode.equals(ResultCode.NO_CARDNO)){
                //卡号不存在
                System.out.println(request.getValue());
                textArea.append(sdf.format(date) + " " + "，卡号不存在！"+ "\n");
                write("bankCard", ResultCode.NO_CARDNO);
            } else if(resultCode.equals(ResultCode.CARD_FREEZE)){
                //卡号被冻结
                textArea.append(sdf.format(date) + request.getValue() + "，卡号被冻结！"+ "\n");
                write("bankCard", ResultCode.CARD_FREEZE);
            }
        } else if ("pwdAndIDCard".equals(request.getType())) {
            //判断密码是否正确
            List<Integer> list = (List) request.getValue();
            resultCode = service.pwdISRight(list.get(0), list.get(1),list.get(2));
            if (resultCode.equals(ResultCode.SUCCESS)) {
                //密码正确
                textArea.append(sdf.format(date) + " " + list.get(0) + "，检验密码成功！"+ "\n");
                write("pwdAndIDCard", resultCode);
            } else {
                //密码错误
                if(list.get(2) > 0){
                    write("pwdAndIDCard", ResultCode.PWD_ERROR);
                    textArea.append(sdf.format(date) + " " + list.get(0) + "，密码失败！密码输入还剩 "+ list.get(2) + " 次机会。" + "\n");
                }else{
                    write("pwdAndIDCard", ResultCode.CARD_FREEZE);
                    textArea.append(sdf.format(date) + " " + list.get(0) + "密码输入次数过多，卡号被冻结。");
                }
            }
        } else if ("checkAccount".equals(request.getType())) {
            //查看个人账户信息
            Double balance = service.checkAccount(Integer.parseInt(request.getValue() + ""));
            System.out.println(balance);
            write("checkAccount", balance);
        } else if ("transferAccount".equals(request.getType())) {
            //转账
            List<Object> list = (List<Object>) request.getValue();
            resultCode = service.transferAccount((int) list.get(0), (int) list.get(1), (double) list.get(2));
            textArea.append(sdf.format(date) + " " + list.get(0) +"给"+ list.get(1) + "转了" + list.get(2) + "元。"+ "\n");
            write("checkAccount", resultCode);
        } else if ("takeMoney".equals(request.getType())) {
            //取钱
            List<Integer> list = (List<Integer>) request.getValue();
            resultCode = service.takeMoney(list.get(0), list.get(1));
            textArea.append(sdf.format(date) + " " + list.get(0) + "取了" + list.get(1)+ "元。" +"\n");
            write("takeMoney", resultCode);
        } else if ("saveMoney".equals(request.getType())) {
            //存钱
            List<Integer> list = (List<Integer>) request.getValue();
            resultCode = service.saveMoney(list.get(0), list.get(1));
            textArea.append(sdf.format(date) + " " + list.get(0)+ ",存了" + list.get(1) + "元。"+ "\n");
            write("saveMoney", resultCode);
        }
    }

    //将相关的信息发送给客户端
    public void write(String type, Object resultCode) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Response<>(type, resultCode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
