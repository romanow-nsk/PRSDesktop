package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamPanel;

public class SAAnswerStart extends SAExamBase {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        return onlyInTaking(panel,env);
        }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {
    }
}
