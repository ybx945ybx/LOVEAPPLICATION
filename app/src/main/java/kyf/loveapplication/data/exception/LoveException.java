package kyf.loveapplication.data.exception;

/**
 * Created by a55 on 2017/10/31.
 */

public class LoveException extends Exception{
    public int code;
    public String msg;

    public static LoveException createLoveException(int code, String msg) {
        LoveException ex = new LoveException(code, msg);
        ex.code = code;
        ex.msg = msg;
        return ex;
    }

    public LoveException(int code, String msg) {
        super(code + "###" + msg);
    }

}
