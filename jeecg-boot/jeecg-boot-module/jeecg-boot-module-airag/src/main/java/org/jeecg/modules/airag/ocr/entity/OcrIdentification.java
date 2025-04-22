package org.jeecg.modules.airag.ocr.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_ocr_identification")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="OCR识别")
public class OcrIdentification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;

    /**
     * 标题(文件名)
     */
    @Schema(description = "标题(文件名)")
    private String fileName;

    /**
     * 识别结果
     */
    @Schema(description = "识别结果")
    private String ocrResult;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间")
    private Date uploadTime;

    /**
     * 图片路径
     */
    @Schema(description = "图片路径")
    private String imagePath;
}
