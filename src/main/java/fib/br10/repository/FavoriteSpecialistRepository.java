package fib.br10.repository;

import fib.br10.dto.specialist.favoritespecialist.response.FavoriteSpecialistResponse;
import fib.br10.entity.specialist.FavoriteSpecialist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteSpecialistRepository extends JpaRepository<FavoriteSpecialist, Long> {
//           private Long id;
//            private Long userId;
//            private String userName;
//            private Long specialistId;
//            private String specialistName;
//            private String image;

    @Query("""
                    select new fib.br10.dto.specialist.favoritespecialist.response.FavoriteSpecialistResponse(
                        fs.id,
                        fs.userId,
                        u.name,
                        fs.specialistId,
                        u.username,
                        img.path
                    )
                        from FavoriteSpecialist fs
                        left join User u on u.id = fs.specialistId
                        left join SpecialistProfile sp on sp.specialistUserId = fs.specialistId
                        left join Image img on img.id = sp.imageId
            """)
    List<FavoriteSpecialistResponse> findByCustomerId(Long customerID);
}

