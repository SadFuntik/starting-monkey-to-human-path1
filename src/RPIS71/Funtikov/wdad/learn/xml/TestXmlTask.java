package RPIS71.Funtikov.wdad.learn.xml;

import java.time.LocalDate;

public class TestXmlTask {

    public static void main(String[] args) {
        XmlTask task = new XmlTask("src/RPIS71/Funtikov/wdad/learn/xml/poopnfork.xml");
        //task.removeDay(LocalDate.of(2019,10,11));
        String secName="sidorov";
        System.out.println(task.earningsTotal(secName,LocalDate.of(2019,10,9)));
        task.changeOfficiantName("Kazak","petrov","Kazak","ivanov");
    }
}
