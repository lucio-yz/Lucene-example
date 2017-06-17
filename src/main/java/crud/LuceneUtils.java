package crud;


import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * Author: Lucio.yang
 * Date: 17-6-16
 * 获取分词器和索引位置
 */
public class LuceneUtils {
    private static Logger logger=Logger.getLogger(LuceneUtils.class);
    private static Directory directory;
    private static Analyzer analyzer;
    static{
        try {
            directory= FSDirectory.open(Paths.get("./index/"));
            analyzer=new SmartChineseAnalyzer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Directory getDirectory(){
        return directory;
    }
    public static Analyzer getAnalyzer(){
        return analyzer;
    }
    public static void closeIndexWriter(IndexWriter indexWriter){
        if( indexWriter!=null ){
            try {
                indexWriter.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
