package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.EMAnswer;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMAnswerSetRating extends EMClientEmpty{
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        return onlyInTaking(panel,env);
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.calcRatingBall();
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {
        ((EMAnswer)env).setRating(Integer.parseInt(panel.getAnswerBall().getSelectedItem()));
        }
    }
