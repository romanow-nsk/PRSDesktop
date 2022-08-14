package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMExamTakingStop extends EMClientEmpty {
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
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
