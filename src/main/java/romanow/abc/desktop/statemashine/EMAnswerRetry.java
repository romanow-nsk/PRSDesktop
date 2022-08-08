package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAAnswer;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMAnswerRetry extends EMClientEmpty {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        return "";
    }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.calcRatingBall();
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {
        ((SAAnswer)env).setRating(0);
        }
    }
