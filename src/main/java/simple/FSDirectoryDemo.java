package simple;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Author: Lucio.yang
 * Date: 17-6-16
 */
public class FSDirectoryDemo {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("*****************检索开始**********************");


        // 创建一个内存目录对象，所以这里生成的索引不会放在磁盘中，而是在内存中。
        String indexPath="./index/";
        Directory directory= FSDirectory.open(Paths.get(indexPath));
        // 分词器对象为StandardAnalyzer
        IndexWriterConfig writerConfig = new IndexWriterConfig(new StandardAnalyzer());
        // 创建索引写入对象，该对象既可以把索引写入到磁盘中也可以写入到内存中。
        // directory为目录对象
        // writerConfig为写入控制
        IndexWriter writer = new IndexWriter(directory, writerConfig);
        // 创建文档对象，在lucene中创建的索引可以看成数据库中的一张表，表中也可以有字段,往里面添加内容之后可以根据字段去匹配查询
        // 下面创建的doc对象中添加了三个字段，分别为name,sex,dosomething,
        Document doc = new Document();
        // TextField.TYPE_STORED:存储字段值
        doc.add(new Field("name", "lin zhengle", TextField.TYPE_STORED));
        doc.add(new Field("address", "中国上海", TextField.TYPE_STORED));
        doc.add(new Field("dosometing", "I am learning lucene ", TextField.TYPE_STORED));
        // doc添加到索引
        writer.addDocument(doc);
        writer.close(); // 这里可以提前关闭，因为directory 写入内存之后 与IndexWriter 没有任何关系了


        // 因为索引放在内存中，所以存放进去之后要立马测试，否则，关闭应用程序之后就检索不到了
        // 创建IndexSearcher 检索索引的对象，里面要传递上面写入的内存目录对象directory
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(ireader);
        // 根据搜索关键字 封装一个term组合对象，然后封装成Query查询对象
        // dosometing是上面定义的字段，lucene是检索的关键字
        Query query = new TermQuery(new Term("dosometing", "lucene"));
        // Query query = new TermQuery(new Term("address", "中国上海"));
        // Query query = new TermQuery(new Term("name", "cheng"));

        // 去索引目录中查询，返回的是TopDocs对象，里面存放的就是上面放的document文档对象
        TopDocs rs = searcher.search(query, 100);
        long endTime = System.currentTimeMillis();
        System.out.println("总共花费" + (endTime - startTime) + "毫秒，检索到" + rs.totalHits + "条记录。");
        for (int i = 0; i < rs.scoreDocs.length; i++) {
            // rs.scoreDocs[i].doc 是获取索引中的标志位id, 从0开始记录
            Document firstHit = searcher.doc(rs.scoreDocs[i].doc);
            System.out.println("name:" + firstHit.getField("name").stringValue());
            System.out.println("address:" + firstHit.getField("address").stringValue());
            System.out.println("dosomething:" + firstHit.getField("dosometing").stringValue());
        }

        writer.close();
        directory.close();
        System.out.println("*****************检索结束**********************");

    }
}
