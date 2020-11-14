package com.zhu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientUI {
    private static Pattern p1 = Pattern.compile("[1-9]\\d{2,}");//输入的取款金额是非0开头且最少3位的数字
    //Public Content
    private Timer timer;
    private int pwdNo = 3;
    private JLabel timelable, envno, title, noticeLabel;
    Font textFont = new Font("Kokonor", Font.ITALIC, 25);
    Font titleFont = new Font("Kokonor", Font.ITALIC, 72);

    //ATM余额
    private static double balanceATM = 10000;
    //查看账户余额
    private String balance;
    //InsertCard
    private JPanel ICtitlePanel, ICnoticePanel, ICloginPanel;
    //ICusername - 银行卡号
    private JTextField ICusername;
    public static String ICusernamestr;
    private JButton ICLogin;
    private JPanel IPtitlePanel, IPnoticePanel, IPsubmitPanel;
    //IPuserpassword - 密码
    private JTextField IPuserpassword;
    private JButton IPsubmit;
    //MainMenu
    private JButton saving, withdrawal, select, transfer, back, HELP;
    //QueryUI
    private JLabel QUIshowBalance, QUIshowBalanceDATA;
    private JButton QUIback;
    //saveReceiptConfirm
    private JButton SRCsubmit, SRCback;
    //SaveUI
    private JPanel SUIuserInput;

    //SUIsaving - 存钱输入框金额
    private JTextField SUIsaving;
    private JButton SUIsubmit, SUIback;
    private JLabel SUIshowID, SUIshowBalance, SUIshowIDdata, SUIshowBalancedata;//SUIshouIDdata用于显示当前登录的卡号,SUIshowBalancedata用于显示当前登录卡号对应的余额
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    Date date = new Date();
    //TransferConfirmUI
    private JButton TCUIsubmit, TCUIback;
    //transferReceiptConfirm
    private JButton TRCsubmit, TRCback;
    //TransferUI
    private JLabel TUIshowID, TUIshowBalance, TUIshowIDdata, TUIshowBalancedata;
    public static String TUIToNStr;
    public static String TUItransCashStr;
    private JButton TUIsubmit, TUIback;
    JTextField TUItoN;
    JTextField TUItransCash;
    //withdrawReceiptConfirm
    private JButton WRCsubmit, WRCback;
    //WithDrawUI - 取钱输入框
    private JPanel WUIuserInput;
    //WUIwithDrawal  - 取钱输入框
    private JTextField WUIwithDrawal;
    private JButton WUIsubmit, WUIback;
    private JLabel WUIshowID, WUIshowBalance, WUIshowIDdata, WUIshowBalancedata;

    //初始化界面
    public ClientUI() throws IOException {
        InsertCard insertCard = new InsertCard();
        insertCard.Login();
    }

    public static void main(String[] args) throws IOException {
        new ClientUI();
    }

    //检验银行卡号界面
    public class InsertCard extends JFrame implements ActionListener {
        public void Login() {
            //主界面
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ICtitlePanel = new JPanel();
            ICtitlePanel.setBounds(390, 120, 500, 100);
            this.add(ICtitlePanel);
            title = new JLabel("中国ATM银行");
            ICtitlePanel.add(title);
            title.setFont(titleFont);
            title.setForeground(Color.red);
            ICnoticePanel = new JPanel();
            ICnoticePanel.setBounds(425, 230, 430, 60);
            this.add(ICnoticePanel);
            noticeLabel = new JLabel("请输入你的卡号");
            noticeLabel.setFont(textFont);
            ICnoticePanel.add(noticeLabel);
            noticeLabel.setForeground(Color.red);

            ICusername = new JTextField(32);
            ICusername.setBounds(440, 330, 400, 50);
            this.add(ICusername);
            ICusername.setFont(new Font("宋体", Font.ITALIC, 25));

            ICLogin = new JButton("登录");
            ICLogin.setFont(new Font("微软雅黑", Font.ITALIC, 24));
            ICLogin.addActionListener(this);
            ICloginPanel = new JPanel();
            ICloginPanel.setBounds(490, 390, 300, 50);
            this.add(ICloginPanel);
            ICloginPanel.add(ICLogin);

            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));

                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (ICusername.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "不能输入空账号");
            } else {
                Response response = null;
                try {
                    Client client = new Client();
                    response = client.sendIDCard(Integer.parseInt(ICusername.getText()));

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                if (response.getValue().equals(ResultCode.SUCCESS)) {
                    new InputPassword().PasswordValiditing();
                    this.dispose();
                } else if (response.getValue().equals(ResultCode.NO_CARDNO)) {
                    JOptionPane.showMessageDialog(this, "账号不存在");
                } else if (response.getValue().equals(ResultCode.CARD_FREEZE)) {
                    JOptionPane.showMessageDialog(this, "账号被冻结");
                }
            }
        }
    }

    //输入密码界面
    public class InputPassword extends JFrame implements ActionListener {
        public void PasswordValiditing() {
            //主界面
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            IPtitlePanel = new JPanel();
            IPtitlePanel.setBounds(390, 120, 500, 100);
            this.add(IPtitlePanel);
            title = new JLabel("中国ATM银行");
            IPtitlePanel.add(title);
            title.setFont(titleFont);
            title.setForeground(Color.red);

            IPnoticePanel = new JPanel();
            IPnoticePanel.setBounds(340, 230, 600, 60);
            this.add(IPnoticePanel);
            noticeLabel = new JLabel("现在请输入你的密码，输入密码时请注意遮挡。");
            IPnoticePanel.add(noticeLabel);
            noticeLabel.setFont(textFont);
            IPnoticePanel.add(noticeLabel);
            noticeLabel.setForeground(Color.red);

            IPuserpassword = new JPasswordField(32);
            IPuserpassword.setBounds(440, 330, 400, 50);
            this.add(IPuserpassword);
            IPuserpassword.setFont(new Font("宋体", Font.ITALIC, 25));

            IPsubmit = new JButton("确定");
            IPsubmit.addActionListener(this);
            IPsubmitPanel = new JPanel();
            IPsubmitPanel.setBounds(540, 400, 160, 80);
            IPsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(IPsubmitPanel);
            IPsubmitPanel.add(IPsubmit);

            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));
                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }

        //密码校验
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Client client = new Client();
                Response response = client.sendPWDandIDCard(Integer.parseInt(ICusername.getText() + ""), Integer.parseInt(IPuserpassword.getText() + ""), pwdNo);
                if (response.getValue().equals(ResultCode.SUCCESS)) {
                    new MainMenu().Menu();
                    this.dispose();
                } else if (response.getValue().equals(ResultCode.PWD_ERROR)) {
                    pwdNo--;
                    System.out.println(pwdNo);
                    JOptionPane.showMessageDialog(this, "密码错误，还剩" + pwdNo);
                } else if (response.getValue().equals(ResultCode.CARD_FREEZE)) {
                    JOptionPane.showMessageDialog(this, "密码输出次数过多，卡号已被冻结！");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //MainMenu UI
    public class MainMenu extends JFrame implements ActionListener {
        public void Menu() {
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            saving = new JButton("存款");
            withdrawal = new JButton("取款");
            transfer = new JButton("转账");
            select = new JButton("查询");
            back = new JButton("退出");
            HELP = new JButton("帮助");
            title = new JLabel("请选择你想执行的功能");
            title.setFont(titleFont);

            saving.setBounds(0, 100, 300, 100);
            withdrawal.setBounds(0, 250, 300, 100);
            select.setBounds(980, 100, 300, 100);
            transfer.setBounds(980, 250, 300, 100);
            back.setBounds(0, 400, 300, 100);
            HELP.setBounds(980, 400, 300, 100);
            title.setBounds(0, 0, 1280, 100);
            //将按钮添加到内容界面中
            this.add(saving);
            this.add(withdrawal);
            this.add(transfer);
            this.add(select);
            this.add(back);
            this.add(HELP);
            this.add(title);

            saving.addActionListener(this);
            withdrawal.addActionListener(this);
            select.addActionListener(this);
            transfer.addActionListener(this);
            back.addActionListener(this);
            HELP.addActionListener(this);

            saving.setFont(titleFont);
            withdrawal.setFont(titleFont);
            select.setFont(titleFont);
            transfer.setFont(titleFont);
            back.setFont(titleFont);
            HELP.setFont(titleFont);

            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));

                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 600, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);

            this.setVisible(true);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            Object choice = e.getSource();
            if (choice == saving) {  // 存钱
                this.dispose();
                new SaveUI().Save();
            } else if (choice == withdrawal) {  // 取钱
                this.dispose();

                new WithDrawUI().WithDrawal();
            } else if (choice == transfer) {  // 转账
                this.dispose();

                new TransferUI().Trnasfer();
            } else if (choice == select) { // 查看余额
                try {
                    Client client = new Client();
                    balance = client.checkAccount(Integer.parseInt(ICusername.getText() + "")).getValue() + "";
                    QUIshowBalanceDATA = new JLabel(balance);
                    this.dispose();
                    new QueryUI().query();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (choice == back) {  // 返回
                this.dispose();
                balance=null;
                new InsertCard().Login();
            } else if (choice == HELP) {  // 帮助
                JOptionPane.showMessageDialog(this, "客服小姐姐已经在快马加鞭赶来了，不要离开。");
            }
        }
    }

    //QueryUI - 显示查询余额
    public class QueryUI extends JFrame implements ActionListener {
        public void query() {
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            QUIshowBalance = new JLabel("您的账户余额为:");
            QUIshowBalance.setBounds(300, 200, 300, 50);
            QUIshowBalance.setFont(textFont);
            QUIshowBalanceDATA.setBounds(550, 150, 400, 150);
            QUIshowBalanceDATA.setFont(titleFont);
            QUIshowBalanceDATA.setForeground(Color.red);
            this.add(QUIshowBalance);
            this.add(QUIshowBalanceDATA);


            QUIback = new JButton("返回");
            QUIback.addActionListener(this);
            QUIback.setBounds(540, 400, 160, 80);
            QUIback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(QUIback);


            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));

                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            this.dispose();
            JOptionPane.showMessageDialog(this, "返回......");
            new MainMenu().Menu();

        }
    }

    //saveReceiptConfirm UI
    public class saveReceiptConfirm extends JFrame implements ActionListener {
        public void saveConfirm() {
            boolean flag = false;
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            noticeLabel = new JLabel("是否打印凭条？");
            noticeLabel.setFont(titleFont);
            noticeLabel.setBounds(300, 200, 400, 100);
            this.add(noticeLabel);
            SRCsubmit = new JButton("是");
            SRCsubmit.addActionListener(this);
            SRCsubmit.setBounds(460, 400, 160, 80);
            SRCsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(SRCsubmit);

            //返回按钮
            SRCback = new JButton("否");
            SRCback.addActionListener(this);
            SRCback.setBounds(660, 400, 160, 80);
            SRCback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(SRCback);


            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));
                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == SRCsubmit) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "凭条已打印");
                FileWriter fw = null;
                try {
                    fw = new FileWriter("SavingInfo.txt", false);
                    fw.write("用户:" + ICusername.getText() + "\n存款:" + SUIsaving.getText() + "\n" + "交易时间" + simpleDateFormat.format(date));
                    fw.flush();
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler SavingInfo.txt");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                new MainMenu().Menu();
            } else if (source == SRCback) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "返回......");
                new MainMenu().Menu();
            }
        }
    }

    //SaveUI UI
    public class SaveUI extends JFrame implements ActionListener {
        public void Save() {
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);

            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Font textFont = new Font("幼圆", Font.PLAIN, 24);

            SUIshowID = new JLabel("当前卡号:");
            SUIshowIDdata = new JLabel(ICusername.getText());
            SUIshowID.setFont(textFont);
            SUIshowID.setBounds(320, 50, 120, 50);
            SUIshowIDdata.setBounds(440, 50, 200, 50);
            SUIshowIDdata.setFont(new Font("幼圆", Font.PLAIN, 24));
            SUIshowIDdata.setForeground(Color.red);
            this.add(SUIshowID);
            this.add(SUIshowIDdata);
            //账户余额显示栏
            SUIshowBalance = new JLabel("当前余额:");
            SUIshowBalancedata = new JLabel(balance);
            SUIshowBalance.setFont(textFont);
            SUIshowBalance.setBounds(320, 120, 120, 50);
            SUIshowBalancedata.setFont(new Font("幼圆", Font.PLAIN, 24));
            SUIshowBalancedata.setForeground(Color.red);
            SUIshowBalancedata.setBounds(440, 120, 100, 50);
            this.add(SUIshowBalance);
            this.add(SUIshowBalancedata);

            SUIuserInput = new JPanel();
            SUIuserInput.setBounds(300, 200, 160, 60);
            JLabel hint = new JLabel("输入存款数额:");
            SUIuserInput.add(hint);
            hint.setFont(textFont);
            this.add(SUIuserInput);

            SUIsaving = new JTextField();
            SUIsaving.setBounds(460, 200, 400, 30);
            this.add(SUIsaving);
            SUIsaving.setFont(new Font("宋体", Font.ITALIC, 25));
            SUIsubmit = new JButton("存款");
            SUIsubmit.addActionListener(this);
            SUIsubmit.setBounds(460, 400, 160, 80);
            SUIsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(SUIsubmit);

            //返回按钮
            SUIback = new JButton("返回");
            SUIback.addActionListener(this);
            SUIback.setBounds(660, 400, 160, 80);
            SUIback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(SUIback);
            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));

                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);

        }


        //存钱
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == SUIsubmit) {
                try {
                    Matcher m1 = p1.matcher(SUIsaving.getText());
                    if (m1.matches()) {
                        //输入金额是一百的整数倍，不能超过一万
                        int i = (Integer.parseInt(SUIsaving.getText()));
                        if (i % 100 == 0 && i <= 10000) {  //判断数字金额是否符合要求
                            balanceATM += i;
                            Client client = new Client();
                            client.saveMoney(Integer.parseInt(ICusername.getText() + ""), i);
                            ///////
                            new saveReceiptConfirm().saveConfirm();

                            this.dispose();
                            ////////
                        } else {
                            JOptionPane.showMessageDialog(this, "取款金额必须是100的整数，且单次取款金额不得超过1万");
                        }
                    } else {
                        //输入的不是数字
                        JOptionPane.showMessageDialog(this, "请输入大于100整数！");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (source == SUIback) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "返回......");
                new MainMenu().Menu();
            }
        }
    }

    //TransferConfirmUI UI
    public class TransferConfirmUI extends JFrame implements ActionListener {

        public void confirm() {
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel notice = new JLabel("确认？");
            notice.setFont(titleFont);
            notice.setBounds(0, 0, 300, 50);
            this.add(notice);
            JLabel notice2 = new JLabel("请认真核对，转账后无法撤回");
            notice2.setFont(textFont);
            notice2.setBounds(0, 100, 350, 50);
            this.add(notice2);
            JLabel toname = new JLabel("转账目的地");
            JLabel tonamedata = new JLabel(TUItoN.getText());
            JLabel toCash = new JLabel("转账金额");
            JLabel tocashdata = new JLabel(TUItransCash.getText());

            toname.setBounds(0, 200, 160, 100);
            tonamedata.setBounds(200, 200, 200, 100);
            toname.setFont(textFont);
            tonamedata.setFont(textFont);
            tonamedata.setForeground(Color.RED);
            toCash.setBounds(0, 300, 100, 100);
            tocashdata.setBounds(200, 300, 200, 100);
            toCash.setFont(textFont);
            tocashdata.setForeground(Color.red);
            tocashdata.setFont(textFont);
            this.add(toname);
            this.add(tonamedata);
            this.add(toCash);
            this.add(tocashdata);

            TCUIsubmit = new JButton("转账");
            TCUIsubmit.addActionListener(this);
            TCUIsubmit.setBounds(460, 400, 160, 80);
            TCUIsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(TCUIsubmit);

            //返回按钮
            TCUIback = new JButton("返回");
            TCUIback.addActionListener(this);
            TCUIback.setBounds(660, 400, 160, 80);
            TCUIback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(TCUIback);


            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));

                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == TCUIsubmit) {
                new transferReceiptConfirm().transferConfirm();

                this.dispose();
            } else if (source == TCUIback) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "返回......");
                new MainMenu().Menu();
            }
        }
    }

    //transferReceiptConfirm UI
    public class transferReceiptConfirm extends JFrame implements ActionListener {
        public void transferConfirm() {
            boolean flag = false;
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            noticeLabel = new JLabel("是否打印凭条？");
            noticeLabel.setFont(titleFont);
            noticeLabel.setBounds(300, 200, 400, 100);
            this.add(noticeLabel);
            TRCsubmit = new JButton("是");
            TRCsubmit.addActionListener(this);
            TRCsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            TRCsubmit.setBounds(460, 400, 160, 80);
            this.add(TRCsubmit);

            //返回按钮
            TRCback = new JButton("否");
            TRCback.addActionListener(this);
            TRCback.setBounds(660, 400, 160, 80);
            TRCback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(TRCback);


            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));
                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == TRCsubmit) {
                this.dispose();
                FileWriter fw = null;
                try {
                    fw = new FileWriter("TransferInfo.txt", false);
                    fw.write("用户:" + ICusername.getText() + "\n目标账户" + TUItoN.getText() + "\n转账金额:" + TUItransCash.getText() + "\n" + "交易时间" + simpleDateFormat.format(date));
                    fw.flush();
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "凭条已打印");
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler TransferInfo.txt");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                new MainMenu().Menu();
            } else if (source == TRCback) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "返回......");
                new MainMenu().Menu();
            }
        }
    }

    //TransferUI UI
    public class TransferUI extends JFrame implements ActionListener {
        public void Trnasfer() {
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            TUIshowID = new JLabel("当前卡号");
            TUIshowIDdata = new JLabel(ICusername.getText());
            TUIshowID.setFont(textFont);
            TUIshowID.setBounds(320, 50, 400, 50);
            TUIshowIDdata.setBounds(440, 50, 200, 50);
            TUIshowIDdata.setFont(new Font("幼圆", Font.PLAIN, 24));
            TUIshowIDdata.setForeground(Color.red);
            this.add(TUIshowID);
            this.add(TUIshowIDdata);

            TUIshowBalance = new JLabel("当前余额"/*+Account.userid*/);
            TUIshowBalancedata = new JLabel(balance);

            TUIshowBalance.setFont(textFont);
            TUIshowBalancedata.setFont(new Font("幼圆", Font.PLAIN, 24));
            TUIshowBalancedata.setForeground(Color.red);
            TUIshowBalancedata.setBounds(440, 120, 100, 50);
            TUIshowBalance.setBounds(320, 120, 400, 50);
            this.add(TUIshowBalance);
            this.add(TUIshowBalancedata);

            JPanel userInput = new JPanel();
            userInput.setBounds(290, 200, 160, 60);
            JLabel hint = new JLabel("输入转账数额:");
            userInput.add(hint);
            hint.setFont(textFont);
            this.add(userInput);

            TUItransCash = new JTextField();
            TUItransCash.setBounds(460, 200, 400, 30);
            this.add(TUItransCash);
            TUItransCash.setFont(new Font("宋体", Font.ITALIC, 25));

            JPanel userCardInput = new JPanel();
            userCardInput.setBounds(290, 250, 160, 60);
            JLabel hint2 = new JLabel("输入对方卡号:");
            userCardInput.add(hint2);
            hint2.setFont(textFont);
            this.add(userCardInput);

            TUItoN = new JTextField();
            TUItoN.setBounds(460, 250, 400, 30);
            this.add(TUItoN);
            TUItoN.setFont(new Font("宋体", Font.ITALIC, 25));

            TUIsubmit = new JButton("转账");
            TUIsubmit.addActionListener(this);
            TUIsubmit.setBounds(460, 400, 160, 80);
            TUIsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(TUIsubmit);

            //返回按钮
            TUIback = new JButton("返回");
            TUIback.addActionListener(this);
            TUIback.setBounds(660, 400, 160, 80);
            TUIback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(TUIback);

            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));

                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }


        //转账
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == TUIsubmit) {
                try {
                    if (ICusername.getText().equals(TUItoN.getText())) {
                        JOptionPane.showMessageDialog(this, "不能转给自己");
                    } else {
                        Client client = new Client();
                        Response response = client.transferAccount(Integer.parseInt(ICusername.getText() + ""), Integer.parseInt(TUItoN.getText() + ""), Double.parseDouble(TUItransCash.getText() + ""));
                        if (response.getValue().equals(ResultCode.SUCCESS)) {
                            this.dispose();
                            new TransferConfirmUI().confirm();
                        } else if (response.getValue().equals(ResultCode.NSF_CHECK)) {
                            JOptionPane.showMessageDialog(this, "对不起，你目前的余额不足，请重新输入转账金额！");
                        } else if (response.getValue().equals(ResultCode.NO_CARDNO)) {
                            JOptionPane.showMessageDialog(this, "对不起，你要转的账户不存在，请重新输入转账账户！");
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (source == TUIback) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "返回......");
                new MainMenu().Menu();
            }

        }
    }

    //withdrawReceiptConfirm UI
    public class withdrawReceiptConfirm extends JFrame implements ActionListener {
        public void withdrawConfirm() {
            boolean flag = false;
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            noticeLabel = new JLabel("是否打印凭条？");
            noticeLabel.setFont(titleFont);
            noticeLabel.setBounds(300, 200, 400, 100);
            this.add(noticeLabel);
            WRCsubmit = new JButton("是");
            WRCsubmit.addActionListener(this);
            WRCsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            WRCsubmit.setBounds(460, 400, 160, 80);
            this.add(WRCsubmit);

            //返回按钮
            WRCback = new JButton("否");
            WRCback.addActionListener(this);
            WRCback.setBounds(660, 400, 160, 80);
            WRCback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(WRCback);


            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));
                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == WRCsubmit) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "凭条已打印");
                FileWriter fw = null;
                try {
                    fw = new FileWriter("WithdrawInfo.txt", false);
                    fw.write("用户:" + ICusername.getText() + "\n取款:\n" + WUIwithDrawal.getText() + "交易时间" + simpleDateFormat.format(date));
                    fw.flush();
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                this.dispose();
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler WithdrawInfo.txt");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                new MainMenu().Menu();
            } else if (source == WRCback) {
                this.dispose();
                JOptionPane.showMessageDialog(this, "返回......");
                new MainMenu().Menu();
            }

        }
    }

    //WithDrawUI UI
    public class WithDrawUI extends JFrame implements ActionListener {
        public void WithDrawal() {
            this.setTitle("ATM CLIENT HD");
            this.setSize(1280, 720);
            this.setLocation(500, 200);
            this.setLayout(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            WUIshowID = new JLabel("当前卡号");
            WUIshowIDdata = new JLabel(ICusername.getText());
            WUIshowID.setFont(textFont);
            WUIshowID.setBounds(320, 50, 400, 50);
            WUIshowIDdata.setBounds(440, 50, 200, 50);
            WUIshowIDdata.setFont(new Font("幼圆", Font.PLAIN, 24));
            WUIshowIDdata.setForeground(Color.red);
            this.add(WUIshowID);
            this.add(WUIshowIDdata);

            //账户余额显示栏
            WUIshowBalance = new JLabel("当前余额");
            WUIshowBalance.setFont(textFont);
            WUIshowBalance.setBounds(320, 120, 400, 50);
            WUIshowBalancedata = new JLabel(balance);
            WUIshowBalancedata.setFont(new Font("幼圆", Font.PLAIN, 24));
            WUIshowBalancedata.setForeground(Color.red);
            WUIshowBalancedata.setBounds(440, 120, 100, 50);
            this.add(WUIshowBalance);
            this.add(WUIshowBalancedata);

            WUIuserInput = new JPanel();
            WUIuserInput.setBounds(300, 200, 160, 60);
            JLabel hint = new JLabel("输入取款数额:");
            WUIuserInput.add(hint);
            hint.setFont(textFont);
            this.add(WUIuserInput);

            WUIwithDrawal = new JTextField();
            WUIwithDrawal.setBounds(460, 200, 400, 30);
            this.add(WUIwithDrawal);
            WUIwithDrawal.setFont(new Font("宋体", Font.ITALIC, 25));


            WUIsubmit = new JButton("取款");
            WUIsubmit.addActionListener(this);

            WUIsubmit.setBounds(460, 400, 160, 80);
            WUIsubmit.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(WUIsubmit);

            //返回按钮
            WUIback = new JButton("返回");
            WUIback.addActionListener(this);
            WUIback.setBounds(660, 400, 160, 80);
            WUIback.setFont(new Font("楷体", Font.ITALIC, 48));
            this.add(WUIback);


            timelable = new JLabel("");
            timelable.setBounds(1080, 650, 200, 30);
            timer = new Timer(1, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timelable.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
                            .format(new Date()));
                }
            });
            envno = new JLabel("为响应环保，我们将从近期停止提供纸质回执单关爱地球，从我做起");
            envno.setBounds(0, 650, 800, 30);
            envno.setFont(textFont);
            timer.start();
            this.add(timelable);
            this.add(envno);
            this.setVisible(true);
        }


        //取款
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == WUIsubmit) {
                try {
                    Matcher m1 = p1.matcher(WUIwithDrawal.getText() + "");
                    if (m1.matches() && (Double.parseDouble(WUIwithDrawal.getText() + "") % 100 == 0)) {
                        if (Integer.parseInt(WUIwithDrawal.getText()) > balanceATM) {
                            JOptionPane.showMessageDialog(this, "对不起，该ATM的余额不足，请重新输入取款金额！");
                        } else {
                            balanceATM -= Double.parseDouble(WUIwithDrawal.getText() + "");
                            System.out.println(balanceATM);
                            Client client = new Client();
                            Response response = client.takeMoney(Integer.parseInt(ICusername.getText()), Integer.parseInt(WUIwithDrawal.getText()));
                            if (response.getValue().equals(ResultCode.SUCCESS)) {
                                this.dispose();
                                new withdrawReceiptConfirm().withdrawConfirm();
                            } else if (response.getValue().equals(ResultCode.NSF_CHECK)) {
                                //余额不足
                                JOptionPane.showMessageDialog(this, "对不起，你的余额不足！");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "请输入不小于100的整数倍的取款金额！");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (source == WUIback) {
                JOptionPane.showMessageDialog(this, "返回......");
                this.dispose();
                new MainMenu().Menu();
            }
        }
    }


}