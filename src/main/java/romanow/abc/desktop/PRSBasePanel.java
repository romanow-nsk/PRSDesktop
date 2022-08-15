package romanow.abc.desktop;

import romanow.abc.core.entity.subjectarea.*;

import java.awt.*;

public abstract class PRSBasePanel extends BasePanel{
    public abstract void refreshSelectedDiscipline(boolean withPos);
    public void refreshSelectedDiscipline(){ refreshSelectedDiscipline(true); }
    public abstract void refreshSelectedStudRating();
    public abstract void refreshStudRatingFull(boolean withPos);
    public abstract SAStudRating getCStudRating();
    public abstract SADiscipline getCDiscipline();
    public abstract void calcRatingBall();
    public abstract SAExamTaking getCTaking();
    public abstract SAGroupRating getCRating();
    public abstract Choice getAnswerBall();
}
