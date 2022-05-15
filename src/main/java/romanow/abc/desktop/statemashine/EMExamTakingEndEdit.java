package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.subjectarea.I_State;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMExamTakingEndEdit implements I_ClientTransition {
    @Override
    public boolean testTransition(EMExamAdminPanel panel, I_State env) {
        return true;
        }
    @Override
    public void onTransition(EMExamAdminPanel panel,I_State env) {
        panel.refreshSelectedDiscipline();
        }
}
