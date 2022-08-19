package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamRating;
import romanow.abc.desktop.PRSExamPanel;

public class SAExamRatingTakingCancel implements I_ClientTransition<PRSExamPanel> {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        return "";
        }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
    }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {
        ((SAExamRating)env).getSAExamTaking().setOid(0);
        }
}
