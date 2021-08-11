package com.myproject.myweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}") // 에러코드.html로 연결, 그 외에는 ExceptionHandler가 낚아채감
public class CustomErrorController extends BasicErrorController {

    public CustomErrorController(ErrorAttributes errorAttributes,
                                 ServerProperties serverProperties,
                                 List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request,
                                  HttpServletResponse response) {
        log(request); // custom
        return super.errorHtml(request, response);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        log(request);
        return super.error(request);
    }

    private void log(HttpServletRequest request) { // request에서 무엇인가 끌어내니까 메서드화 >> 이런 방식 참고하기
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach(
                (key, val) -> log.info("parameter " + key + " : " + Arrays.toString(parameterMap.get(key)))
        );
        Map<String, Object> errorAttributes = getErrorAttributes(request, true);
        errorAttributes.forEach(
                (key, val) -> log.info("errorKey = " + key + " errorValue = " + errorAttributes.get(key))
        );
    }
}

