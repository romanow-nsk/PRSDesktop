package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMStudRatingTakingCancel implements I_ClientTransition {
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
        return "";
        }
    @Override
    public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
    }
    @Override
    public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env) {
        ((SAStudRating)env).getEMExamTaking().setOid(0);
        }
}
