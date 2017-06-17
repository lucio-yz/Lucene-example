package crud;

/**
 * Author: Lucio.yang
 * Date: 17-6-16
 * 文章实体类
 */
public class Article {
    private Integer id;
    private String title;
    private String content;

    public Article(Integer id,String title,String content){
        this.id=id;
        this.title=title;
        this.content=content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
