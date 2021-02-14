/*
 * Copyright (c) 2021 hangcc.cn
 * All rights reserved.
 *
 */
package cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.ConfigDO;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.ConfigModel;

/**
 *
 * @author chenhang
 * @version $Id: ConfigModelConverter.java, v 0.1 2021-02-14 22:10:43 chenhang Exp $$
 */
public class ConfigModelConverter {

    /**
     * Convert ConfigDO to ConfigModel
     * @param configDO
     * @return
     */
    public static ConfigModel convertToConfigModel(ConfigDO configDO) {
        if (configDO == null) {
            return null;
        }
        ConfigModel configModel = new ConfigModel();

        configModel.setKey(configDO.getKey());
        configModel.setValue(configDO.getValue());

        return configModel;
    }

    /**
     * Convert ConfigModel to ConfigDO
     * @param configModel
     * @return
     */
    public static ConfigDO convertToConfigDO(ConfigModel configModel) {
        if (configModel == null) {
            return null;
        }
        ConfigDO configDO = new ConfigDO();

        configDO.setKey(configModel.getKey());
        configDO.setValue(configModel.getValue());

        return configDO;
    }
}
