package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMAnswerStart extends EMClientEmpty {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        return onlyInTaking(panel,env);
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {
    }
}
