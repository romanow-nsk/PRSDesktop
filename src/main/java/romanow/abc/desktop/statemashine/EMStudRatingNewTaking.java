package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.PRSExamPanel;

public class EMStudRatingNewTaking implements I_ClientTransition<PRSExamPanel>{
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        SAStudRating studRating = (SAStudRating)env;
        // TODO - условия пересдачи
        return "";
        }

    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {

        }
}

