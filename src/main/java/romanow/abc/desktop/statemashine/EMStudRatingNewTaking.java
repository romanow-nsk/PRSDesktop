package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.PRSBasePanel;

public class EMStudRatingNewTaking implements I_ClientTransition{
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        SAStudRating studRating = (SAStudRating)env;
        // TODO - условия пересдачи
        return "";
        }

    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {

        }
}

