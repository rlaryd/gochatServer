import java.io.Serializable;

public class ChatVO implements Serializable {

    private static final long serialVersionUID = 1234567890L;

    private int idx;
    private String id;
    private String content;

    public int getIdx() {
        return idx;
    }
    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "chatVO [idx=" + idx + ", id=" + id + ", content=" + content + "]";
    }
}
