package crud;

import java.util.List;

/**
 * Author: Lucio.yang
 * Date: 17-6-17
 * 结果集
 */
public class QueryResult {
    private int count;
    private List list;
    public QueryResult(){
        super();
    }
    public QueryResult(int count,List list){
        super();
        this.count=count;
        this.list=list;
    }
}
