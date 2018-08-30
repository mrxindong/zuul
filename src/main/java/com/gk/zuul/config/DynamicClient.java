package com.gk.zuul.config;

import com.gk.zuul.config.Model.ZuulRouteVO;
import com.netflix.client.ClientException;
import com.netflix.client.ClientFactory;
import com.netflix.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

/**
 * 动态生成 ribbon 客户端
 */
@Component
public class DynamicClient {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ConfigSequence sequenceConfig;

    @Value("${zuulversion}")
    String version;

    @Autowired
    ContextRefresher refresher;


    public void createRibbonClient(String serviceId, String LIST_SERVERS, String path) throws ClientException {

        //设置：配置项 (ribbon 会读取此配置)
        //根据 archaius 动态配置的特性 ， 服务列表更新时只需重新赋值即可
        ConfigurationManager.getConfigInstance().setProperty( "hiapi-v1.ribbon.listOfServers", LIST_SERVERS);

        // 获取客户端， 若不存在则创建
        ClientFactory.getNamedClient(serviceId);
    }

    /**
     * 批量添加配置
     *
     * @param serverList
     */
    public void addRibbonList(List<ZuulRouteVO> serverList) {
        try {
            for (ZuulRouteVO server : serverList) {
                createRibbonClient(server.getServiceId(), server.getServer_list(), server.getPath());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            logger.error("添加配置失败：" + new Date().toGMTString());
        }
    }

    /**
     * 筛选合适的路由
     * @param serverList
     */
    public void addRibbonListByStartId(List<ZuulRouteVO> serverList) {
        Integer thisSequence = sequenceConfig.getSequence();
        try {
            for (ZuulRouteVO server : serverList) {
                //版本号一致，并且实例Id大于路由控制id
                if (server.getVersion().equals(version) && thisSequence > server.getStart_id()) {
                    createRibbonClient(server.getServiceId(), server.getServer_list(), server.getPath());
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            logger.error("添加配置失败：" + new Date().toGMTString()+e.toString());
        }
    }
}