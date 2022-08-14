package romanow.abc.desktop.exam;

import romanow.abc.vkr.exam.model.StudentRatingBean;

public class StudentRatingStateFactory extends EnumStateFactory<StudentRatingBean.StudentRatingStateEnum> {
    public StudentRatingStateFactory(){
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.EMPTY,"Не определен"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.NOT_ALLOWED,"Не допущен"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.ALLOWED,"Допущен"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.ASSIGNED_TO_EXAM,"Назначен на сдачу"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.WAITING_TO_APPEAR,"Подтверждение явки"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.ABSENT,"Неявка"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.PASSING,"На экзамене"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.FINISHED,"Закончил сдачу"));
        list.add(new EnumStatePair(StudentRatingBean.StudentRatingStateEnum.RATED,"Получил оценку"));
        for(EnumStatePair pair : list)
            namesMap.put(pair.value,pair.name);
            }
    }
