package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.PRSExamPanel;

public abstract class EMClientEmpty implements I_ClientTransition<PRSExamPanel>{
    public boolean onlyForTakingState(int state, PRSExamPanel panel, StateEntity env){
        SAStudRating rating = panel.getCStudRating();
        SAExamTaking taking = panel.getCDiscipline().getTakings().getById(rating.getSAExamTaking().getOid());
        return taking.getState()==state;
        }
    public String onlyInTaking(PRSExamPanel panel, StateEntity env){
        boolean bb = onlyForTakingState(Values.TakingAnswerCheck,panel,env);
        boolean bb2 = onlyForTakingState(Values.TakingInProcess,panel,env);
        return bb | bb2 ? "" : "Проверка только на экзамене";
        }
}
