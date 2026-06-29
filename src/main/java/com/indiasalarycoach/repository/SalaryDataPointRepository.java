package com.indiasalarycoach.repository;

import com.indiasalarycoach.entity.SalaryDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryDataPointRepository extends JpaRepository<SalaryDataPoint, Long> {

    @Query("""
        SELECT s FROM SalaryDataPoint s
        WHERE LOWER(s.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))
        AND LOWER(s.city) = LOWER(:city)
        AND LOWER(s.industry) = LOWER(:industry)
        AND s.experienceMin <= :exp AND s.experienceMax >= :exp
        """)
    List<SalaryDataPoint> findByTitleCityIndustryExperience(
        @Param("title") String title,
        @Param("city") String city,
        @Param("industry") String industry,
        @Param("exp") int experience
    );

    @Query("""
        SELECT s FROM SalaryDataPoint s
        WHERE LOWER(s.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))
        AND s.experienceMin <= :exp AND s.experienceMax >= :exp
        """)
    List<SalaryDataPoint> findByTitleAndExperience(@Param("title") String title, @Param("exp") int experience);

    @Query("""
        SELECT s FROM SalaryDataPoint s
        WHERE LOWER(s.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))
        AND LOWER(s.industry) = LOWER(:industry)
        AND s.experienceMin <= :exp AND s.experienceMax >= :exp
        ORDER BY s.salaryMedian DESC
        """)
    List<SalaryDataPoint> findTopCitiesForRole(
        @Param("title") String title,
        @Param("industry") String industry,
        @Param("exp") int experience
    );

    @Query("""
        SELECT DISTINCT s.city, AVG(s.salaryMedian) as avgSalary
        FROM SalaryDataPoint s
        WHERE LOWER(s.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))
        GROUP BY s.city
        ORDER BY avgSalary DESC
        """)
    List<Object[]> findAvgSalaryByCity(@Param("title") String title);

    @Query("""
        SELECT DISTINCT s.industry, AVG(s.salaryMedian) as avgSalary
        FROM SalaryDataPoint s
        WHERE LOWER(s.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))
        GROUP BY s.industry
        ORDER BY avgSalary DESC
        """)
    List<Object[]> findAvgSalaryByIndustry(@Param("title") String title);

    @Query("""
        SELECT s FROM SalaryDataPoint s
        WHERE LOWER(s.jobTitle) LIKE LOWER(CONCAT('%', :title, '%'))
        AND s.skillsRequired IS NOT NULL
        """)
    List<SalaryDataPoint> findByTitleWithSkills(@Param("title") String title);
}
