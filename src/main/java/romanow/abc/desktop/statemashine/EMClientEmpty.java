package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.subjectarea.I_State;
import romanow.abc.desktop.EMClient;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMClientEmpty implements I_ClientTransition{
    @Override
    public String testTransition(EMExamAdminPanel panel, I_State env) {
        return "";
    }
    @Override
    public void onTransition(EMExamAdminPanel panel,I_State env) {
        panel.refreshSelectedDiscipline();
    }
    }
