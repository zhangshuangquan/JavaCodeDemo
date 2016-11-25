package util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsq on 16/11/25.
 */
public class HttpClientUtils {
        /**
         * post方式提交表单（模拟用户登录请求）
         */
        public static String postForm(String url,Map<String, String> param) {
            String responseContent = null;
            // 创建默认的httpClient实例
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 创建httppost
            HttpPost httpPost = new HttpPost(url);
            // 创建参数队列
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            if (param != null) {
                Set<String> keys = param.keySet();
                for (String string : keys) {
                    formparams.add(new BasicNameValuePair(string, param.get(string)));
                }
            }
            UrlEncodedFormEntity uefEntity;
            try {
                uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
                httpPost.setEntity(uefEntity);
                CloseableHttpResponse response = httpClient.execute(httpPost);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        responseContent = EntityUtils.toString(entity, "UTF-8");
                    }
                } finally {
                    response.close();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭连接,释放资源
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return responseContent;
        }


        /**
         * 发送 get请求
         */
        public static String get(String url) {
            String responseContent = "{\"result\":false}";
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpclient.execute(httpGet);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        responseContent = EntityUtils.toString(entity);
                    }
                } finally {
                    response.close();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return responseContent;
        }
}
