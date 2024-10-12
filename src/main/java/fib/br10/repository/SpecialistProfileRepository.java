package fib.br10.repository;

import fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse;
import fib.br10.entity.specialist.SpecialistProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistProfileRepository extends JpaRepository<SpecialistProfile, Long> {

    Boolean existsBySpecialistUserId(Long userId);
    boolean existsBySpecialityId(Long id);

    Optional<SpecialistProfile> findBySpecialistUserId(Long userId);

    @Query("""
                    select new fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse(
                    sp.id,u.id,
                     case when :lang = 2 then s.name_en
                              when :lang = 3 then s.name_ru
                              else s.name end,
                    s.id,sp.address,sp.city,sp.instagram,sp.tiktok,sp.facebook,img.path, u.username,u.name,u.surname
                   )
                    from User u
                    join SpecialistProfile sp on sp.specialistUserId = u.id
                    join Image img on img.id = sp.imageId
                    join Speciality s on sp.specialityId = s.id
                    where u.status = :status and u.phoneNumber in :phoneNumbers
            """)
    List<SpecialistProfileReadResponse> findAllByPhoneNumbers(List<String> phoneNumbers,Integer status, Integer lang ) ;

    @Query("""
   select new fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse(
                    sp.id,u.id,
                     case when :lang = 2 then s.name_en
                              when :lang = 3 then s.name_ru
                              else s.name end,
                    s.id,sp.address,sp.city,sp.instagram,sp.tiktok,sp.facebook,img.path, u.username,u.name,u.surname
                   )
                    from User u
                    join SpecialistProfile sp on sp.specialistUserId = u.id
                    join Image img on img.id = sp.imageId
                    join Speciality s on sp.specialityId = s.id
                    where u.status = :status and u.phoneNumber = :phoneNumber
""")
    SpecialistProfileReadResponse findByPhoneNumber(String phoneNumber, Integer status, Integer lang);

    @Query("""
                    select new fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse(
                   sp.id,u.id,
                     case when :lang = 2 then s.name_en
                              when :lang = 3 then s.name_ru
                              else s.name end,
                    s.id,sp.address,sp.city,sp.instagram,sp.tiktok,sp.facebook,img.path,u.username,u.name,u.surname
                   )
                    from User u
                    join SpecialistProfile sp on sp.specialistUserId = u.id
                    join Speciality s on sp.specialityId = s.id
                    left join Image img on img.id = sp.imageId
                    where u.status = :status and ( u.phoneNumber = :search or u.username = :search)
            """)
    List<SpecialistProfileReadResponse> findBySearch(String search, Integer status, Integer lang);

    @Query("""
   select new fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse(
                    sp.id,u.id,
                     case when :lang = 2 then s.name_en
                              when :lang = 3 then s.name_ru
                              else s.name end,
                    s.id,sp.address,sp.city,sp.instagram,sp.tiktok,sp.facebook,img.path,u.username,u.name,u.surname
                   )
                    from User u
                    join SpecialistProfile sp on sp.specialistUserId = u.id
                    left join Image img on img.id = sp.imageId
                    join Speciality s on sp.specialityId = s.id
                    where u.status = :status and sp.id = :profileId
""")
    SpecialistProfileReadResponse findById(Long profileId, Integer status, Integer lang);


    @Query("""
                    select new fib.br10.dto.specialist.specialistprofile.response.SpecialistProfileReadResponse(
                    sp.id,u.id,
                     case when :lang = 2 then s.name_en
                              when :lang = 3 then s.name_ru
                              else s.name end,
                    s.id,sp.address,sp.city,sp.instagram,sp.tiktok,sp.facebook,img.path, u.username,u.name,u.surname
                   )
                    from User u
                    join SpecialistProfile sp on sp.specialistUserId = u.id
                    left join Image img on img.id = sp.imageId
                    join Speciality s on sp.specialityId = s.id
                    where u.status = :status and u.id = :userId
            """)
    SpecialistProfileReadResponse findByUserId(Long userId,Integer status, Integer lang ) ;


}