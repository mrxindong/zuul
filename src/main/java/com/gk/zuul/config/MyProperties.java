package com.gk.zuul.config;

import com.gk.zuul.config.Model.ZuulRouteVO;
import java.util.List;

public class MyProperties {
    private static volatile List<ZuulRouteVO> results ;

    public static List<ZuulRouteVO> getResults() {
        return results;
    }

    public static void setResults(List<ZuulRouteVO> results) {
        MyProperties.results = results;
    }


}
