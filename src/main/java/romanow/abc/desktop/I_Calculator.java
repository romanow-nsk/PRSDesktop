package romanow.abc.desktop;

import romanow.abc.core.UniException;

public interface I_Calculator {
    public void parseAndWrite(String ss,boolean mode) throws UniException;
    public  String getTitle();
    public boolean isMinFormulaValid();
    public String getMinValue();
    public boolean isMaxFormulaValid();
    public  String getMaxValue();
}
