package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMAnswerStart extends EMClientEmpty {
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
        return onlyInTaking(panel,env);
        }
    @Override
    public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env) {
    }
}
