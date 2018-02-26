package kyf.loveapplication.data.net;

/**
 * Created by a55 on 2017/11/2.
 */

public class ApiModel<T> {
    public String description;

    public boolean isSuccess;

    public T data;

    public T getData() {
        return data;
    }
}
