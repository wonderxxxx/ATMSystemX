package com.zhu;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Client {

    private Socket socket;

    public Client() throws IOException {
        Properties property = new Properties();
        property.load(new FileInputStream("D:\\Projects\\ATM(5)\\ATMServer\\src\\config.properties"));
        String ip = property.getProperty("ip");
        int port = Integer.parseInt(property.getProperty("port"));
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //校验卡号是否存在
    public Response sendIDCard(int bankCard) throws IOException {
        //向外发送消息
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Request<String, Integer> request = new Request<>("bankCard", bankCard);
        //向服务端发送消息
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接受消息
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response<String, Object> response = null;
        try {
            //接受服务端发送的响应，既下一步该如何操作！
            response = (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getValue());
        System.out.println(response.getType());
        return response;
    }

    //校验密码是否正确
    public Response sendPWDandIDCard(int bankCard, int password , int count) throws IOException {
        //向外发送消息
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        List<Integer> list = new ArrayList<>();
        list.add(bankCard);
        list.add(password);
        list.add(count);
        Request<String, List> request = new Request<>("pwdAndIDCard", list);
        //向服务端发送消息
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接受消息
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response<String, Object> response = null;
        try {
            //接受服务端发送的响应，既下一步该如何操作！
            response = (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getType());
        System.out.println(response.getValue());
        return response;
    }


    //查看账户余额
    public Response checkAccount(int bankCard) throws IOException {
        //向外发送消息
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Request<String, Integer> request = new Request<>("checkAccount", bankCard);
        //向服务端发送消息
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接受消息
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response<String, Object> response = null;
        try {
            //接受服务端发送的响应，既下一步该如何操作！
            response = (Response)ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getType());
        System.out.println(response.getValue());
        return response;
    }


    //转账
    public Response transferAccount(int bankCard, int toBankCard, double money) throws IOException {
        //向外发送消息
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        List<Object> list = new ArrayList<>();
        list.add(bankCard);
        list.add(toBankCard);
        list.add(money);
        Request<String, List> request = new Request<>("transferAccount", list);
        //向服务端发送消息
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接受消息
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response<String, Object> response = null;
        try {
            //接受服务端发送的响应，既下一步该如何操作！
            response = (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getType());
        System.out.println(response.getValue());
        return response;
    }

    //取钱
    public Response takeMoney(int bankCard, int money) throws IOException {
        //向外发送消息
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        List<Integer> list = new ArrayList<>();
        list.add(bankCard);
        list.add(money);
        Request<String, List> request = new Request<>("takeMoney", list);
        //向服务端发送消息
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接受消息
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response<String, Object> response = null;
        try {
            //接受服务端发送的响应，既下一步该如何操作！
            response = (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getType());
        System.out.println(response.getValue());
        return response;
    }

    //存钱
    public Response saveMoney(int bankCard, int moneyNo) throws IOException {
        //向外发送消息
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        List<Integer> list = new ArrayList<>();
        list.add(bankCard);
        list.add(moneyNo);
        Request<String, List> request = new Request<>("saveMoney", list);
        //向服务端发送消息
        try {
            oos.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接受消息
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Response<String, Object> response = null;
        try {
            //接受服务端发送的响应，既下一步该如何操作！
            response = (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(response.getType());
        System.out.println(response.getValue());
        return response;
    }

}
