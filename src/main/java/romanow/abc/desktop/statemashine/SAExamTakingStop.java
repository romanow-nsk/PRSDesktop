package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamPanel;

public class SAExamTakingStop extends SAExamBase {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
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
