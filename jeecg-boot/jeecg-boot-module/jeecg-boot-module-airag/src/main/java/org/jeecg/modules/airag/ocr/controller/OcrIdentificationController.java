package org.jeecg.modules.airag.ocr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.AssertUtils;
import org.jeecg.modules.airag.app.consts.AiAppConsts;
import org.jeecg.modules.airag.app.entity.AiragApp;
import org.jeecg.modules.airag.ocr.entity.OcrIdentification;
import org.jeecg.modules.airag.ocr.service.IOcrIdentificationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/airag/ocr")
@Slf4j
public class OcrIdentificationController extends JeecgController<OcrIdentification, IOcrIdentificationService> {

    @Resource
    private IOcrIdentificationService ocrIdentificationService;


    @GetMapping(value = "/list")
    public Result<IPage<OcrIdentification>> queryPageList(OcrIdentification ocrIdentification,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<OcrIdentification> queryWrapper = QueryGenerator.initQueryWrapper(ocrIdentification, req.getParameterMap());
        Page<OcrIdentification> page = new Page<>(pageNo, pageSize);
        IPage<OcrIdentification> pageList = ocrIdentificationService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody OcrIdentification ocrIdentification) {
        if(ocrIdentification.getId() == null){
            ocrIdentification.setUploadTime(new Date());
        }
        ocrIdentificationService.saveOrUpdate(ocrIdentification);
        return Result.OK("保存完成!", ocrIdentification.getId());
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        ocrIdentificationService.removeById(id);
        return Result.OK("删除成功!");
    }

    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.ocrIdentificationService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }
}
