package ua.palamar.courseworkbackend.adapter.item;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.ItemAdvertisementAdapter;
import ua.palamar.courseworkbackend.dto.response.ItemAdvertisementResponse;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

@Component
public class SimpleItemAdvertisementAdapter implements ItemAdvertisementAdapter {
    @Override
    public ItemAdvertisementResponse getResponse(ItemAdvertisementEntity advertisement, UserInfo userInfo) {
        return new ItemAdvertisementResponse(
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCreatedAt(),
                advertisement.getDimensions(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getCity()
        );
    }
}
