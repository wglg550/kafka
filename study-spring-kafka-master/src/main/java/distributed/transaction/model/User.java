package distributed.transaction.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dean
 */
public class User implements Serializable {
    private Long id;
    private String name;
    private Date createTime;

    private Long rootOrgId;
    private Long courseId;
    private Long lessonId;
    private int type;

    public Long getRootOrgId() {
        return rootOrgId;
    }

    public void setRootOrgId(Long rootOrgId) {
        this.rootOrgId = rootOrgId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User(String name) {
        this.name = name;
        this.createTime = new Date();
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
