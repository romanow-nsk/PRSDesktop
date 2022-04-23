package romanow.abc.convert.onewayticket;

import lombok.Getter;

import java.util.ArrayList;

public class OWTDiscipline extends ArrayList<OWTTheme> {
    @Getter private String head;
    public void clearSelected(){
        for(OWTTheme question : this)
            question.clearSelected();
            }
    public void testAndClearSelected(){
        for(OWTTheme question : this)
            for(OWTTask test : question)
                if (!test.selected){
                    return;
                    }
        clearSelected();
        }
    public void read(OWTReader reader){
        head = reader.readLine();
        while (!reader.eof()){
            String ss = reader.readLine();
            if (ss.startsWith(Main.delim)){
                OWTTheme question = new OWTTheme();
                add(question);
                question.read(reader);
                }
            }
        }
    public void toLog(StringBuffer log, boolean full){
        log.append(head+"\nВопросов:"+size()+"\n");
        for(OWTTheme qq : this)
            qq.toLog(log,full);
        }
}
