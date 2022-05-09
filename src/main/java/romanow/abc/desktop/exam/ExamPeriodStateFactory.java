package romanow.abc.desktop.exam;

import romanow.abc.exam.model.ExamPeriodBean;

import java.util.ArrayList;
import java.util.HashMap;

public class ExamPeriodStateFactory {
    public class EnumPeriodStatePair{
        public final ExamPeriodBean.StateEnum value;
        public final String name;
        public EnumPeriodStatePair(ExamPeriodBean.StateEnum value, String name) {
            this.value = value;
            this.name = name;
            }
        }
    private ArrayList<EnumPeriodStatePair> list = new ArrayList<>();
    private HashMap<ExamPeriodBean.StateEnum,String> namesMap = new HashMap<>();
    public ExamPeriodStateFactory(){
        list.add(new EnumPeriodStatePair(ExamPeriodBean.StateEnum.REDACTION,"Редактируется"));
        list.add(new EnumPeriodStatePair(ExamPeriodBean.StateEnum.ALLOWANCE,"Допуски"));
        list.add(new EnumPeriodStatePair(ExamPeriodBean.StateEnum.READY,"Назначена сдача"));
        list.add(new EnumPeriodStatePair(ExamPeriodBean.StateEnum.PROGRESS,"Идет экзамен"));
        list.add(new EnumPeriodStatePair(ExamPeriodBean.StateEnum.FINISHED,"Экзамен закончен"));
        list.add(new EnumPeriodStatePair(ExamPeriodBean.StateEnum.CLOSED,"Оценки выставлены"));
        for(EnumPeriodStatePair pair : list)
            namesMap.put(pair.value,pair.name);
        }
    public ArrayList<EnumPeriodStatePair> getList() {
        return list;
        }
    public String getByValue(ExamPeriodBean.StateEnum value){
        String ss  = namesMap.get(value);
        return ss ==null ? "???" : ss;
        }
    public int getValueIdx(ExamPeriodBean.StateEnum value){
        for (int i=0;i<list.size();i++)
            if (list.get(i).value == value)
                return i;
        return -1;
    }
}
