package romanow.abc.desktop.exam;

import romanow.abc.exam.model.AnswerBean;
import romanow.abc.exam.model.ExamBean;

import javax.enterprise.inject.spi.Bean;

public class AnswerStateFactory extends EnumStateFactory<AnswerBean.StateEnum> {
    public AnswerStateFactory(){
        super();
        list.add(new EnumStatePair(AnswerBean.StateEnum.NO_ANSWER,"Нет ответа"));
        list.add(new EnumStatePair(AnswerBean.StateEnum.IN_PROGRESS,"Отвечает"));
        list.add(new EnumStatePair(AnswerBean.StateEnum.SENT,"Ответ отправлен"));
        list.add(new EnumStatePair(AnswerBean.StateEnum.CHECKING,"Проверка"));
        list.add(new EnumStatePair(AnswerBean.StateEnum.RATED,"Оценен"));
        list.add(new EnumStatePair(AnswerBean.StateEnum.NO_RATING,"Без оценки"));
        for(EnumStatePair pair : list)
            namesMap.put(pair.value,pair.name);
        }
}
