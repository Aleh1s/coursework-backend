package ua.palamar.courseworkbackend.adapter.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.user.UserDtoAdapter;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.user.UserAccount;

@Component
public class AdvertisementDtoAdapterImpl implements AdvertisementDtoAdapter{

    private final UserDtoAdapter userDtoAdapter;

    @Autowired
    public AdvertisementDtoAdapterImpl(UserDtoAdapter userDtoAdapter) {
        this.userDtoAdapter = userDtoAdapter;
    }

    @Override
    public AdvertisementResponse getModel(
            Advertisement advertisement,
            UserAccount creator
    ) {
        return new AdvertisementResponse(
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getCity(),
                advertisement.getStatus(),
                advertisement.getCategory(),
                advertisement.getCreatedAt(),
                userDtoAdapter.getModel(creator)
        );
    }
}
