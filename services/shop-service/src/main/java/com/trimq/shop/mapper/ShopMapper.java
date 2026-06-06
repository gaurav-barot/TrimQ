package com.trimq.shop.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trimq.shop.dto.*;
import com.trimq.shop.entity.Shop;
import com.trimq.shop.entity.ShopService;
import com.trimq.shop.entity.Staff;
import com.trimq.shop.entity.WorkingHours;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * MapStruct mapper for Shop entities.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShopMapper {

    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    @Mapping(target = "id", expression = "java(shop.getId().toString())")
    @Mapping(target = "ownerId", expression = "java(shop.getOwnerId().toString())")
    @Mapping(target = "fullAddress", expression = "java(buildFullAddress(shop))")
    @Mapping(target = "images", expression = "java(parseImages(shop.getImages()))")
    ShopResponse toShopResponse(Shop shop);

    @Mapping(target = "id", expression = "java(service.getId().toString())")
    @Mapping(target = "shopId", expression = "java(service.getShop().getId().toString())")
    ServiceResponse toServiceResponse(ShopService service);

    @Mapping(target = "id", expression = "java(staff.getId().toString())")
    @Mapping(target = "shopId", expression = "java(staff.getShop().getId().toString())")
    @Mapping(target = "userId", expression = "java(staff.getUserId() != null ? staff.getUserId().toString() : null)")
    @Mapping(target = "specializations", expression = "java(parseSpecializations(staff.getSpecializations()))")
    StaffResponse toStaffResponse(Staff staff);

    @Mapping(target = "id", expression = "java(wh.getId().toString())")
    @Mapping(target = "dayName", expression = "java(wh.getDayOfWeek().toString())")
    @Mapping(target = "openTime", expression = "java(wh.getOpenTime() != null ? wh.getOpenTime().format(TIME_FORMATTER) : null)")
    @Mapping(target = "closeTime", expression = "java(wh.getCloseTime() != null ? wh.getCloseTime().format(TIME_FORMATTER) : null)")
    @Mapping(target = "breakStart", expression = "java(wh.getBreakStart() != null ? wh.getBreakStart().format(TIME_FORMATTER) : null)")
    @Mapping(target = "breakEnd", expression = "java(wh.getBreakEnd() != null ? wh.getBreakEnd().format(TIME_FORMATTER) : null)")
    WorkingHoursResponse toWorkingHoursResponse(WorkingHours wh);

    List<ServiceResponse> toServiceResponseList(List<ShopService> services);
    List<StaffResponse> toStaffResponseList(List<Staff> staff);
    List<WorkingHoursResponse> toWorkingHoursResponseList(List<WorkingHours> workingHours);

    default String buildFullAddress(Shop shop) {
        return String.format("%s, %s, %s, %s - %s",
                shop.getAddressLine(),
                shop.getArea(),
                shop.getCity(),
                shop.getState(),
                shop.getPincode());
    }

    default List<String> parseImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(imagesJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    default List<String> parseSpecializations(String specializationsJson) {
        if (specializationsJson == null || specializationsJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(specializationsJson, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}

