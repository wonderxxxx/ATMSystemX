package com.zhu;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase_ParseXML {

    private Map<Integer,Account> map = new HashMap<>();

    public Map<Integer, Account> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Account> map) {
        this.map = map;
    }

    public DataBase_ParseXML(){
        File xmlFile = new File("xml/accounts.xml");
        SAXReader reader = new SAXReader();
        Document xmlDoc = null;
        try {
            xmlDoc = reader.read(xmlFile);
            //获取accounts.xml文件的根元素
            Element root = xmlDoc.getRootElement();
            //获取根元素下的所有子元素
            List<Element> aList = root.elements();
            for(Element e : aList){
                map.put(Integer.parseInt(e.elementText("bankCard")),new Account(
                        Integer.parseInt(e.elementText("id")),
                        e.elementText("name"),
                        Integer.valueOf(e.elementText("bankCard")),
                        Integer.valueOf(e.elementText("password")),
                        Double.parseDouble(e.elementText("balance")),
                        Integer.parseInt(e.elementText("idCardFreeze"))
                ));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
