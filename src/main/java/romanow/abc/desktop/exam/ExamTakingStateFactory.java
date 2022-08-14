package romanow.abc.desktop.exam;

import romanow.abc.vkr.exam.model.ExamBean;

public class ExamTakingStateFactory extends EnumStateFactory<ExamBean.StateEnum> {
    public ExamTakingStateFactory(){
        super();
        list.add(new EnumStatePair(ExamBean.StateEnum.REDACTION,"Редактируется"));
        list.add(new EnumStatePair(ExamBean.StateEnum.READY,"Допуски"));
        list.add(new EnumStatePair(ExamBean.StateEnum.TIME_SET,"Назначена сдача"));
        list.add(new EnumStatePair(ExamBean.StateEnum.PROGRESS,"Идет экзамен"));
        list.add(new EnumStatePair(ExamBean.StateEnum.FINISHED,"Экзамен закончен"));
        list.add(new EnumStatePair(ExamBean.StateEnum.CLOSED,"Оценки выставлены"));
        for(EnumStatePair pair : list)
            namesMap.put(pair.value,pair.name);
        }
}
