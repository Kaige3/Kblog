package com.kaige.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaige.entity.LoginLog;
import com.kaige.mapper.LoginLogMapper;
import com.kaige.model.dto.UserAgentDTO;
import com.kaige.service.LoginLogService;
import com.kaige.utils.IpAddressUtils;
import com.kaige.utils.UserAgentUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (LoginLog)表服务实现类
 *
 * @author makejava
 * @since 2024-09-07 17:09:19
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

   /* @Autowired
    LoginLogMapper loginLogMapper;*/

    @Autowired
    private UserAgentUtils userAgentUtils;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveLoginLog(LoginLog log) {
        String ipSource = IpAddressUtils.getCityInfo(log.getIp());
        UserAgentDTO userAgentDTO = userAgentUtils.parseOsAndBrowser(log.getUserAgent());
        log.setIpSource(ipSource);
        log.setOs(userAgentDTO.getOs());
        log.setBrowser(userAgentDTO.getBrowser());
        boolean save = save(log);
        //TODO
        if ( save == false) {
            throw new PersistenceException("日志添加失败");
        }
    }
}
