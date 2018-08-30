package com.gk.zuul.config;

import com.gk.zuul.config.Model.ZuulRouteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigSchedule {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DynamicClient dynamicClient;

    @Autowired
    RefreshableRouteLocator routeLocators;

    @Scheduled(fixedDelay = 10000,initialDelay = 6000)  //cron="0 15 1 ? * *"   每天早上1：15触发
    public void reportCurrentTime() {
        routeLocators.refresh();

        List<ZuulRouteVO> clientServers= MyProperties.getResults();
        dynamicClient.addRibbonListByStartId(clientServers);


//        MonitoringHelper.initMocks();
//        FilterLoader.getInstance().setCompiler(new GroovyCompiler());

       /* try {

//            FilterFileManager.setFilenameFilter(new GroovyFileFilter());
//            FilterFileManager.init(5, groovyPath + "pre", groovyPath + "post");

            DefaultFilterFactory filterFactory=new DefaultFilterFactory();
            filterFactory.newInstance(null);

            logger.info("groovy刷新完成");

        } catch (Exception e) {

            logger.info("groovy filter刷新出错：" + e.getMessage());
            throw new RuntimeException(e);

        }*/
    }

}
