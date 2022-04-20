package romanow.abc.desktop;

import java.util.ArrayList;

public class ScreenModeFactory {
    private ScreenMode list[] = {
        new ScreenMode(),
        new ScreenMode("1024x768",768,1024),    //4:3
        new ScreenMode("1600x900",900,1600),    //4:3
        new ScreenMode("1280x1024",1024,1280),  //5:4
        };
    public ScreenModeFactory(){}
    public ScreenMode getByName(String name){
        for(ScreenMode ss : list)
            if (ss.mode.equals(name))
                return ss;
        return new ScreenMode();
        }
    public ArrayList<String> createList(){
        ArrayList<String> out = new ArrayList<>();
        for(ScreenMode ss : list)
            out.add(ss.mode);
        return out;
    }
}
