package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.EMExamTaking;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMExamTakingTimeSet implements I_ClientTransition{
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        if (((EMExamTaking)env).getStartTime().timeInMS()==0)
            return "Не установлено время начала";
        return "";
    }
    @Override
    public void onTransition(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline();
    }
    }
