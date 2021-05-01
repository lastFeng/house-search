package com.example.housesearch.service;

import com.example.housesearch.domain.base.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.housesearch.utils.Constant.SMS_CODE_CONTENT_PREFIX;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Service
@Slf4j
public class SmsService {

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 获取验证码
     * 根据具体情况具体实现
     * 这里使用随机获取的机制，将其保存在redis中
     */
    public ServiceResult<String> sendSms(String telephone) {
        String code = UUID.randomUUID().toString().substring(0, 3);
        String key = SMS_CODE_CONTENT_PREFIX + telephone;
        redisTemplate.opsForValue().set(key, code, 60, TimeUnit.SECONDS);
        log.info("add code for " + telephone + " " + code);
        System.out.println("获取的验证码为：" + code);
        return ServiceResult.<String>builder().success(true).message("success").result(code).build();
    }
    /**
     * 获取手机验证码缓存
     * @param telephone
     * @return
     */
    public String getSmsCode(String telephone) {
        return (String) redisTemplate.opsForValue().get(SMS_CODE_CONTENT_PREFIX + telephone);
    }

    /***
     * 移除手机验证码换成你
     */
    public void removeCode(String telephone) {
        redisTemplate.delete(SMS_CODE_CONTENT_PREFIX +telephone);
    }
}
