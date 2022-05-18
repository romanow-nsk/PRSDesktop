package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.EMExamTaking;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMExamTakingTimeSet extends EMClientEmpty {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        if (((EMExamTaking)env).getStartTime().timeInMS()==0)
            return "Не установлено время начала";
        return "";
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline();
    }

    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {

    }
}
