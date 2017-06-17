package crud;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lucio.yang
 * Date: 17-6-17
 */
public class IndexDao {
    private static Logger logger=Logger.getLogger(IndexDao.class);

    public void save(Article article){
        logger.info("Save:"+article.getId());
        Document document=DocumentUtils.article2Document(article);
        IndexWriter indexWriter=null;
        try {
            IndexWriterConfig config=new IndexWriterConfig(LuceneUtils.getAnalyzer());
            indexWriter=new IndexWriter(LuceneUtils.getDirectory(),config);
            indexWriter.addDocument(document);
        }catch (Exception e){
            logger.error("IndexDao.save error ",e);
        }finally {
            LuceneUtils.closeIndexWriter(indexWriter);
        }
    }

    public QueryResult search(String queryString,int begin,int end){
        logger.info("Search:"+queryString+",from "+begin+" to "+end);
        try {
            DirectoryReader ireader=DirectoryReader.open(LuceneUtils.getDirectory());
            IndexSearcher isearcher=new IndexSearcher(ireader);

            String[] fields={"title","content"};
            // 在全部的域上进行检索
            QueryParser parser=new MultiFieldQueryParser(fields,LuceneUtils.getAnalyzer());
            Query query= parser.parse(queryString);

            TopDocs topDocs=isearcher.search(query,end-begin+1);
            int count=topDocs.totalHits;
            logger.info("#total hits="+count);
            ScoreDoc[] hits=topDocs.scoreDocs;

            // 高亮显示查询项
            Formatter formatter=new SimpleHTMLFormatter("<font color='red'>","</font>");
            Scorer source=new QueryScorer(query);
            Highlighter highlighter=new Highlighter(formatter,source);

            // 处理结果
            end=Math.min(end-begin+1,hits.length)-1;
            List<Article> list=new ArrayList<>();
            for( int i=begin;i<=end;i++ ){
                Document hitDoc=isearcher.doc(hits[i].doc);
                Article article=DocumentUtils.document2Article(hitDoc);

                // 摘要
                // 提取检索关键字出现频率最高的一段文字作为摘要，默认情况下提取100个字符;同时加上自定义的高亮显示代码，可实现关键字高亮显示。
                String abstraction=highlighter.getBestFragment(LuceneUtils.getAnalyzer(),"content",hitDoc.get("content"));
                System.out.println("#"+abstraction);
                if( abstraction!=null ){
                    article.setContent(abstraction);
                }
                list.add(article);
            }
            ireader.close();
            return new QueryResult(count,list);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("IndexDao.search error ",e);
        }
        return null;
    }

    public void delete(String id){
        logger.info("Delete:"+id);
        IndexWriter indexWriter=null;
        try {
            IndexWriterConfig config=new IndexWriterConfig(LuceneUtils.getAnalyzer());
            indexWriter=new IndexWriter(LuceneUtils.getDirectory(),config);
            indexWriter.deleteDocuments(new Term("id",id));
        }catch (Exception e){
            logger.error("IndexDao.delete error ",e);
        }finally {
            LuceneUtils.closeIndexWriter(indexWriter);
        }
    }

    public void update(Article article){
        logger.info("Update:"+article.getId());
        Document document=DocumentUtils.article2Document(article);
        IndexWriter indexWriter=null;
        try {
            IndexWriterConfig config=new IndexWriterConfig(LuceneUtils.getAnalyzer());
            indexWriter=new IndexWriter(LuceneUtils.getDirectory(),config);
            indexWriter.updateDocument(new Term("id",article.getId().toString()),document);// 先删除，后创建

        }catch (Exception e){
            logger.error("IndexDao.update error",e);
        }finally {
            LuceneUtils.closeIndexWriter(indexWriter);
        }
    }

}
