package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAAnswer;
import romanow.abc.desktop.PRSExamPanel;

public class SAAnswerRetry2 extends SAExamBase {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        return "";
    }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.calcRatingBall();
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {
        ((SAAnswer)env).setRating(0);
        }
    }
