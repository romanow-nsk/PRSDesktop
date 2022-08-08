package romanow.abc.desktop.statemashine;

import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.subjectarea.SAExamTaking;
import romanow.abc.desktop.EMExamAdminPanel;

public class EMAnswerSendStud implements I_ClientTransition {
    @Override
    public String testTransition(EMExamAdminPanel panel, StateEntity env) {
        if (panel.getCStudRating().getState()!=Values.StudRatingOnExam)
            return "Отправка ответов закончена студентом";
        long oid = panel.getCStudRating().getEMExamTaking().getOid();
        SAExamTaking taking = panel.getCDiscipline().getTakings().getById(oid);
        if (taking.getState()!=Values.TakingInProcess)
            return "Отправка ответов закончена преподавателем";
        return "";
        }
    @Override
    public void onTransitionAfter(EMExamAdminPanel panel, StateEntity env) {
        panel.refreshSelectedStudRating();
        }
    @Override
    public void onTransitionBefore(EMExamAdminPanel panel, StateEntity env) {

    }
}
