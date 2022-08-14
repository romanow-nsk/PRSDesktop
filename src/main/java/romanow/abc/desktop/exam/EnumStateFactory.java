package romanow.abc.desktop.exam;

import java.util.ArrayList;
import java.util.HashMap;

public class EnumStateFactory<T> {
    public class EnumStatePair{
        public final T value;
        public final String name;
        public EnumStatePair(T value, String name) {
            this.value = value;
            this.name = name;
            }
        }
    ArrayList<EnumStatePair> list = new ArrayList<>();
    HashMap<T,String> namesMap = new HashMap<>();
    public EnumStateFactory(){}
    public ArrayList<EnumStatePair> getList() {
        return list;
        }
    public String []getNames() {
        String out[]=new String[list.size()];
        for(int i=0;i<out.length;i++)
            out[i]=list.get(i).name;
        return out;
        }
    public String getByValue(T value){
        String ss  = namesMap.get(value);
        return ss ==null ? "???" : ss;
        }
    public int getValueIdx(T value){
        for (int i=0;i<list.size();i++)
            if (list.get(i).value == value)
                return i;
        return -1;
    }
}
