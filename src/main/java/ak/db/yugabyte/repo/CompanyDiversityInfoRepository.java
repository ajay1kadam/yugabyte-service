package ak.db.yugabyte.repo;

import ak.db.yugabyte.entity.CompanyDiversityInfo;
import com.yugabyte.data.jdbc.repository.YsqlRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDiversityInfoRepository extends YsqlRepository<CompanyDiversityInfo, Integer> {
}
