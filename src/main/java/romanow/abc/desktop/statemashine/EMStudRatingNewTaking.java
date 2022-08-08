package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMStudRatingNewTaking implements I_ClientTransition{
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        SAStudRating studRating = (SAStudRating)env;
        // TODO - условия пересдачи
        return "";
        }

    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {

        }
}

