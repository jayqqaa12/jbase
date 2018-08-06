package com.jayqqaa12.jbase.spring.mvc.handler;

import com.jayqqaa12.jbase.spring.mvc.annotation.ApiVersion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    private static final String PARAM_VERSION = "v";

    ApiVersion.Model model = ApiVersion.Model.COMPATIBLY;
    int version = 1;
    CompareVersion compatibly = new CompareVersion();
    CompareVersion incompatibly = new CompareVersion();


    public ApiVersionCondition(ApiVersion.Model model, int version) {
        this.model = model;
        this.version = version;
    }

    public ApiVersionCondition(ApiVersion.Model model, int version, CompareVersion compatibly, CompareVersion incompatibly) {
        this.model = model;
        this.version = version;
        this.compatibly = compatibly;
        this.incompatibly = incompatibly;
    }


    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {


        return this.version >= other.version ? this : other;
    }


    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {

        int version = getParamVersion(request);

        if (model == ApiVersion.Model.ASSIGN) {
            return version == this.version ? this : null;
        } else if (model == ApiVersion.Model.COMPATIBLY) {
            if (version >= this.version) {
                return this;
            } else {
                return incompatibly.contains(version) ? null : this; // 兼容版本
            }
        } else if (model == ApiVersion.Model.INCOMPATIBLE) {
            if (version >= this.version) {
                return this;
            } else {
                return compatibly.contains(version) ? this : null; // 兼容版本
            }
        } else {
            return this;
        }
    }


    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return other.version - this.version;
    }


    private int getParamVersion(HttpServletRequest request) {
        String version = null;
        String queryString = request.getQueryString();

        if (StringUtils.isEmpty(queryString)) {
            return 1;
        }

        String[] pathParam = queryString.split("&");

        if (pathParam.length > 0) {
            for (String s : pathParam) {

                if (s.contains("=")) {
                    String k = s.substring(0, s.indexOf("="));
                    String v = s.substring(s.indexOf("=") + 1);

                    if (PARAM_VERSION.equals(k)) {
                        version = v;
                        break;
                    }
                }
            }
        }

        return Integer.parseInt(version);
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setCompatibly(CompareVersion compatibly) {
        this.compatibly = compatibly;
    }

    public void setIncompatibly(CompareVersion incompatibly) {
        this.incompatibly = incompatibly;
    }


    public static class CompareVersion {
        List<Integer> special = new ArrayList<>();

        List<Range> ranges = new ArrayList<>();

        public void addSpecial(Integer i) {
            special.add(i);
        }

        public void addRange(Integer s, Integer e) {
            ranges.add(new Range(s, e));
        }

        public boolean contains(Integer s) {

            if (isEmpty()) {
                return false;
            }

            if (special.size() > 0 && special.contains(s)) {
                return true;
            }

            if (ranges.size() > 0) {
                for (Range range : ranges) {
                    if (range.contains(s)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean isEmpty() {

            return special.isEmpty() && ranges.isEmpty();
        }
    }

    public static class Range {
        int start;
        int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(Integer s) {
            return s >= start && s <= end;
        }

    }
}
