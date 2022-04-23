package romanow.abc.convert.onewayticket;

import lombok.Getter;

import java.util.ArrayList;

public class OWTTheme extends ArrayList<OWTTask> {
    @Getter private  String question;
    @Getter private  String header="";
    public void clearSelected(){
        for(OWTTask question : this)
            question.selected=false;
        }
    public void read(OWTReader reader){
        question = reader.readLine();
        while (true){
            String ss = reader.readLine();
            if (ss.startsWith(Main.delim)){
                reader.pushBack();
                return;
                }
            if (ss.startsWith("$$$")){
                header = ss.substring(3);
                }
            if (reader.isStartString(ss)){
                OWTTask test = new OWTTask(ss);
                add(test);
                test.read(reader);
                }
        }
    }
    public void toLog(StringBuffer log,boolean full){
        log.append("Тема: "+question+"\n"+(header.length()==0 ? "" : header+"\n")+"Тестов:"+size()+"\n");
        if (!full)
            return;
        for(OWTTask qq : this)
            qq.toLog(log);
        }
}
