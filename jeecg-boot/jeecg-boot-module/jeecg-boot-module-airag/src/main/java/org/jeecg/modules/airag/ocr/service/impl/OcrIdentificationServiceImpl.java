package org.jeecg.modules.airag.ocr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.airag.ocr.entity.OcrIdentification;
import org.jeecg.modules.airag.ocr.mapper.OcrIdentificationMapper;
import org.jeecg.modules.airag.ocr.service.IOcrIdentificationService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OcrIdentificationServiceImpl extends ServiceImpl<OcrIdentificationMapper, OcrIdentification> implements IOcrIdentificationService {

}
