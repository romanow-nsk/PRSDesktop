package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMExamTakingTimeSet extends EMClientEmpty {
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
        if (((SAExamTaking)env).getStartTime().timeInMS()==0)
            return "Не установлено время начала";
        return "";
        }
    @Override
    public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline();
    }

    @Override
    public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env) {

    }
}
