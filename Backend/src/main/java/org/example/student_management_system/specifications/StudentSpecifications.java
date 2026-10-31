package org.example.student_management_system.specifications;

import org.example.student_management_system.entities.Student;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class StudentSpecifications {
    private StudentSpecifications() {}

    public static Specification<Student> keyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;
            String pattern = keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("lastName")), pattern),
                    cb.like(cb.lower(root.get("firstName")), pattern)
            );
        };
    }
    public static Specification<Student> levelIn(List<String> levels) {
        return (root, query, cb) ->
                (levels == null || levels.isEmpty()) ? null : root.get("studyLevel").in(levels);
    }

    public static Specification<Student> enrollmentYearBetween(Integer from, Integer to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from == null) return cb.lessThanOrEqualTo(root.get("enrollmentYear"), to);
            if (to == null)   return cb.greaterThanOrEqualTo(root.get("enrollmentYear"), from);
            return cb.between(root.get("enrollmentYear"), from, to);
        };
    }

    public static Specification<Student> absencesBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(root.get("absencesCount"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("absencesCount"), min);
            return cb.between(root.get("absencesCount"), min, max);
        };
    }
}