package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.core.entity.subjectarea.SAStudRating;
import romanow.abc.desktop.PRSExamAdminPanel;

public class EMStudRatingTakingSet implements I_ClientTransition {
    @Override
    public String testTransition(PRSExamAdminPanel panel, StateEntity env) {
        SAStudRating rating = (SAStudRating)env;
        SAExamTaking taking = panel.getCTaking();
        if (taking==null)
            return "Не выбран прием экзамена";
        if (taking.isOneGroup() && rating.getStudent().getRef().getSAGroup().getOid()!=taking.getGroup().getOid()){
            return "Прием экзамена для другой группы";
            }
        return "";
        }
    @Override
    public void onTransitionAfter(PRSExamAdminPanel panel, StateEntity env) {
        panel.refreshStudRatingFull(true);
    }
    @Override
    public void onTransitionBefore(PRSExamAdminPanel panel, StateEntity env) {
        SAStudRating rating = (SAStudRating)env;
        rating.getSAExamTaking().setOid(panel.getCTaking().getOid());
        }
}
