package com.example.housesearch.controller;

import com.example.housesearch.domain.base.ApiResponse;
import com.example.housesearch.domain.base.ServiceMultiResult;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.SubwayDTO;
import com.example.housesearch.domain.dto.SubwayStationDTO;
import com.example.housesearch.domain.dto.SupportAddressDTO;
import com.example.housesearch.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Controller
public class HouseController {
    @Autowired
    private HouseService houseService;
    @Autowired
    private SupportAddressService addressService;
    @Autowired
    private BaiduAddressService baiduAddressService;
    @Autowired
    private SubwayService subwayService;
    @Autowired
    private SubwayStationService subwayStationService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private UserService userService;

    /***
     * 自动不全接口
     */
    @GetMapping("rent/house/autocomplete")
    @ResponseBody
    public ApiResponse autoComplete(@RequestParam("prefix") String prefix) {
        if (StringUtils.isNotBlank(prefix)) {
            return ApiResponse.of(HttpStatus.SC_BAD_REQUEST);
        }
        ServiceResult<List<String>> suggest = searchService.suggest(prefix);
        return ApiResponse.of(HttpStatus.SC_OK, suggest.getMessage(), suggest.getResult());
    }

    /***
     * 获取支持城市列表
     */
    @GetMapping("address/support/regions")
    @ResponseBody
    public ApiResponse getSupportRegions(@RequestParam("city_name") String cityEnName) {
        ServiceMultiResult<SupportAddressDTO> address = addressService.findAllRegionByCityName(cityEnName);
        if (address.getTotal() > 0) {
            return ApiResponse.of(HttpStatus.SC_OK, address.getResult());
        }
        return ApiResponse.of(HttpStatus.SC_BAD_REQUEST);
    }

    /***
     * 后去城市支持区域列表
     */
    @GetMapping("address/support/cities")
    @ResponseBody
    public ApiResponse getSupportCities() {
        ServiceMultiResult<SupportAddressDTO> result = addressService.findAllCities();
        if (result.getTotal() > 0) {
            return ApiResponse.of(HttpStatus.SC_OK, result.getResult());
        }
        return ApiResponse.of(HttpStatus.SC_BAD_REQUEST);
    }

    /***
     * 获取具体城市支持的地铁线路
     */
    @GetMapping("address/support/subway/line")
    @ResponseBody
    public ApiResponse getSupportSubwayLine(@RequestParam("city_name")String cityEnName) {
        List<SubwayDTO> result = subwayService.findAllSubwayByCity(cityEnName);

        if (result.size() > 0) {
            return ApiResponse.of(HttpStatus.SC_OK, result);
        }
        return ApiResponse.of(HttpStatus.SC_BAD_REQUEST);
    }

    /***
     * 获取对应地铁线路所支持的地铁站点
     */
    @GetMapping("address/suupport/subway/station")
    @ResponseBody
    public ApiResponse getSupportSubwayStation(@RequestParam("subway_id") Integer subwayId) {
        ServiceResult<SubwayStationDTO> result = subwayStationService.findSubwayStationById(subwayId);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.SC_OK, result.getResult());
        }
        return ApiResponse.of(HttpStatus.SC_BAD_REQUEST);
    }

    /***
     * 获取租房情况
     */
}
