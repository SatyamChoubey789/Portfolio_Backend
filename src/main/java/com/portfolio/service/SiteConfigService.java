package com.portfolio.service;

import com.portfolio.model.SiteConfig;
import com.portfolio.repository.SiteConfigRepository;
import com.portfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SiteConfigService {
    
    private final SiteConfigRepository siteConfigRepository;
    
    public SiteConfig getConfigByKey(String key) {
        return siteConfigRepository.findByConfigKey(key)
            .orElseThrow(() -> new ResourceNotFoundException("Config not found: " + key));
    }
    
    @Transactional
    public SiteConfig updateConfig(String key, Map<String, Object> value) {
        SiteConfig config = siteConfigRepository.findByConfigKey(key)
            .orElse(SiteConfig.builder().configKey(key).build());
        
        config.setConfigValue(value);
        return siteConfigRepository.save(config);
    }
}