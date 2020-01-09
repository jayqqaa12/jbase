package com.jayqqaa12.jbase.spring.mvc.version;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public final class ApiVersionRequestCondition extends AbstractRequestCondition<ApiVersionRequestCondition> {

    private Set<ApiVersionExpression> versions;

    public ApiVersionRequestCondition(ApiVersionExpression... version) {
        this.versions = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(version)));
    }

    public ApiVersionRequestCondition(Collection<ApiVersionExpression> versions) {
        if (versions == null)
            this.versions = Collections.unmodifiableSet(new LinkedHashSet<>());
        this.versions = Collections.unmodifiableSet(new LinkedHashSet<>(versions));
    }

    @Override
    public ApiVersionRequestCondition combine(ApiVersionRequestCondition other) {
        Set<ApiVersionExpression> set = new LinkedHashSet<ApiVersionExpression>(this.versions);
        set.addAll(other.versions);
        return new ApiVersionRequestCondition(set);
    }

    @Override
    public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
//
//        String version = request.getHeader("version");
//
//        ApiVersionExpression annotationVersion = getMaxApiVersionExpression(this.versions);
//        ApiVersion apiVersion = annotationVersion.version;
//        Integer v = VersionEnum.of(version).getVersion();
//
//        if (apiVersion.model() == ApiVersion.Model.ASSIGN && apiVersion.value() == v)
//            return new ApiVersionRequestCondition(annotationVersion);
//        else if (apiVersion.model() == ApiVersion.Model.COMPATIBLY &&
//                v>=apiVersion.min() && v<=apiVersion.max())
//            return new ApiVersionRequestCondition(annotationVersion);


        return null;
    }

    private ApiVersionExpression getMaxApiVersionExpression(Collection<ApiVersionExpression> collection) {
        Optional<ApiVersionExpression> m = this.versions.stream().max(ApiVersionExpression::compareTo);

        if (!m.isPresent())
            return null;
        return m.get();
    }

    @Override
    public int compareTo(ApiVersionRequestCondition other, HttpServletRequest request) {
        ApiVersionExpression m = getMaxApiVersionExpression(this.versions);
        ApiVersionExpression n = getMaxApiVersionExpression(other.versions);

        if (m != null && n != null) {
            return m.compareTo(n);
        } else if (m == null)
            return 1;
        else if (n == null)
            return -1;
        else
            return 0;

    }

    @Override
    protected Collection<?> getContent() {
        return this.versions;
    }

    @Override
    protected String getToStringInfix() {
        return "||";
    }

    @Data
    @AllArgsConstructor
    public static class ApiVersionExpression implements Comparable<ApiVersionExpression> {
        private ApiVersion version;

        @Override
        public int compareTo(ApiVersionExpression o) {
            return 0;
        }
    }

}