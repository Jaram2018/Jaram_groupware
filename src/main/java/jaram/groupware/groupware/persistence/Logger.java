package jaram.groupware.groupware.persistence;

import jaram.groupware.groupware.model.value.log.Log;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NamHyunsil on 2018. 7. 3..
 */

@Entity
public class Logger {

    @Id
    public long id;

    @Embedded
    public List<Log> logList;

    public Logger(){}

    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
    }

    public Boolean writeLog(){
        return true;
    }
}
