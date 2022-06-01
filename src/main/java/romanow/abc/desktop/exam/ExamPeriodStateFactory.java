package romanow.abc.desktop.exam;

import romanow.abc.exam.model.ExamBean;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamPeriodStateFactory {
    public class EnumPeriodStatePair{
        public final ExamBean.StateEnum value;
        public final String name;
        public EnumPeriodStatePair(ExamBean.StateEnum value, String name) {
            this.value = value;
            this.name = name;
            }
        }
    private ArrayList<EnumPeriodStatePair> list = new ArrayList<>();
    private HashMap<ExamBean.StateEnum,String> namesMap = new HashMap<>();
    public ExamPeriodStateFactory(){
        list.add(new EnumPeriodStatePair(ExamBean.StateEnum.REDACTION,"Редактируется"));
        list.add(new EnumPeriodStatePair(ExamBean.StateEnum.READY,"Допуски"));
        list.add(new EnumPeriodStatePair(ExamBean.StateEnum.TIME_SET,"Назначена сдача"));
        list.add(new EnumPeriodStatePair(ExamBean.StateEnum.PROGRESS,"Идет экзамен"));
        list.add(new EnumPeriodStatePair(ExamBean.StateEnum.FINISHED,"Экзамен закончен"));
        list.add(new EnumPeriodStatePair(ExamBean.StateEnum.CLOSED,"Оценки выставлены"));
        for(EnumPeriodStatePair pair : list)
            namesMap.put(pair.value,pair.name);
        }
    public ArrayList<EnumPeriodStatePair> getList() {
        return list;
        }
    public String getByValue(ExamBean.StateEnum value){
        String ss  = namesMap.get(value);
        return ss ==null ? "???" : ss;
        }
    public int getValueIdx(ExamBean.StateEnum value){
        for (int i=0;i<list.size();i++)
            if (list.get(i).value == value)
                return i;
        return -1;
    }
}
