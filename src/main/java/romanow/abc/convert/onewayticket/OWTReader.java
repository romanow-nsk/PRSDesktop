package romanow.abc.convert.onewayticket;

import java.io.BufferedReader;
import java.io.IOException;

public class OWTReader {
    private boolean eof=false;
    private BufferedReader in;
    private String lastString = null;
    private String backString =null;
    public OWTReader(BufferedReader in0){
        in = in0;
        }
    public boolean eof(){ return eof; }
    public void pushBack(){
        backString = lastString;
        }
    public boolean isStartString(String ss){
        for (int i=0;i<ss.length();i++){
            char cc = ss.charAt(i);
            if (cc>='0' && cc<='9')
                continue;
            return  cc=='.';
            }
        return false;
    }
    public String readLine(){
        if (backString!=null){
            String ss = backString;
            lastString = backString;
            backString = null;
            return ss;
            }
        else {
            if (eof)
                return Main.delim;
            try {
                lastString = in.readLine();
                if (lastString==null){
                    eof=true;
                    in.close();
                    return Main.delim;
                    }
                return lastString;
                } catch (IOException e) {
                    eof = true;
                    return Main.delim;
                    }
            }
        }
    }
