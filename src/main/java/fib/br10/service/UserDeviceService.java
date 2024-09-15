package fib.br10.service;

import fib.br10.core.exception.BaseException;
import fib.br10.dto.userdevice.request.UserDeviceDto;
import fib.br10.entity.user.ClientType;
import fib.br10.entity.user.UserDevice;
import fib.br10.mapper.UserDeviceMapper;
import fib.br10.repository.UserDeviceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDeviceService {

    UserDeviceRepository userDeviceRepository;
    UserDeviceMapper userDeviceMapper;
    UserService userService;

    @Transactional
    public UserDeviceDto create(UserDeviceDto userDeviceDto) {
        userService.validateUserExists(userDeviceDto.getUserId());

        UserDevice userDevice = userDeviceMapper.userDeviceDtoToUserDevice(userDeviceDto);
        ClientType.fromValue(userDevice.getClientType());
        userDeviceRepository.save(userDevice);

        userDeviceDto.setId(userDevice.getId());
        return userDeviceDto;
    }

    @Transactional
    public UserDeviceDto update(UserDeviceDto userDeviceDto) {
        userService.validateUserExists(userDeviceDto.getUserId());
        UserDevice userDevice = userDeviceRepository.findByDeviceIdAndUserId(userDeviceDto.getDeviceId(), userDeviceDto.getUserId())
                .orElseGet(UserDevice::new);

        userDevice = userDeviceMapper.userDeviceDtoToUserDevice(userDevice, userDeviceDto);
        ClientType.fromValue(userDevice.getClientType());
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
