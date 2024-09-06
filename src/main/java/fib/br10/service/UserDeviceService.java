package fib.br10.service;

import fib.br10.core.exception.BaseException;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.entity.user.UserDevice;
import fib.br10.mapper.UserDeviceMapper;
import fib.br10.repository.UserDeviceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDeviceService {

    UserDeviceRepository userDeviceRepository;
    UserDeviceMapper userDeviceMapper;
    UserService userService;

    public UserDeviceDto create(UserDeviceDto userDeviceDto) {
        userService.validateUserExists(userDeviceDto.getUserId());

        UserDevice userDevice = userDeviceMapper.userDeviceDtoToUserDevice(userDeviceDto);
        userDeviceRepository.save(userDevice);

        userDeviceDto.setId(userDevice.getId());
        return userDeviceDto;
    }

    public UserDeviceDto update(UserDeviceDto userDeviceDto) {
        userService.validateUserExists(userDeviceDto.getUserId());
        UserDevice userDevice = userDeviceRepository.findById(userDeviceDto.getId())
                .orElseGet(UserDevice::new);

        userDevice = userDeviceMapper.userDeviceDtoToUserDevice(userDevice, userDeviceDto);
        userDeviceRepository.save(userDevice);

        userDeviceDto.setId(userDevice.getId());
        return userDeviceDto;
    }

    public UserDeviceDto getUserDevice(Long id) {
        UserDevice userDevice = finById(id);
        return userDeviceMapper.userDeviceUserDeviceToUserDeviceDto(userDevice);
    }

    public UserDevice finById(Long id) {
        return userDeviceRepository.findById(id).orElseThrow(BaseException::new);
    }
}
