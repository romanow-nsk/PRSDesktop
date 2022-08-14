package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAAnswer;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMAnswerSetRating extends EMClientEmpty{
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
        return onlyInTaking(panel,env);
        }
    @Override
    public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env) {
        panel.calcRatingBall();
        }
    @Override
    public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env) {
        ((SAAnswer)env).setRating(Integer.parseInt(panel.getAnswerBall().getSelectedItem()));
        }
    }
