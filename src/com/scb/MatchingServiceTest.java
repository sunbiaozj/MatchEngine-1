package com.scb;

import com.scb.match.ReconType;
import com.scb.match.service.MatchingService;

import java.util.Objects;

/**
 * @author DAO TUAN ANH (DENNIS).
 * @created on 15/7/16.
 */
public class MatchingServiceTest {

    public static void main(String[] args) {
        MatchingService matchingService = new MatchingService();
        matchingService.reconcile(ReconType.LINE_BY_LINE);
        verifyLineByLine(matchingService);

        matchingService = new MatchingService();
        matchingService.reconcile(ReconType.X_AS_REFERENCE);
        verifyXAsReference(matchingService);
    }

    private static void verifyLineByLine(MatchingService matchingService) {
        assert Objects.nonNull(matchingService.getStrongMatchItemMap().get("x0y0"));
        assert Objects.nonNull(matchingService.getWeakMatchItemMap().get("x1y1"));
        assert Objects.nonNull(matchingService.getWeakMatchItemMap().get("x2y2"));
        assert Objects.nonNull(matchingService.getWeakMatchItemMap().get("x3y3"));
        assert Objects.nonNull(matchingService.getWeakMatchItemMap().get("x6y6"));

        assertCommon(matchingService);
    }

    private static void verifyXAsReference(MatchingService matchingService) {
        assert Objects.equals("x0", matchingService.getStrongMatchItemMap().get("x0").getId1())
                && Objects.equals("y0", matchingService.getStrongMatchItemMap().get("x0").getId2());

        assert Objects.equals("x1", matchingService.getWeakMatchItemMap().get("x1").getId1())
                && Objects.equals("y1", matchingService.getWeakMatchItemMap().get("x1").getId2());

        assert Objects.equals("x2", matchingService.getWeakMatchItemMap().get("x2").getId1())
                && Objects.equals("y2", matchingService.getWeakMatchItemMap().get("x2").getId2());

        assert Objects.equals("x3", matchingService.getWeakMatchItemMap().get("x3").getId1())
                && Objects.equals("y3", matchingService.getWeakMatchItemMap().get("x3").getId2());

        assert Objects.equals("x6", matchingService.getWeakMatchItemMap().get("x6").getId1())
                && Objects.equals("y6", matchingService.getWeakMatchItemMap().get("x6").getId2());
    }

    private static void assertCommon(MatchingService matchingService) {
        assert Objects.nonNull(matchingService.getBreakItemMapX().get("x4"));
        assert Objects.nonNull(matchingService.getBreakItemMapX().get("x5"));
        assert Objects.nonNull(matchingService.getBreakItemMapX().get("x7"));

        assert Objects.nonNull(matchingService.getBreakItemMapY().get("y4"));
        assert Objects.nonNull(matchingService.getBreakItemMapY().get("y5"));
        assert Objects.nonNull(matchingService.getBreakItemMapY().get("y7"));
    }
}
