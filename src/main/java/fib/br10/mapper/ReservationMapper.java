package fib.br10.mapper;

import fib.br10.dto.reservation.request.CreateReservationRequest;
import fib.br10.dto.reservation.request.UpdateReservationRequest;
import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.entity.reservation.Reservation;
import fib.br10.entity.specialist.SpecialistService;
import org.mapstruct.*;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservationMapper {

    Reservation createReservationRequestToReservation(@MappingTarget Reservation reservation, CreateReservationRequest createReservationRequest);

    ReservationResponse reservationToReservationResponse(@MappingTarget ReservationResponse response,Reservation reservation);

    Reservation updateReservationRequestToReservation(@MappingTarget Reservation reservation, UpdateReservationRequest updateReservationRequest);
}
    
    
  
 