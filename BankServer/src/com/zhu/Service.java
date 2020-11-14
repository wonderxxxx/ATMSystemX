package com.zhu;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 校验密码、验证账户、转账。。。。
 */
public class Service {

    private DataBase_ParseXML db = new DataBase_ParseXML();

    //获取数据库中的所有的值，既所有的account账户的信息
    Collection<Account> accounts = db.getMap().values();

    private File xmlFile = new File("xml/accounts.xml");
    private SAXReader reader = new SAXReader();

    private int MAX_INPUT_NUM = 3;

    //检验用户存不存在
    public ResultCode bankCardisExist(int bankCard) {
        for (Account a : accounts) {
            if (a.getBankCard() == bankCard) {
                //判断卡号是否被冻结
                if (!judgeCardFreeze(bankCard)) {
                    //成功
                    return ResultCode.SUCCESS;
                } else {
                    //卡号被冻结
                    return ResultCode.CARD_FREEZE;
                }
            }
        }
        //没有该卡号
        return ResultCode.NO_CARDNO;
    }


    //校验卡号是否被冻结
    public boolean judgeCardFreeze(int bankCard) {
        if (db.getMap().get(bankCard).getBankCardFreeze() == 0) {
            return false;
        } else {
            //卡号被冻结
            return true;
        }
    }

    //验证密码正不正确
    public ResultCode pwdISRight(int bankCard, int password,int count) {
        //将之前校验好的账户密码与其进行比较
        if ((db.getMap().get(bankCard).getPassword() == password) && count > 0) {
            //密码匹配成功
            return ResultCode.SUCCESS;
        } else {
            //密码匹配失败
            if(count <= 0){
                db.getMap().get(bankCard).setBankCardFreeze(1);
                try {
                    print(bankCard);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ResultCode.PWD_ERROR;
        }
    }

    //查看账户余额
    public double checkAccount(int bankCard) {
        return db.getMap().get(bankCard).getBalance();
    }

    //转账
    public synchronized ResultCode transferAccount(int bankCard, int toBankCard, double money) {
        //判断要转账的对方账户存不存在
        if (!ResultCode.NO_CARDNO.equals(bankCardisExist(toBankCard))) {
            //判断转账用户的余额够不够
            if (db.getMap().get(bankCard).getBalance() >= money) {
                db.getMap().get(bankCard).setBalance(db.getMap().get(bankCard).getBalance() - money);
                db.getMap().get(toBankCard).setBalance(db.getMap().get(toBankCard).getBalance() + money);
                try {
                    print(toBankCard);
                    print(bankCard);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //余额不足
                return ResultCode.NSF_CHECK;
            }
            //转账成功
            return ResultCode.SUCCESS;
        } else {
            //对方账户不存在
            return ResultCode.NO_CARDNO;
        }
    }

    //取钱
    public ResultCode takeMoney(int bankCard, int money) {
        if (db.getMap().get(bankCard).getBalance() >= money) {
            //取钱成功
            db.getMap().get(bankCard).setBalance(db.getMap().get(bankCard).getBalance() - money);
            try {
                print(bankCard);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultCode.SUCCESS;
        } else {
            //余额不足
            return ResultCode.NSF_CHECK;
        }
    }

    //存钱
    public ResultCode saveMoney(int bankCard, int moenyNo) {
        db.getMap().get(bankCard).setBalance(db.getMap().get(bankCard).getBalance() + moenyNo);
        try {
            print(bankCard);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultCode.SUCCESS;
    }


    //将更改后的数据写入到XML文件中
    public void print(int bankCard) throws DocumentException, IOException {

        Document xmlDoc = reader.read(xmlFile);

        Element root = xmlDoc.getRootElement();
        List<Element> accountList = root.elements();
        for (Element aLi : accountList) {
            if (aLi.elementText("bankCard").equals(db.getMap().get(bankCard).getBankCard() + "")) {
                aLi.element("id").setText(db.getMap().get(bankCard).getId() + "");
                aLi.element("name").setText(db.getMap().get(bankCard).getName());
                aLi.element("bankCard").setText(db.getMap().get(bankCard).getBankCard() + "");
                aLi.element("password").setText(db.getMap().get(bankCard).getPassword() + "");
                aLi.element("balance").setText(db.getMap().get(bankCard).getBalance() + "");
                aLi.element("idCardFreeze").setText(db.getMap().get(bankCard).getBankCardFreeze() + "");
            }
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        FileWriter fw = new FileWriter("xml/accounts.xml");
        XMLWriter writer = new XMLWriter(fw, format);
        writer.write(xmlDoc);
        writer.close();
    }

}
