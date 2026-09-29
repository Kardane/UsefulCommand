"""Publish a verified GitHub release JAR using the repository's Modrinth secret."""

import hashlib
import json
import os
import re
import sys
import urllib.error
import urllib.request
import uuid
import zipfile
from pathlib import Path


PROJECT_ID = "vnLkFlcH"
FABRIC_API_ID = "P7dR8mSH"
API = "https://api.modrinth.com/v2"
HEADERS = {"User-Agent": "Kardane/UsefulCommand release workflow"}


def request_json(url, *, data=None, content_type=None, token=None):
    headers = dict(HEADERS)
    if content_type:
        headers["Content-Type"] = content_type
    if token:
        headers["Authorization"] = token
    request = urllib.request.Request(url, data=data, headers=headers)
    try:
        with urllib.request.urlopen(request, timeout=60) as response:
            return json.load(response)
    except urllib.error.HTTPError as error:
        raise RuntimeError(f"Modrinth API returned HTTP {error.code}") from None


def version_from_properties():
    properties = Path("gradle.properties").read_text(encoding="utf-8")
    match = re.search(r"(?m)^\s*mod_version\s*=\s*(\S+)\s*$", properties)
    if not match:
        raise RuntimeError("mod_version is missing from gradle.properties")
    return match.group(1)


def check_jar(directory):
    version = version_from_properties()
    tag = os.environ.get("RELEASE_TAG", "")
    if tag != f"v{version}":
        raise RuntimeError("Release tag does not match mod_version")
    path = directory / f"karns_UsefulCommand-{version}.jar"
    if not path.is_file():
        raise RuntimeError(f"Expected release JAR is missing: {path}")
    with zipfile.ZipFile(path) as archive:
        metadata = json.loads(archive.read("fabric.mod.json"))
    if (
        metadata.get("id") != "karns_usefulcommand"
        or metadata.get("version") != version
        or metadata.get("depends", {}).get("minecraft") != "26.3"
        or metadata.get("depends", {}).get("fabric-api") != "*"
        or metadata.get("depends", {}).get("java") != ">=25"
    ):
        raise RuntimeError("Release JAR metadata does not match the 26.3 release")
    sha512 = hashlib.sha512(path.read_bytes()).hexdigest()
    print(f"Verified {path.name}: SHA-512 {sha512}")
    return version, path, sha512


def publish(version, path, sha512):
    token = os.environ.get("MODRINTH_TOKEN")
    if not token:
        raise RuntimeError("MODRINTH_TOKEN is unavailable")

    versions = request_json(f"{API}/project/{PROJECT_ID}/version")
    existing = next((item for item in versions if item["version_number"] == version), None)
    if existing:
        if not any(file["hashes"]["sha512"] == sha512 for file in existing["files"]):
            raise RuntimeError("Modrinth version number exists with a different JAR")
        version_id = existing["id"]
    else:
        metadata = {
            "name": version,
            "version_number": version,
            "changelog": "Updated for Minecraft 26.3 with Mojang names and Java 25. Migrated commands and mixins to the 26.3 API.",
            "dependencies": [{"project_id": FABRIC_API_ID, "dependency_type": "required"}],
            "game_versions": ["26.3"],
            "version_type": "release",
            "loaders": ["fabric"],
            "status": "listed",
            "project_id": PROJECT_ID,
            "file_parts": ["file"],
            "primary_file": "file",
            "environment": "server_only",
        }
        boundary = f"----UsefulCommand{uuid.uuid4().hex}"
        body = (
            f"--{boundary}\r\nContent-Disposition: form-data; name=\"data\"\r\nContent-Type: application/json\r\n\r\n"
        ).encode() + json.dumps(metadata).encode() + (
            f"\r\n--{boundary}\r\nContent-Disposition: form-data; name=\"file\"; filename=\"{path.name}\"\r\n"
            "Content-Type: application/java-archive\r\n\r\n"
        ).encode() + path.read_bytes() + f"\r\n--{boundary}--\r\n".encode()
        created = request_json(
            f"{API}/version",
            data=body,
            content_type=f"multipart/form-data; boundary={boundary}",
            token=token,
        )
        version_id = created["id"]

    verified = request_json(f"{API}/version/{version_id}")
    if (
        verified["project_id"] != PROJECT_ID
        or verified["version_number"] != version
        or verified["status"] != "listed"
        or verified["game_versions"] != ["26.3"]
        or not any(file["primary"] and file["hashes"]["sha512"] == sha512 for file in verified["files"])
    ):
        raise RuntimeError("Published Modrinth version did not pass verification")
    print(f"Published and verified https://modrinth.com/mod/karns-useful-command/version/{version_id}")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        raise SystemExit("Usage: publish_modrinth.py <release-jar-directory>")
    release_version, jar_path, jar_sha512 = check_jar(Path(sys.argv[1]))
    publish(release_version, jar_path, jar_sha512)
