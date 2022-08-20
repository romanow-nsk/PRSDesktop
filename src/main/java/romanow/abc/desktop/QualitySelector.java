package romanow.abc.desktop;

import romanow.abc.core.constants.ConstValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Comparator;

public class QualitySelector {
    private ArrayList<ConstValue> qualityList;
    private I_Value<Integer> back;
    private JPanel panel;
    private boolean busy=false;
    private ArrayList<Choice> choices = new ArrayList<>();
    public QualitySelector(ArrayList<ConstValue> list, JPanel panel0, int x0, int y0, I_Value<Integer> back0){
        panel = panel0;
        back = back0;
        qualityList = list;
        busy=true;
        for(int i=0;i<list.size();i++){
            ConstValue cc = qualityList.get(i);
            JLabel label = new JLabel(cc.title());
            label.setBounds(10,10+25*i,100,25);
            panel.add(label);
            Choice choice = new Choice();
            choice.setBounds(110,10+25*i,40,25);
            choice.add("...");
            choice.add("+");
            choice.add("-");
            panel.add(choice);
            choices.add(choice);
            choice.select(1);
            choice.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (busy)
                        return;
                    int vv=0, pp=1;
                    for(Choice cc : choices){
                        int ss=cc.getSelectedIndex();
                        vv+=ss*pp;
                        pp*=3;
                        }
                    back.onEnter(vv);
                    }
                });
            }
        busy=false;
        }
    public void setVisible(boolean mode){
        panel.setVisible(mode);
        }
    public void select(int value){
        busy=true;
        for(Choice cc : choices){
            int ss=value%3;
            cc.select(ss);
            value/=3;
            }
        busy=false;
        }

}
