package com.cuckoo.mapper;


import com.cuckoo.entity.ReportTemplateField;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportTemplateFieldMapper {

    List<ReportTemplateField> queryByTemplateCode(@Param("templateCode")  String templateCode);

}