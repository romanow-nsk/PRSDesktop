package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.desktop.PRSBasePanel;

public class EMExamTakingTimeSet extends EMClientEmpty {
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        if (((SAExamTaking)env).getStartTime().timeInMS()==0)
            return "Не установлено время начала";
        return "";
        }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline();
    }

    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {

    }
}
