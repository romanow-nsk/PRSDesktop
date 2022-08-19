package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.desktop.PRSExamPanel;

public class SAExamTakingTimeSet extends SAExamBase {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        if (((SAExamTaking)env).getStartTime().timeInMS()==0)
            return "Не установлено время начала";
        return "";
        }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline();
    }

    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {

    }
}
