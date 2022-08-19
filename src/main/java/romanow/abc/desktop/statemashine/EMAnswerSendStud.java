package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.desktop.PRSExamPanel;

public class EMAnswerSendStud implements I_ClientTransition<PRSExamPanel> {
    @Override
    public String testTransition(PRSExamPanel panel, StateEntity env) {
        if (panel.getCStudRating().getState()!=Values.StudRatingOnExam)
            return "Отправка ответов закончена студентом";
        long oid = panel.getCStudRating().getSAExamTaking().getOid();
        SAExamTaking taking = panel.getCDiscipline().getTakings().getById(oid);
        if (taking.getState()!=Values.TakingInProcess)
            return "Отправка ответов закончена преподавателем";
        return "";
        }
    @Override
    public void onTransitionAfter(PRSExamPanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(PRSExamPanel panel, StateEntity env) {

    }
}
