package cn.hangcc.automaticclockinxust.service.converter.AutomaticPunch;

import cn.hangcc.automaticclockinxust.domain.dto.AutomaticClockIn.SmsLogsDO;
import cn.hangcc.automaticclockinxust.domain.model.AutomaticClockIn.SmsLogsModel;

/**
 *
 * @author chenhang
 * @version $Id: SmsLogsModelConverter.java, v 0.1 2021-03-06 20:24:22 chenhang Exp $$
 */
public class SmsLogsModelConverter {

    /**
     * Convert SmsLogsDO to SmsLogsModel
     * @param smsLogsDO
     * @return
     */
    public static SmsLogsModel convertToSmsLogsModel(SmsLogsDO smsLogsDO) {
        if (smsLogsDO == null) {
            return null;
        }
        SmsLogsModel smsLogsModel = new SmsLogsModel();

        smsLogsModel.setId(smsLogsDO.getId());
        smsLogsModel.setPhone(smsLogsDO.getPhone());
        smsLogsModel.setSignName(smsLogsDO.getSignName());
        smsLogsModel.setTemplateCode(smsLogsDO.getTemplateCode());
        smsLogsModel.setTemplateParam(smsLogsDO.getTemplateParam());
        smsLogsModel.setResultData(smsLogsDO.getResultData());
        smsLogsModel.setOtherInfo(smsLogsDO.getOtherInfo());
        smsLogsModel.setCreated(smsLogsDO.getCreated());
        smsLogsModel.setUpdated(smsLogsDO.getUpdated());

        return smsLogsModel;
    }

    /**
     * Convert SmsLogsModel to SmsLogsDO
     * @param smsLogsModel
     * @return
     */
    public static SmsLogsDO convertToSmsLogsDO(SmsLogsModel smsLogsModel) {
        if (smsLogsModel == null) {
            return null;
        }
        SmsLogsDO smsLogsDO = new SmsLogsDO();

        smsLogsDO.setId(smsLogsModel.getId());
        smsLogsDO.setPhone(smsLogsModel.getPhone());
        smsLogsDO.setSignName(smsLogsModel.getSignName());
        smsLogsDO.setTemplateCode(smsLogsModel.getTemplateCode());
        smsLogsDO.setTemplateParam(smsLogsModel.getTemplateParam());
        smsLogsDO.setResultData(smsLogsModel.getResultData());
        smsLogsDO.setOtherInfo(smsLogsDO.getOtherInfo());
        smsLogsDO.setCreated(smsLogsModel.getCreated());
        smsLogsDO.setUpdated(smsLogsModel.getUpdated());

        return smsLogsDO;
    }
}
