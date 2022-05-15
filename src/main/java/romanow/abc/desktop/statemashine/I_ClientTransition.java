package romanow.abc.desktop.statemashine;

import romanow.abc.core.entity.StateEntity;
import romanow.abc.desktop.EMExamAdminPanel;

public interface I_ClientTransition{
public String testTransition(EMExamAdminPanel panel, StateEntity env);
public void onTransition(EMExamAdminPanel panel, StateEntity env);
}
