package ak.db.yugabyte.repo;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;


/*
\d dept
                  Table "public.dept"
   Column    |  Type   | Collation | Nullable | Default
-------------+---------+-----------+----------+---------
 deptno      | integer |           | not null |
 dname       | text    |           |          |
 loc         | text    |           |          |
 description | text    |           |          |
Indexes:
    "pk_dept" PRIMARY KEY, lsm (deptno ASC)
Referenced by:
    TABLE "emp" CONSTRAINT "fk_deptno" FOREIGN KEY (deptno) REFERENCES dept(deptno)

 select * from dept;
 deptno |   dname    |   loc    |                                                               description
--------+------------+----------+-----------------------------------------------------------------------------------------------------------------------------------------
     10 | ACCOUNTING | NEW YORK | preparation of financial statements, maintenance of general ledger, payment of bills, preparation of customer bills, payroll, and more.
     20 | RESEARCH   | DALLAS   | responsible for preparing the substance of a research report or security recommendation.
     30 | SALES      | CHICAGO  | division of a business that is responsible for selling products or services
     40 | OPERATIONS | BOSTON   | administration of business practices to create the highest level of efficiency possible within an organization
(4 rows)
 */
@Table
public class Dept {

    @Id
    private Integer deptno;

    @NotEmpty
    private String dname;

    @NotEmpty
    private String loc;

    @NotEmpty
    private String description;

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dept dept = (Dept) o;
        return Objects.equals(deptno, dept.deptno) && Objects.equals(dname, dept.dname) && Objects.equals(loc, dept.loc) && Objects.equals(description, dept.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptno, dname, loc, description);
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", loc='" + loc + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
