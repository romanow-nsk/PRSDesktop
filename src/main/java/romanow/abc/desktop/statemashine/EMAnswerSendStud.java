package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.desktop.PRSBasePanel;

public class EMAnswerSendStud implements I_ClientTransition {
    @Override
    public String testTransition(PRSBasePanel panel, StateEntity env) {
        if (panel.getCStudRating().getState()!=Values.StudRatingOnExam)
            return "Отправка ответов закончена студентом";
        long oid = panel.getCStudRating().getSAExamTaking().getOid();
        SAExamTaking taking = panel.getCDiscipline().getTakings().getById(oid);
        if (taking.getState()!=Values.TakingInProcess)
            return "Отправка ответов закончена преподавателем";
        return "";
        }
    @Override
    public void onTransitionAfter(PRSBasePanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(PRSBasePanel panel, StateEntity env) {

    }
}
