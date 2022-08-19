package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.PRSExamPanel;

public class EMExamTakingTimeCancel extends EMClientEmpty {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        return null;
    }

    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {

    }

    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {

    }
}
