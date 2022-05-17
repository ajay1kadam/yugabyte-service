package ak.db.yugabyte.contoller;

import ak.db.yugabyte.entity.LeaderDiversityInfo;
import ak.db.yugabyte.repo.LeaderDiversityInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class LeaderRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(LeaderRestController.class);


    private final LeaderDiversityInfoRepository leaderDiversityInfoRepository;

    public LeaderRestController(LeaderDiversityInfoRepository leaderDiversityInfoRepository) {
        this.leaderDiversityInfoRepository = leaderDiversityInfoRepository;
    }

    @PostMapping("/api/getLeadersByGender/{gender}")
    public ResponseEntity<List<LeaderDiversityInfo>> getLeadersByGender(@PathVariable String gender) {
        long st_time = System.currentTimeMillis();

        try {

            List<LeaderDiversityInfo> leaderDiversityInfoList = leaderDiversityInfoRepository.findByGender(gender);
            return new ResponseEntity(leaderDiversityInfoList, HttpStatus.OK);

        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info("getLeadersByGender/{gender}() Processing time (ms) : " + (System.currentTimeMillis() - st_time));
        }
    }

}
