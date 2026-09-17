$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
Push-Location $projectRoot

try {
    mvn spring-boot:run `
        "-Dspring-boot.run.arguments=--demo.failure-category=rate-limit" `
        "-Dspring-boot.run.jvmArguments=-Dspring.devtools.restart.enabled=false"
    exit $LASTEXITCODE
} finally {
    Pop-Location
}
