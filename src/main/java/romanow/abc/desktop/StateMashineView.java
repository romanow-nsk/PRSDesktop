package romanow.abc.desktop;

import retrofit2.Call;
import romanow.abc.core.DBRequest;
import romanow.abc.core.entity.StateEntity;
import romanow.abc.core.entity.baseentityes.JEmpty;
import romanow.abc.core.entity.subjectarea.statemashine.Transition;
import romanow.abc.core.entity.subjectarea.statemashine.TransitionsFactory;
import romanow.abc.desktop.statemashine.I_ClientTransition;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StateMashineView {
    private final static int bHight=25;
    private final static int bWidth=150;
    private final int panelX0;
    private final int panelY0;
    private final EMExamAdminPanel panel;
    private final TransitionsFactory factory;
    private ArrayList<JButton> bList = new ArrayList<>();
    public StateMashineView(EMExamAdminPanel client0,int panelX0, int panelY0, TransitionsFactory factory0) {
        this.panelX0 = panelX0;
        this.panelY0 = panelY0;
        factory = factory0;
        panel = client0;
    }
    public void refresh(final StateEntity stateObject){
        for(JButton bb : bList)
            panel.remove(bb);
        bList.clear();
        if (stateObject==null)
            return;
        int state = stateObject.getState();
        ArrayList<Transition> tList = factory.getTransitionsForState(state);
        int n=0;
        for(int i=0;i<tList.size();i++){
            final Transition transition = tList.get(i);
            try {
                Class clazz= Class.forName("romanow.abc.desktop.statemashine."+factory.name+transition.transName);
                final I_ClientTransition transitionObject = (I_ClientTransition) clazz.newInstance();
                JButton bb = new JButton();
                bb.setText(transition.title);
                bb.setBounds(panelX0, panelY0+n*bHight+5,bWidth,bHight);
                n++;
                panel.add(bb);
                bList.add(bb);
                bb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String ss = transitionObject.testTransition(panel,stateObject);
                        if (ss.length()!=0){
                            panel.popup(ss);
                            System.out.println(ss);
                            return;
                            }
                        stateObject.setState(transition.nextState);         // Подготовить переход
                        new APICall<JEmpty>(panel.main) {
                            @Override
                            public Call<JEmpty> apiFun() {
                                return ((EMClient)panel.main).service2.execTransition(panel.main.debugToken,new DBRequest(stateObject,panel.main.gson));
                                }
                            @Override
                            public void onSucess(JEmpty oo) {
                                transitionObject.onTransition(panel,stateObject);
                                }
                            };
                        }
                    });
            } catch (Exception ee){
                System.out.println("Ошибка обработчика перехода "+factory.name+transition.transName+": "+ee.toString());
            }
        }
    }
}

