package com.kaige.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.kaige.entity.pojo.Category;
import com.kaige.entity.vo.CategoryVo;
import com.kaige.entity.vo.ExcelCategoryVo;
import com.kaige.enums.AppHttpCodeEnum;
import com.kaige.result.ResponseResult;
import com.kaige.service.CategoryService;
import com.kaige.utils.BeanCopyUtils;
import com.kaige.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list =  categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
//        设置下载文件的请求头
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
//        获取需要导出的数据
            List<Category> categoryVOs = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVOs, ExcelCategoryVo.class);
//        把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(),ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出");
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
//        如果出现异常要响应json
    }

}
