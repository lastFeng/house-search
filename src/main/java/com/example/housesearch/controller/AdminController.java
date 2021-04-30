package com.example.housesearch.controller;

import com.example.housesearch.domain.base.ApiResponse;
import com.example.housesearch.domain.base.HouseForm;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.*;
import com.example.housesearch.service.*;
import com.example.housesearch.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.example.housesearch.utils.Constant.*;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private SupportAddressService supportAddressService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private SubwayService subwayService;

    @Autowired
    private SubwayStationService subwayStationService;

    @Autowired
    private HouseTagService houseTagService;

    @Autowired
    private UserService userService;

    @Autowired
    private HouseSubscribeService houseSubscribeService;

    @Value("${spring.servlet.multipart.location}")
    private String localPath;

    @GetMapping("/center")
    public  String adminCenter() {
        return "/admin/center";
    }

    @GetMapping("/welcome")
    public String adminWelcome() {
        return "/admin/welcome";
    }

    @GetMapping("/common")
    public String adminCommon() {
        return "/admin/common";
    }

    /***
     * 上传图片到本地
     * @param file
     * @return
     */
    @PostMapping(value = "/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.of(501, "NOT_INVALID_PARAMS");
        }
        String fileName = file.getOriginalFilename();
        File target = new File(localPath + fileName);
        try {
            file.transferTo(target);
        } catch (IOException e) {
            log.error("文件上传失败：" + e.getMessage());
            return ApiResponse.of(500, e.getMessage());
        }

        return ApiResponse.of(200);
    }

    /***
     * 新增房源功能页
     */
    @GetMapping("/add/house")
    public String addHousePage() {
        return "admin/house-add";
    }

    @PostMapping("/add/house")
    @ResponseBody
    public ApiResponse addHouse(@Valid @ModelAttribute("form-house-add")HouseForm houseForm, BindingResult error) {
        if (error.hasErrors()) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), error.getAllErrors().get(0).getDefaultMessage());
        }

        if (houseForm.getPhotos() == null) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), "必须上传图片");
        }

        Map<Constant.CityLevel, SupportAddressDTO> addressDTOMap = supportAddressService.findCityAndRegion(
                houseForm.getCityEnName(), houseForm.getRegionEnName());
        if (addressDTOMap.size() != Constant.CityLevel.values().length) {
            return ApiResponse.of(600);
        }

        ServiceResult<HouseDTO> result = houseService.saveOrUpdate(houseForm);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value(), result.getMessage(), result.getResult());
        }

        return ApiResponse.of(600);
    }

    /***
     * 获取房源列表
     */
    @GetMapping("/house/list")
    public String getHouseListPage() {
        return "/admin/house-list";
    }

    /***
     * TODO: 房源查询接口，后面统一写
     */

    @GetMapping("/house/edit")
    public String getHouseEdit(@RequestParam("id")Integer id, Model model) {
        if (id == null || id < 1) {
            return "404";
        }
        ServiceResult<HouseDTO> result = houseService.findCompleteOne(id);
        if (!result.isSuccess()) {
            return "404";
        }

        HouseDTO houseDTO = result.getResult();
        model.addAttribute("house", houseDTO);

        Map<Constant.CityLevel, SupportAddressDTO> region = supportAddressService.findCityAndRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());
        model.addAttribute(Constant.CityLevel.CITY.getValue(), region.get(Constant.CityLevel.CITY));
        model.addAttribute(Constant.CityLevel.REGION.getValue(), region.get(Constant.CityLevel.REGION));

        HouseDetailDTO houseDetail = houseDTO.getHouseDetail();
        ServiceResult<SubwayDTO> subway = subwayService.findSubwayById(houseDetail.getSubwayLineId());

        if (subway.isSuccess()) {
            model.addAttribute("subway", subway.getResult());
        }

        ServiceResult<SubwayStationDTO> subwayStation = subwayStationService.findSubwayStationById(houseDetail.getSubwayStationId());

        if (subwayStation.isSuccess()) {
            model.addAttribute("station", subwayStation.getResult());
        }

        return "/admin/house-edit";
    }

    @PostMapping("/house/edit")
    @ResponseBody
    public ApiResponse editHouse(@Valid @ModelAttribute("form-house-edit") HouseForm houseForm, BindingResult error) {
        if (error.hasErrors()) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), error.getAllErrors().get(0).getDefaultMessage());
        }

        if (houseForm.getPhotos() == null) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), "必须上传图片");
        }

        Map<Constant.CityLevel, SupportAddressDTO> addressDTOMap = supportAddressService.findCityAndRegion(
                houseForm.getCityEnName(), houseForm.getRegionEnName());
        if (addressDTOMap.size() != Constant.CityLevel.values().length) {
            return ApiResponse.of(600);
        }

        ServiceResult<HouseDTO> result = houseService.saveOrUpdate(houseForm);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value(), result.getMessage(), result.getResult());
        }

        return ApiResponse.of(600);
    }

    /***
     * 新增房源标签
     */
    @PostMapping("/house/tag")
    @ResponseBody
    public ApiResponse addHouseTag(@RequestParam("house_id") Integer houseId,
                                   @RequestParam("tag") String tag) {
        if (houseId == null || StringUtils.isBlank(tag)) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value());
        }

        ServiceResult result = houseTagService.addTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value(), result.getMessage(), result.getResult());
        }

        return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }

    /**
     * 移除房源标签
     */
    @DeleteMapping("/house/tag")
    @ResponseBody
    public ApiResponse removeHouseTag(@RequestParam("houseId") Integer houseId,
                                      @RequestParam("tag") String tag) {
        if (houseId < 1 || StringUtils.isBlank(tag)) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value());
        }

        ServiceResult result = houseTagService.removeTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value());
        }
        return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }

    /***
     * 审核接口
     */
    @PutMapping("/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiResponse operationHouse(@PathVariable("id") Integer id,
                                      @PathVariable("operation") int operation) {
        if (id < 1) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value());
        }

        ServiceResult result;

        switch (operation) {
            case HOUSEOPERATION_PASS:
                result = houseService.updateStatus(id, HouseStatus.PASS.getValue());
                break;
            case HOUSEOPERATION_PULL_OUT:
                result = houseService.updateStatus(id, HouseStatus.NOT_AUDITED.getValue());
                break;
            case HOUSEOPERATION_DELETE:
                result = houseService.updateStatus(id, HouseStatus.DELETED.getValue());
                break;
            case HOUSEOPERATION_RENT:
                result = houseService.updateStatus(id, HouseStatus.RENTED.getValue());
                break;
            default:
                return ApiResponse.of(HttpStatus.BAD_REQUEST.value());
        }

        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value(), result.getMessage());
        }
        return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }

    /***
     * 获取用户信息
     */
    @GetMapping("/user/{userId}")
    @ResponseBody
    public ApiResponse getUserInfo(@PathVariable("userId") Integer userId) {
        if (userId == null || userId < 1) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value());
        }

        ServiceResult<UserDTO> result = userService.findById(userId);
        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value(), result.getMessage(), result.getResult());
        }
        return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }

    /**
     * 完成订阅
     */
    @PostMapping("/finish/subscribe")
    @ResponseBody
    public ApiResponse finishSubscribe(@RequestParam("houseId") Integer houseId) {
        if (houseId < 1) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST.value());
        }

        ServiceResult result = houseSubscribeService.finishSubscribe(houseId);

        if (result.isSuccess()) {
            return ApiResponse.of(HttpStatus.OK.value(), result.getMessage(), result.getResult());
        }
        return ApiResponse.of(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }
}
