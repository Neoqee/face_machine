package com.neoqee.facemachine.baidu

class SingleBaseConfig private constructor(){
    companion object{
        private var baseConfig: BaseConfig? = null

        @JvmStatic
        fun getBaseConfig(): BaseConfig{
            if (baseConfig == null){
                baseConfig = BaseConfig()
            }
            return baseConfig!!
        }

        @JvmStatic
        fun copyInstance(result: BaseConfig){
            baseConfig = result
        }
    }
}