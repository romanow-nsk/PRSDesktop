package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.subjectarea.I_State;
import romanow.abc.desktop.EMExamAdminPanel;

public interface I_ClientTransition{
public String testTransition(EMExamAdminPanel panel, I_State env);
public void onTransition(EMExamAdminPanel panel, I_State env);
}
