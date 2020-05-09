package com.evoa.cloud.contentcenter.init;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.evoa.cloud.contentcenter.init.common.SentinelRuleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wangxinxin
 */
@Configuration
public class DataSourceInitFunc {
    Logger logger = LoggerFactory.getLogger(DataSourceInitFunc.class);
    @Autowired
    private SentinelProperties sentinelProperties;
    @Bean
    public DataSourceInitFunc init(){
        sentinelProperties.getDatasource().entrySet().stream().filter(map -> map.getValue().getNacos() != null).forEach(map -> {
            NacosDataSourceProperties nacos = map.getValue().getNacos();
            // 限流规则，需要Nacos的dataId中包含flow字符串
            if(nacos.getDataId().contains(SentinelRuleEnum.FLOW.getValue())) {
                ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(nacos.getServerAddr(),
                        nacos.getGroupId(), nacos.getDataId(),
                        source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                        }));
                FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
                logger.info("[NacosSource初始化,从Nacos中获取---《限流规则》---注册完成]"+nacos.getDataId());
            }
            // 降级规则，需要Nacos的dataId中包含degrade字符串
            if(nacos.getDataId().contains(SentinelRuleEnum.DEGRADE.getValue())){
                ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new NacosDataSource<>(nacos.getServerAddr(),
                        nacos.getGroupId(), nacos.getDataId(),
                        source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                        }));
                DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
                logger.info("[NacosSource初始化,从Nacos中获取---《熔断规则》---注册完成]"+nacos.getDataId());
            }
            // 热点规则，需要Nacos的dataId中包含degrade字符串
            if(nacos.getDataId().contains(SentinelRuleEnum.PARAM.getValue())){

                ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleNacosDataSource = new NacosDataSource<>(nacos.getServerAddr(),
                        nacos.getGroupId(), nacos.getDataId(),
                        source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                        }));
                ParamFlowRuleManager.register2Property(paramFlowRuleNacosDataSource.getProperty());
                logger.info("[NacosSource初始化,从Nacos中获取---《热点规则》---注册完成]" +nacos.getDataId());
            }
        });
        return new DataSourceInitFunc();
    }

}