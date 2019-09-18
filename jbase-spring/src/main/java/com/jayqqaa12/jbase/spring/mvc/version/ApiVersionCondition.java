package com.jayqqaa12.jbase.spring.mvc.version;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {


    private ApiVersion.Model model;
    private int version = 1;
    private CompareVersion compatibly;
    private CompareVersion incompatibly;


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
        }
        return this;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return this.version >= other.version ? this : other;
    }

    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return other.version - this.version;
    }


    private int getParamVersion(HttpServletRequest request) {
        String version = request.getHeader("version");
        
        return Integer.parseInt(version);
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
