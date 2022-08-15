package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAAnswer;
import romanow.abc.desktop.PRSBasePanel;

public class EMAnswerRetry2 extends EMClientEmpty {
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        return "";
    }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.calcRatingBall();
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {
        ((SAAnswer)env).setRating(0);
        }
    }
