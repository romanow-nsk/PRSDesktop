package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.EMStudRating;
import romanow.abc.dataserver.DataServer;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMStudRatingAllow implements I_ClientTransition{
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        long ruleId = panel.getCRating().getRule().getOid();
        int minBall = panel.getCDiscipline().getRules().getById(ruleId).getMinimalRating();
        return ((EMStudRating)env).getSemesterRating()>=minBall ? "" : "Недостаточно баллов для допуска";
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {
        }
}
