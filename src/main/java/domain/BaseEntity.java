package domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

import static javax.persistence.FetchType.*;

/**
 * Created by zsq on 16/12/2.
 */
public class BaseEntity {

    @Column(name = "update_time", insertable = false, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime",
            parameters = {@Parameter(name = "databaseZone", value = "jvm"),
                    @Parameter(name = "javaZone", value = "jvm")})
    private DateTime update_time;

    @Column(name = "create_time", insertable = true, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime",
            parameters = {@Parameter(name = "databaseZone", value = "jvm"),
                    @Parameter(name = "javaZone", value = "jvm")})
    private DateTime create_time;

    public DateTime getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(DateTime update_time) {
        this.update_time = update_time;
    }

    public DateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(DateTime create_time) {
        if (this.create_time == null) {
            this.create_time = create_time;
        }
    }

    @PrePersist
    void onCreate() {
        this.setCreate_time(new DateTime((new Date()).getTime()));
    }

    @PreUpdate
    void onPersist() {
        this.setUpdate_time(new DateTime((new Date()).getTime()));
    }
}
