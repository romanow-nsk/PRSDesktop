package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSBasePanel;

public class EMExamTakingEndEdit implements I_ClientTransition {
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        return "";
        }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.refreshSelectedDiscipline(true);
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {
        }
}
