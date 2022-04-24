package romanow.abc.desktop;

public class UtilsEM {
    public static String[]  parseFIO(String fio){
        fio = fio.trim();
        String out[]={"","",""};
        int idx=fio.indexOf(" ");
        if(idx==-1){
            out[0]=fio;
            return out;
        }
        out[0]=fio.substring(0,idx);
        fio = fio.substring(idx+1);
        idx=fio.indexOf(" ");
        if(idx==-1){
            out[1]=fio;
            return out;
        }
        out[1]=fio.substring(0,idx);
        out[2]=fio.substring(idx+1);
        return out;
        }
    public static String formatSize(String ss, int size){
        String out = "";
        if (ss.length()<size)
            return ss;
        while (ss.length()>=size){
            int idx = ss.substring(0,size).lastIndexOf(" ");
            if (idx==-1){
                idx = ss.substring(size).indexOf(" ");
                if (idx==-1)
                    break;
                idx = size + idx;
                out += (out.length()!=0 ? "\n" : "")+ss.substring(0,idx);
                ss = ss.substring(idx+1);
                }
            else{
                out += (out.length()!=0 ? "\n" : "")+ss.substring(0,idx);
                ss = ss.substring(idx+1);
                }
            }
        out+="\n"+ss;
        return out;
    }

}
