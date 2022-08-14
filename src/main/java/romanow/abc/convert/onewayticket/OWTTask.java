package romanow.abc.convert.onewayticket;

import romanow.abc.desktop.UtilsPRS;

import java.util.ArrayList;

public class OWTTask extends ArrayList<String> {
    public boolean selected=false;
    public OWTTask(String ss){
        add(ss);
        }
    public void read(OWTReader reader){
        while (true){
            String ss = reader.readLine();
            if (ss.startsWith(Main.delim)){
                reader.pushBack();
                return;
                }
            if (reader.isStartString(ss)){
                reader.pushBack();
                return;
                }
            add(ss);
            }
        }
    public String toString(){
        String out="";
        for(String ss : this){
            if (out.length()!=0)
                out+="\n";
            out+=ss;
            }
        return out;
        }
    public void toLog(StringBuffer log){
        log.append("---");
        for(String qq : this)
            log.append(UtilsPRS.formatSize(qq,60)+"\n");
        }

}
