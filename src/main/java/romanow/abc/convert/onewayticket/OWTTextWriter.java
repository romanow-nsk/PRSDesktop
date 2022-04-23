package romanow.abc.convert.onewayticket;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class OWTTextWriter implements OWT_Writer {
    BufferedWriter out;
    @Override
    public void write(String ss) throws Exception {
        out.write(ss);
        out.newLine();
        }

    @Override
    public void open(String fname) throws Exception {
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), "Windows-1251"));
        }
    @Override
    public void close() throws Exception {
        out.flush();
        out.close();
        }

    @Override
    public void space() throws Exception {
        out.newLine();
    }
}
