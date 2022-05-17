package ak.db.yugabyte.entity;

import javax.persistence.*;

@Entity
@Table(name= "TBL_DIV_LEADER")
public class LeaderDiversityInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="NAME")
    private String name;
    @Column(name="GENDER")
    private String gender;
    @Column(name="ETHNICITY")
    private String ethnicity;
    @Column(name="LGBT")
    private String isLgbt;
    @Column(name="VETERAN")
    private String isVeteran;
    @Column(name="DISABLED")
    private String isDisable;
    @Column(name="SHARE_PERCENT")
    private long sharePercentage;


    @Column(name = "company_id", insertable = false, updatable = false)
    private Integer companyId;


    public LeaderDiversityInfo() {
    }


    public LeaderDiversityInfo(Integer id, String name, String gender, String ethnicity, String isLgbt, String isVeteran, String isDisable, long sharePercentage) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.ethnicity = ethnicity;
        this.isLgbt = isLgbt;
        this.isVeteran = isVeteran;
        this.isDisable = isDisable;
        this.sharePercentage = sharePercentage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getIsLgbt() {
        return isLgbt;
    }

    public void setIsLgbt(String isLgbt) {
        this.isLgbt = isLgbt;
    }

    public String getIsVeteran() {
        return isVeteran;
    }

    public void setIsVeteran(String isVeteran) {
        this.isVeteran = isVeteran;
    }

    public String getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(String isDisable) {
        this.isDisable = isDisable;
    }

    public long getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(long sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }



}