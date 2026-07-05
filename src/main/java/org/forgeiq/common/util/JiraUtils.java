package org.forgeiq.common.util;

import org.forgeiq.planning.entity.Breakdown;
import org.forgeiq.planning.entity.Epic;
import org.forgeiq.project.entity.Project;

import java.util.Objects;

public class JiraUtils {

    private JiraUtils() {
    }

    public static String buildIssueUrl(String baseUrl, String issueKey) {
        if (baseUrl == null || issueKey == null) {
            return null;
        }
        return baseUrl + "/browse/" + issueKey;
    }
}
