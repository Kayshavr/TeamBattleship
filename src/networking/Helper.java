package networking;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    private SimpleDateFormat tformatter = new SimpleDateFormat("[HH:mm:ss]");
    public String getCurrentTimestamp()
    {
        Date date = new Date(System.currentTimeMillis());
        String timestamp = this.tformatter.format(date);

        return timestamp;
    }
}
