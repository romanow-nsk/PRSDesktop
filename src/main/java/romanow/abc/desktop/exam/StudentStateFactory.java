package romanow.abc.desktop.exam;

import romanow.abc.exam.model.StudentBean;
import romanow.abc.exam.model.StudentRatingBean;

public class StudentStateFactory extends EnumStateFactory<StudentBean.StatusEnum> {
    public StudentStateFactory(){
        list.add(new EnumStatePair(StudentBean.StatusEnum.ACTIVE,"Учится"));
        list.add(new EnumStatePair(StudentBean.StatusEnum.EXPELLED,"Отчислен"));
        list.add(new EnumStatePair(StudentBean.StatusEnum.ACADEMIC_LEAVE,"Акад.отпуск"));
        for(EnumStatePair pair : list)
            namesMap.put(pair.value,pair.name);
            }
    }
