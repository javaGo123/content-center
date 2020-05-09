package com.evoa.cloud.contentcenter.init.common;

public enum SentinelRuleEnum {

    /**
     * 规则类型定义
     */
    FLOW("client-flow"), DEGRADE("client-degrade"),PARAM("client-param"){
        @Override
        public boolean isRest(){
            return true;
        }
    };

    private String value;

    private  SentinelRuleEnum(String value){
        this.value=value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public boolean isRest(){
        return  false;
    }

}
