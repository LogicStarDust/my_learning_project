package se.solr;



import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.richinfo.se.help.ResourcesUtil;
import scala.Tuple2;
import scala.Tuple3;

import java.io.IOException;
import java.util.*;

/**
 * @user 刘宪领
 */
public class SolrOperate {

    /**
     *
     * @param reqVal 请求的标签
     * @param nums  需要商品的个数
     * @return String
     */
    public static String query(String reqVal,int nums) {
         SolrServer server = new HttpSolrServer(ResourcesUtil.getVal("item_index"));//new HttpSolrServer("http://192.168.1.107:8084/solr/item_index/");//
        try {
            SolrQuery  params = new SolrQuery();
            params.add("q","content:"+reqVal);
            params.add("rows",String.valueOf(nums));
            params.add("f1","item_id");
            QueryResponse qres = server.query(params);
            SolrDocumentList sdlist = qres.getResults();
            StringBuffer sb = new StringBuffer();
            for(SolrDocument sd : sdlist){
                sb.append(String.valueOf(sd.getFieldValue("item_id"))).append(",") ;
            }
            return StringUtils.substringBeforeLast(sb.toString(),",");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *
     * @param reqVal 请求的goods_id
     * @return String
     */
    public static String query(String reqVal) {
        SolrServer server = new HttpSolrServer(ResourcesUtil.getVal("item_data"));//new HttpSolrServer("http://192.168.1.107:8084/solr/item_index/");//
        try {
            SolrQuery  params = new SolrQuery();
            params.add("q","data_id:"+reqVal);
            params.add("f1","item_id");
            QueryResponse qres = server.query(params);
            SolrDocumentList sdlist = qres.getResults();
            if(sdlist==null||sdlist.size()==0){
                return null;
            }
            String result = String.valueOf(sdlist.get(0).getFieldValue("item_id"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *@param type 类型
     * @return String
     */
    public static List<Tuple2<String, String>> queryAll(String type) {
        SolrServer server = new HttpSolrServer(ResourcesUtil.getVal("item_data"));//new HttpSolrServer("http://192.168.1.107:8084/solr/item_index/");//
        try {
            SolrQuery  params = new SolrQuery();
            params.add("q","source:"+type);
            params.add("f1","data_id,item_id");
            QueryResponse qres = server.query(params);
            SolrDocumentList sdlist = qres.getResults();
            String result = String.valueOf(sdlist.getNumFound());

            params.add("start","0");
            params.add("rows",result);
            List<Tuple2<String, String>> list = getAll(params,server);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Tuple2<String, String>> getAll(SolrQuery query, SolrServer server) {
        try {
            QueryResponse qres = server.query(query);
            SolrDocumentList sdlist = qres.getResults();
            List<Tuple2<String, String>> list = new ArrayList<>();
            for(SolrDocument sd : sdlist){
                Tuple2<String, String> t = new Tuple2<>(String.valueOf(sd.getFieldValue("data_id")),String.valueOf(sd.getFieldValue("item_id")));
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param reqVals 所有搜索词
     * @param nums 总返回商品个数
     * @return
     */
    public static List<Tuple3<String, String, Double>> queryMore(String reqVals, int nums) {
        if(StringUtils.isEmpty(reqVals)) return Collections.emptyList();
        SolrServer server = new HttpSolrServer(ResourcesUtil.getVal("item_index"));//new HttpSolrServer("http://192.168.1.107:8084/solr/item_index/");//
        List<Tuple3<String, String, Double>> list = new ArrayList<>();
        try {
            String[] vals = reqVals.split(",");
            int num = nums/vals.length;
            for(String reqVal: vals){
                SolrQuery  params = new SolrQuery();
                params.add("q","content:"+reqVals);
                params.add("rows",String.valueOf(num));
                params.add("f1","item_id");
                QueryResponse qres = server.query(params);
                SolrDocumentList sdlist = qres.getResults();

                for(SolrDocument sd : sdlist){
                    Tuple3<String,String,Double> t = new Tuple3<>(reqVal,String.valueOf(sd.getFieldValue("item_id")),1.0);
                    list.add(t);
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param list 增加缓存集合，map中为item_id,content 两个字段
     * @return 状态码 -1为失败
     */
    public static int addBatch(List<Map<String,String>> list){
        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        if(CollectionUtils.isEmpty(list)) return -1;
        for(Map<String,String> map :list){
            String item_id = map.get("item_id");
            if(StringUtils.isBlank(item_id)) continue;
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("item_id", map.get("item_id"));
            doc.addField("content", map.get("content"));
            docs.add(doc);
        }
        SolrServer server = new HttpSolrServer(ResourcesUtil.getVal("item_index"));
        UpdateRequest req = new UpdateRequest();
        req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
        req.add(docs);
        try {
            UpdateResponse rsp = req.process(server);
            int s = rsp.getStatus();
            return s;

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param value id值
     * @return
     */
    public static int delete(String value) {

        SolrServer server = new HttpSolrServer(ResourcesUtil.getVal("item_index"));
        UpdateRequest req = new UpdateRequest();
        req.setAction(UpdateRequest.ACTION.COMMIT, false, false);
        req.deleteById(value);
        try {
            UpdateResponse rsp = req.process(server);
            int s = rsp.getStatus();
            return s;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void main(String[] args){
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();



       //addBatch(list);
        queryAll("shop_data");

        System.out.println( query("中国移动",30));
    }
}
