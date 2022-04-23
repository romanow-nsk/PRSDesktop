package romanow.abc.convert.onewayticket;

public interface OWT_Writer {
    public void write(String ss) throws Exception;
    public void open(String fname) throws Exception;
    public void close() throws Exception;
    public void space() throws Exception;
}
