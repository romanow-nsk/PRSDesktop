package romanow.abc.desktop;

import javax.swing.*;

public class UtilsDesktop {
    public static int setLabelText(JLabel label, String name){
        int cnt=0;
        String out="<html>";
        while(true) {
            cnt++;
            int idx = name.indexOf("$");
            if (idx == -1){
                out += "&nbsp; &nbsp;" + name + "</html>";
                break;
                }
            out+=  "&nbsp; &nbsp;" + name.substring(0, idx)+"<br>";
            name = name.substring(idx + 1);
            }
        label.setText(out);
        return cnt;
        }
    public static int setLabelText(JLabel label, String name, int size){
        int cnt=0;
        if (size==0){
            setLabelText(label,name);
            return 1;
            }
        String out="<html>";
        while(true) {
            cnt++;
            int idx = name.lastIndexOf(" ",size);
            if (name.length()<size) idx=-1;
            if (idx == -1){
                out += "&nbsp; &nbsp;" + name + "</html>";
                break;
                }
            out+=  "&nbsp; &nbsp;" + name.substring(0, idx)+"<br>";
            name = name.substring(idx + 1);
            }
        label.setText(out);
        return cnt;
        }
    public static void setButtonText(JButton label, String name, int size){
        if (name.length()<=size) {
            label.setText("<html>&nbsp; &nbsp;"+name+"</html>");
            return;
            }
        int idx = name.substring(0,size).lastIndexOf(" ");
        String name2;
        if (idx==-1)
            name2 = "<html>&nbsp; &nbsp;"+name+"</html>";
        else
            name2 = "<html> &nbsp; &nbsp;"+name.substring(0,idx)+"<br> &nbsp; &nbsp;"+name.substring(idx+1)+"</html>";
        label.setText(name2);
        }
}
