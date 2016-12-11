package service;

import org.apache.log4j.Logger;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zsq on 16/12/11.
 */
@Component
public class BaseService {

    static Logger logger = Logger.getLogger("baseLogger");

    private static final String HOST = "http://route.showapi.com/66-22";
    private static final int    CONNECT_TIMEOUT = 10 * 1000;                    //10 seconds
    private static final int    READ_TIMEOUT    = 1 * 60 * 1000;                //1 minutes

    private RestTemplate restTemplate;


    /*@Autowired
    private RestTemplate restTemplate;*/

    /**
     * restTemplate是spring的一个基于rest架构的用来发送http client请求的类
     */
    public BaseService() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        this.restTemplate = new RestTemplate(requestFactory);
    }
}
