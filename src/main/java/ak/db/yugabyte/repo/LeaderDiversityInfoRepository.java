package ak.db.yugabyte.repo;

import ak.db.yugabyte.entity.LeaderDiversityInfo;
import com.yugabyte.data.jdbc.repository.YsqlRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderDiversityInfoRepository extends YsqlRepository<LeaderDiversityInfo, Integer> {
}
