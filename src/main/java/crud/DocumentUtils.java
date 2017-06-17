package crud;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

/**
 * Author: Lucio.yang
 * Date: 17-6-16
 * 文章实体类和Document的转换工具类
 */
public class DocumentUtils {
    public static Document article2Document(Article article){
        Document document=new Document();
        document.add(new Field("id",article.getId().toString(), TextField.TYPE_STORED));
        document.add(new Field("title",article.getTitle(),TextField.TYPE_STORED));
        document.add(new Field("content",article.getContent(),TextField.TYPE_STORED));
        return document;
    }
    public static Article document2Article(Document document){
        return new Article(Integer.parseInt(document.get("id")),document.get("title"),document.get("content"));
    }
}
