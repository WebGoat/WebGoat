from typing import Optional

import json
import os
import urllib.parse

import requests  # type: ignore[import-untyped]


def download_report(
    sonarqube_host_url: str,
    sonarqube_token: str,
    sonarqube_org: str,
    sonarqube_project: str,
    branch: Optional[str],
    pull_request_id: Optional[str],
    report_path: str,
):
    headers = {"Authorization": f"Bearer {sonarqube_token}"}
    additional_search_params: dict[str, str] = {}

    if pull_request_id:
        additional_search_params["pullRequest"] = pull_request_id
    elif branch:
        additional_search_params["branch"] = branch
    else:
        raise ValueError("Branch or pull request id is required")

    issues = _get_all_pages(
        # https://sonarcloud.io/web_api/api/issues/search?deprecated=false
        f"{sonarqube_host_url}/api/issues/search?"
        + urllib.parse.urlencode(
            {
                "additionalFields": "_all",
                "organization": sonarqube_org,
                "projects": sonarqube_project,
                **additional_search_params,
            }
        ),
        headers,
        "issues",
        ["issues", "components", "rules"],
    )

    hotspots = _get_all_pages(
        # https://sonarcloud.io/web_api/api/hotspots/search?deprecated=false
        f"{sonarqube_host_url}/api/hotspots/search?"
        + urllib.parse.urlencode(
            {
                "projectKey": sonarqube_project,
                **additional_search_params,
            }
        ),
        headers,
        "hotspots",
        ["hotspots", "components"],
    )

    hotspot_rules = _get_all_pages(
        # https://sonarcloud.io/web_api/api/rules/search?deprecated=false
        f"{sonarqube_host_url}/api/rules/search?"
        + urllib.parse.urlencode(
            {
                "organization": sonarqube_org,
                "rule_keys": ",".join(
                    hotspot["ruleKey"] for hotspot in hotspots["hotspots"]
                ),
                "f": "name,lang,severity",
            }
        ),
        headers,
        "rules",
        ["rules"],
    )

    report = issues
    report["issues"] += hotspots["hotspots"]
    report["components"] += hotspots["components"]
    report["rules"] += hotspot_rules["rules"]

    with open(report_path, "w") as f:
        json.dump(report, f, indent=2)


def _get_all_pages(
    partial_url: str, headers: dict, stop_prop_name: str, merge_props: list[str]
) -> dict:
    result = {}
    page = 1

    while True:
        with requests.get(f"{partial_url}&p={page}&ps=500", headers=headers) as r:
            r.raise_for_status()
            page_data = r.json()

            if page == 1:
                result = page_data
            else:
                for merge_prop in merge_props:
                    result[merge_prop] += page_data[merge_prop]

            if len(page_data[stop_prop_name]) == 0:
                break

            page += 1

    return result


if __name__ == "__main__":
    download_report(
        os.environ["SONARQUBE_HOST_URL"],
        os.environ["SONARQUBE_TOKEN"],
        os.environ["SONARQUBE_ORG"],
        os.environ["SONARQUBE_PROJECT"],
        os.environ.get("BRANCH", ""),
        os.environ.get("PULL_REQUEST_ID", ""),
        os.environ["REPORT_PATH"],
    )
